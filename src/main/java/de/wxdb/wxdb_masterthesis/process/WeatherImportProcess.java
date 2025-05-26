package de.wxdb.wxdb_masterthesis.process;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.wxdb.wxdb_masterthesis.dto.BrightskyApiSourceResponse;
import de.wxdb.wxdb_masterthesis.dto.BrightskySynopResponse;
import de.wxdb.wxdb_masterthesis.dto.DwdSourceData;
import de.wxdb.wxdb_masterthesis.dto.WeatherHistoricalData;
import de.wxdb.wxdb_masterthesis.dto.WeatherRealtimeData;
import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;
import de.wxdb.wxdb_masterthesis.service.BrightskyApiService;
import de.wxdb.wxdb_masterthesis.service.InfluxDbReadWeatherDataService;
import de.wxdb.wxdb_masterthesis.utils.FluxQueryTemplate;
import de.wxdb.wxdb_masterthesis.utils.LocalDateTimeRange;
import de.wxdb.wxdb_masterthesis.utils.WeatherDataMapper;
import de.wxdb.wxdb_masterthesis.utils.WeatherDataValidator;

@Component
public class WeatherImportProcess {
	
	private static final Logger log = LoggerFactory.getLogger(WeatherImportProcess.class);

	@Autowired
	private InfluxDbReadWeatherDataService weatherDataService;

	@Autowired
	private BrightskyApiService brightskyApiService;

	public void importWeatherData(LocalDate startDate, LocalDate endDate, boolean getRealtimeData,
			boolean isHourlyInterval) {
		if (startDate == null) {
			// if no startDate given retrieve current weeks real time data
			startDate = LocalDate.now().minusDays(7);
			getRealtimeData = true;
		}

		List<WxdbWeatherData> weatherData = new ArrayList<WxdbWeatherData>();
		// 1. Retrieve InfluxDB Datasets
		if (getRealtimeData) {
			// if real time is activated then set endDate to today due to influxDB real time data is limited to the current date range.
			endDate = LocalDate.now();

			List<WeatherRealtimeData> influxRtData = isHourlyInterval
					? weatherDataService.retrieveRTWeatherData(startDate, FluxQueryTemplate.REALTIME_1H)
					: weatherDataService.retrieveRTWeatherData(startDate, FluxQueryTemplate.REALTIME_10M);
			influxRtData.forEach(rtd -> weatherData.add(WeatherDataMapper.fromRealtimeData(rtd)));
		} else {
			List<WeatherHistoricalData> influxHistoricalData = isHourlyInterval
					? weatherDataService.retrieveHistoricalWeatherData(startDate, endDate,
							FluxQueryTemplate.HISTORICAL_1H)
					: weatherDataService.retrieveHistoricalWeatherData(startDate, endDate,
							FluxQueryTemplate.HISTORICAL_10M);
			influxHistoricalData.forEach(hd -> weatherData.add(WeatherDataMapper.fromHistoricalData(hd)));
		}

		// 2. Check Datasets which are missing or including invalid values.
		List<WxdbWeatherData> invalidWeatherData = weatherData.stream()
				.filter(data -> !WeatherDataValidator.isValid(data)).collect(Collectors.toList());

		// 3. If list of invalid weatherdata is empty, skip to 7.
		if (!invalidWeatherData.isEmpty()) {
			// 4. parse the date range of the invalid datasets
			Set<LocalDateTime> invalidTimestamps = invalidWeatherData.stream().map(WxdbWeatherData::getTime)
					.filter(Objects::nonNull).collect(Collectors.toCollection(TreeSet::new)); // sortiert

			List<LocalDateTimeRange> invalidRanges = LocalDateTimeRange
					.groupToBroadRanges(new ArrayList<>(invalidTimestamps), 50);

			// 5. Retrieve missing data sets from other ext. sources -- 5.1. Retrieve DWD-Stations from BrightskyApi
			List<DwdSourceData> locations = brightskyApiService.getDwdStations(null, null);
			List<DwdSourceData> nearestLocations = filterNearestSources(locations, 20);
			if (nearestLocations.isEmpty()) {
				log.warn("No Weather Stations were found!");
			}

			// map dwdStationId or WmoStationId - some stations have only one of both ids
			List<Long> stationSourceIds = new ArrayList<>();
			for (DwdSourceData source : nearestLocations) {
				stationSourceIds.add(source.getId());
			}
			List<WxdbWeatherData> validDatasets = new ArrayList<WxdbWeatherData>();
			
			// 5.2. Retrieve data sets from BrightskyApi for the Set Time ranges
			invalidRanges.forEach(iR -> {
				// difference between endpoints due to intervals
				BrightskyApiSourceResponse weatherResponse = new BrightskyApiSourceResponse();
				
				if (!isHourlyInterval && iR.getStartDate().isAfter(LocalDateTime.now().minusHours(31))) {
					// only 30 hours old data can be retrieved
					BrightskySynopResponse synopResponse = brightskyApiService.getDwdData10MinutesInterval(stationSourceIds);
					// todo: synopresponse zu liste von daten mappen
					validDatasets.addAll(WeatherDataMapper.mapSynopDataList(synopResponse.getWeather()));
					
				} else {
					weatherResponse = brightskyApiService.getDwdWeatherDataHourlyInterval(iR.getStartDate().toLocalDate(), 
							iR.getEndDate().toLocalDate(), stationSourceIds);
					validDatasets.addAll(WeatherDataMapper.mapDwdWeatherDataList(weatherResponse.getWeather()));
				}
			});
			// nun validieren wir die neuen Daten
			validDatasets.removeIf(cD -> !WeatherDataValidator.isValid(cD));
			List<WxdbWeatherData> correctedDatasets =  WeatherDataValidator.deduplicateAndImprove(validDatasets);

			// 6. correct invalid data sets with other data - Imputationprocess
			

		}
		// 7. Integrate the data sets to wxdb.

	}

	/**
	 * Filters nearest Locations limited by count.
	 * 
	 * @param locations       Weatherstations
	 * @param limitationCount count to limit the set Array.
	 * @return List of Weatherstations - {@link DwdSourceData}.
	 */
	private List<DwdSourceData> filterNearestSources(List<DwdSourceData> locations, int limitationCount) {
		List<DwdSourceData> nearestLocations = locations.stream().filter(source -> source.getDistance() != null)
				.sorted(Comparator.comparing(DwdSourceData::getDistance)).limit(limitationCount)
				.collect(Collectors.toList());
		return nearestLocations;
	}

}
