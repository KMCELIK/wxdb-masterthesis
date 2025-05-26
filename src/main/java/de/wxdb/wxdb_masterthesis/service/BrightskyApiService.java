package de.wxdb.wxdb_masterthesis.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.wxdb.wxdb_masterthesis.api.BrightskyFeignClient;
import de.wxdb.wxdb_masterthesis.dto.BrightskyApiSourceResponse;
import de.wxdb.wxdb_masterthesis.dto.BrightskySynopResponse;
import de.wxdb.wxdb_masterthesis.dto.DwdSourceData;

@Service
public class BrightskyApiService {

	private static final Logger log = LoggerFactory.getLogger(BrightskyApiService.class);
	/**
	 * default maximum distance setting to referenced distance to our location.
	 */
	private final static long DISTANCE = 50000;

	@Value("${brightsky.base-url}")
	private String baseUrl;

	@Value("${coordinates.lat}")
	private double latitude;

	@Value("${coordinates.lon}")
	private double longitude;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private BrightskyFeignClient feignClient;

	/**
	 * Returns a list of dwd weather stations {@link DwdSourceData}.
	 * 
	 * @param lat latitude
	 * @param lon longtitude
	 * @return list of {@link DwdSourceData}
	 */
	public List<DwdSourceData> getDwdStations(Double lat, Double lon) {
		if (lat == null || lon == null) {
			lat = latitude;
			lon = longitude;
		}

		// Set Get-Request
		String url = String.format(Locale.US, "%s/sources?lat=%.5f&lon=%.5f&max_dist=%d", baseUrl, lat, lon, DISTANCE);

		BrightskyApiSourceResponse response = null;

		try {
			// Get Source Response from BrightskyApi
			response = restTemplate.getForObject(url, BrightskyApiSourceResponse.class);
		} catch (RestClientException ex) {
			log.error("Error occured while retrieving sources by BrightskyApi: " + url, ex);
		} catch (Exception ex) {
			log.error("Unexcepted error occured while retrieving sources by BrightskyApi", ex);
		}

		return response != null ? response.getSources() : Collections.emptyList();
	}
	
	public BrightskyApiSourceResponse getDwdWeatherDataHourlyInterval(LocalDate startDate, LocalDate endDate, List<Long> stationSourceIds) {
		if (endDate == null) {
			endDate = LocalDate.now();
		}
		BrightskyApiSourceResponse response = new BrightskyApiSourceResponse();
		
		try {
			 response = feignClient.getWeatherBySourceIds(startDate.toString(), endDate.toString(), stationSourceIds);
		} catch (HttpStatusCodeException ex) {
			log.error("Brightsky API Fehler: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
		} catch (RuntimeException ex) {
			log.error("Allgemeiner Fehler beim Abruf der Brightsky API: ", ex);
		}
		
		// case if sourceIds didn't get any data
		if (response.getWeather().isEmpty()) {
			try {
				response = feignClient.getWeatherByGeography(startDate.toString(), endDate.toString(), latitude, longitude);
			} catch (HttpStatusCodeException ex) {
				log.error("Brightsky API Fehler: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
			} catch (RuntimeException ex) {
				log.error("Allgemeiner Fehler beim Abruf der Brightsky API: ", ex);
			}
		}
		
		return response;
		
	}

	/**
	 * This Method can only retrieve data that is 30 hours old but in 10 minute intervals
	 * @param startDate
	 * @param endDate
	 * @param dwdIds
	 * @param wmoIds
	 * @return
	 */
	public BrightskySynopResponse getDwdData10MinutesInterval(List<Long> synopStationIds) {
		LocalDate synopDate = LocalDate.now().minusDays(2);
		BrightskySynopResponse response = new BrightskySynopResponse();
		
		try {
			 response = feignClient.getSynop(synopDate.toString(), LocalDate.now().toString(), synopStationIds);
		} catch (HttpStatusCodeException ex) {
			log.error("Brightsky API Fehler: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
		} catch (RuntimeException ex) {
			log.error("Allgemeiner Fehler beim Abruf der Brightsky API: ", ex);
		}
		
		return response != null ? response : new BrightskySynopResponse();
	}

}
