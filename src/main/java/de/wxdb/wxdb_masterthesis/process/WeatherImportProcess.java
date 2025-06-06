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
import de.wxdb.wxdb_masterthesis.schema.Processlog;
import de.wxdb.wxdb_masterthesis.service.BrightskyApiService;
import de.wxdb.wxdb_masterthesis.service.InfluxDbReadWeatherDataService;
import de.wxdb.wxdb_masterthesis.service.InsertWxdbService;
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
	
	@Autowired
	private InsertWxdbService insertWxdbService;

	public void importWeatherDataByTypeAndInterval(LocalDate startDate, LocalDate endDate, boolean getRealtimeData,
			boolean isHourlyInterval) {
		if (startDate == null || getRealtimeData) {
			// if no startDate given retrieve current weeks real time data
			startDate = LocalDate.now().minusDays(2);
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
			List<DwdSourceData> nearestLocations = filterNearestSources(locations, 100);
			if (nearestLocations.isEmpty()) {
				log.warn("No Weather Stations were found!");
			}

			// map dwdStationId or WmoStationId - some stations have only one of both ids
			List<Long> stationSourceIds = new ArrayList<>();
			for (DwdSourceData source : nearestLocations) {
				stationSourceIds.add(source.getId());
			}
			List<WxdbWeatherData> validDatasets = new ArrayList<WxdbWeatherData>();
			
			// 5.2. Imputation - Retrieve data sets from BrightskyApi for the Set Time ranges
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
			validDatasets.addAll(weatherData);
			// nun validieren wir die neuen Daten
			validDatasets.removeIf(cD -> !WeatherDataValidator.isValid(cD)); // Frage: was ist wenn ein Wert: sonnenstrahlung = 0.0.
			// duplizierte Werte vereinheitlichen, genauere Messungen bevorzugen
			List<WxdbWeatherData> correctedDatasets =  WeatherDataValidator.deduplicateAndImprove(validDatasets);
			// sortieren nach Zeit ASC
			correctedDatasets.sort(Comparator.comparing(WxdbWeatherData::getTime));
		}
		// 7. Integrate the data sets to wxdb.

	}
	
	public void importWeatherData(LocalDate startDate, LocalDate endDate) {
		// 1. Prüfen ob Zeitraum gültig ist.
		if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
	        log.warn("Ungültiger Zeitraum für den Import.");
	        return;
	    }
		
		Processlog processLog =  insertWxdbService.startProcessLog("IMPORT-WeatherData", LocalDateTime.now());

	    List<WxdbWeatherData> collected = new ArrayList<>();

	    // 1. Realtime Daten (sofern Zeitraum gültig) - InlfuxDB
	    if (!endDate.isBefore(LocalDate.now())) {
	        log.info("Versuche Realtime 10-Minuten-Daten...");
	        collected.addAll(weatherDataService.retrieveRTWeatherData(startDate, FluxQueryTemplate.REALTIME_10M)
	                .stream().map(WeatherDataMapper::fromRealtimeData).toList());

	        log.info("Versuche Realtime Stunden-Daten...");
	        collected.addAll(weatherDataService.retrieveRTWeatherData(startDate, FluxQueryTemplate.REALTIME_1H)
	                .stream().map(WeatherDataMapper::fromRealtimeData).toList());
	    }

	    // 2. Historische Daten - InfluxDB
	    log.info("Versuche historische 10-Minuten-Daten...");
	    collected.addAll(weatherDataService.retrieveHistoricalWeatherData(startDate, endDate, FluxQueryTemplate.HISTORICAL_10M)
	            .stream().map(WeatherDataMapper::fromHistoricalData).toList());

	    log.info("Versuche historische Stunden-Daten...");
	    collected.addAll(weatherDataService.retrieveHistoricalWeatherData(startDate, endDate, FluxQueryTemplate.HISTORICAL_1H)
	            .stream().map(WeatherDataMapper::fromHistoricalData).toList());

	    // 3. unvollständige Datensätze herausfiltern
	    log.info("valid datasets before filter: " + collected.size());
	    List<WxdbWeatherData> invalidData = new ArrayList<>();
	    collected.removeIf(wd -> {
	        if (wd == null || !WeatherDataValidator.isValid(wd)) {
	            invalidData.add(wd); // invalidData ist z. B. eine separate List<WeatherData>
	            return true; // Entferne dieses Element aus collected
	        }
	        return false; // Behalte das Element
	    });
	    
	    log.info("valid datasets after filter: " + collected.size());

	    // Analyse der fehlerhaften Datensätze falls vorhanden - InfluxDB
	    if (!invalidData.isEmpty()) {
	        log.info("Es existieren noch {} ungültige Datensätze. Versuche externe Quellen (Brightsky)", invalidData.size());

	        // Bestimmen der Zeiträume für die fehlerhaften Zeiträume falls es mehrere Zeitraum lücken gibt.
	        Set<LocalDateTime> invalidTimestamps = invalidData.stream()
	                .map(WxdbWeatherData::getTime)
	                .filter(Objects::nonNull)
	                .collect(Collectors.toCollection(TreeSet::new));

	        // wenn es Lücken gibt welche zu lang sind z.B. 30 Stunden, dann werden verteilte Zeiträume erstellt (um die Schnittstelle nicht zu sehr auszulasten)
	        List<LocalDateTimeRange> invalidRanges = LocalDateTimeRange.groupToBroadRanges(
	                new ArrayList<>(invalidTimestamps), 30);

	        // Filterung der 100 nahsten Wetterstationen in einem Umkreis von 50km
	        List<DwdSourceData> stations = brightskyApiService.getDwdStations(null, null);
	        List<Long> nearestStationIds = filterNearestSources(stations, 100).stream()
	                .map(DwdSourceData::getId)
	                .collect(Collectors.toList());

			for (LocalDateTimeRange range : invalidRanges) {
				// gesammter Zeitraum ist inkludiert d.h. 10-minuten Daten sind voll abrufbar und müssten ausreichen
				boolean isRecent = range.getStartDate().isAfter(LocalDateTime.now().minusHours(31));
				if (isRecent) {
					BrightskySynopResponse synop = brightskyApiService.getDwdData10MinutesInterval(nearestStationIds);
					collected.addAll(WeatherDataMapper.mapSynopDataList(synop.getWeather()));
				} else {
					// in diesem Fall werden Synop Daten nur beansprucht falls der Zeitraum sich mit unserem Zeitraum von 30h before und LocalDate.now() schneidet.
					if (range.getEndDate().isAfter(LocalDateTime.now().minusHours(31))) {
						BrightskySynopResponse synop = brightskyApiService.getDwdData10MinutesInterval(nearestStationIds);
						collected.addAll(WeatherDataMapper.mapSynopDataList(synop.getWeather()).stream()
								.filter(synopData -> WeatherDataValidator.isValid(synopData) 
										&& WeatherDataValidator.isSynopDaterange(synopData)).collect(Collectors.toList()));
					}

					BrightskyApiSourceResponse hourly = brightskyApiService.getDwdWeatherDataHourlyInterval(
							range.getStartDate().toLocalDate(), range.getEndDate().toLocalDate(), nearestStationIds);
					collected.addAll(WeatherDataMapper.mapDwdWeatherDataList(hourly.getWeather()));
				}
			}
		}
	   
	    // Bereinigen und verbessern
	    List<WxdbWeatherData> result = WeatherDataValidator.deduplicateAndImprove(
	            collected.stream().filter(r -> WeatherDataValidator.isValid(r)).toList());
	    result.sort(Comparator.comparing(WxdbWeatherData::getTime));

	    log.info("Import abgeschlossen. {} Datensätze nach Kompensation generiert.", result.size());
	   
	    // Es fehlt beim Import noch der Imputationsprouzess also das nicht nur die Stationen und Daten gezogen werden sondern
	    // auch immer die Daten welche vollständig und von der Distanz am nahsten sind auch integriert werden, stand jetzt wird einfach der "beste Wert" genommen.
	    // TODO: Integration in Zielsystem
	    try {
		    insertWxdbService.insertWeatherData(result);

			insertWxdbService.completeProcessLog(processLog, true, result.size() + " Datensätze wurden erfolgreich importiert.");
		    log.info("insert completed - datasets: {}", result.toString());
	    } catch (RuntimeException e) {
	    	insertWxdbService.completeProcessLog(processLog, false, e.getMessage() + e.getStackTrace());
	    	log.error("Wxdb Insert failed due to: ",e);
	    }
	    
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
