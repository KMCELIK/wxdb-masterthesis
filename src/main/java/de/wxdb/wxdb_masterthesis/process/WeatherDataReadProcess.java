package de.wxdb.wxdb_masterthesis.process;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.wxdb.wxdb_masterthesis.dto.DwdSourceData;
import de.wxdb.wxdb_masterthesis.service.InfluxDbReadWeatherDataService;

@Component
public class WeatherDataReadProcess {
	
	@Autowired
	private InfluxDbReadWeatherDataService weatherDataService;
	

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
	
	/*
	 * List<DwdSourceData> locations = brightskyApiService.getDwdStations(null,
	 * null);
	 * 
	 * 
	 * List<DwdSourceData> nearestLocations = filterNearestSources(locations, 3);
	 * 
	 * List<String> dwdStationIds = new ArrayList<>(); List<String> wmoStationIds =
	 * new ArrayList<>();
	 * 
	 * for (DwdSourceData source : top3Locations) { if (source.getDwdStationId() !=
	 * null && !source.getDwdStationId().isEmpty()) {
	 * dwdStationIds.add(source.getDwdStationId()); } if (source.getWmoStationId()
	 * != null && !source.getWmoStationId().isEmpty()) {
	 * wmoStationIds.add(source.getWmoStationId()); } }
	 * 
	 * @todo Extraktion der Wetterdaten von DWD. BrightskySynopResponse
	 * synopResponse = brightskyApiService.getDwdData10MinutesInterval(startDate,
	 * dwdStationIds, wmoStationIds);
	 * 
	 * log.info(synopResponse.toString());
	 */
}
