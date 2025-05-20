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
import org.springframework.web.client.RestTemplate;

import de.wxdb.wxdb_masterthesis.dto.BrightskyApiSourceResponse;
import de.wxdb.wxdb_masterthesis.dto.BrightskySynopResponse;
import de.wxdb.wxdb_masterthesis.dto.DwdSourceData;

@Service
public class BrightskyApiService {

	private static final Logger log = LoggerFactory.getLogger(BrightskyApiService.class);
	/**
	 * default maximum distance setting to referenced distance to our location.
	 */
	private final static long DISTANCE = 10000;

	@Value("${brightsky.base-url}")
	private String baseUrl;

	@Value("${coordinates.lat}")
	private double latitude ;

	@Value("${coordinates.lon}")
	private double longitude;

	@Autowired
	private RestTemplate restTemplate;

	public List<DwdSourceData> getDwdStations(Double lat, Double lon) {
		if (lat == null || lon == null) {
			lat = latitude;
			lon = longitude;
		} 

		String url = String.format(Locale.US, "%s/sources?lat=%.5f&lon=%.5f&max_dist=%d", baseUrl, lat, lon, DISTANCE);

		BrightskyApiSourceResponse response = restTemplate.getForObject(url, BrightskyApiSourceResponse.class);
		return response != null ? response.getSources() : Collections.emptyList();
	}
	
    public BrightskySynopResponse getDwdData10MinutesInterval(LocalDate startDate, List<String> dwdIds, List<String> wmoIds) {
        LocalDate endDate = LocalDate.now();

        StringBuilder urlBuilder = new StringBuilder(String.format("%s/synop?date=%s&last_date=%s", 
            baseUrl, 
            startDate.toString(), 
            endDate.toString()
        ));

        if (dwdIds != null && !dwdIds.isEmpty()) {
            for (String id : dwdIds) {
                urlBuilder.append("&dwd_station_id=").append(id);
            }
        }

        if (wmoIds != null && !wmoIds.isEmpty()) {
            for (String id : wmoIds) {
                urlBuilder.append("&wmo_station_id=").append(id);
            }
        }

        String url = urlBuilder.toString();

        BrightskySynopResponse response = null;
        try {
            response = restTemplate.getForObject(url, BrightskySynopResponse.class);
        } catch (HttpStatusCodeException ex) {
            log.error("Brightsky API Fehler: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("Allgemeiner Fehler beim Abruf der Brightsky API: ", ex);
        }
        
        return response != null ? response : new BrightskySynopResponse(); // leeres Objekt als Fallback
    }
	
	

}
