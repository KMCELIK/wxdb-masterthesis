package de.wxdb.wxdb_masterthesis.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxTable;

import de.wxdb.wxdb_masterthesis.dto.DwdSourceData;
import de.wxdb.wxdb_masterthesis.dto.WeatherRealtimeData;
import de.wxdb.wxdb_masterthesis.utils.FluxQueryTemplate;
import de.wxdb.wxdb_masterthesis.utils.FluxTableMapper;
import de.wxdb.wxdb_masterthesis.utils.TimeFormatterUtil;
import io.micrometer.common.util.StringUtils;

/**
 * Service zur Ausführung von Flux-Abfragen auf InfluxDB. Verwaltet vier
 * gespeicherte Abfragen zur Datenbeschaffung für Echtzeit- und historische
 * Wetterdaten.
 * <p>
 * Wird beim Start des Systems initialisiert und kann später auch über
 * REST-Controller erweitert werden.
 * </p>
 *
 * @author Kaan Mustafa Celik
 */
@Service
public class WeatherDataService {
	private static final Logger log = LoggerFactory.getLogger(WeatherDataService.class);

	@Autowired
	private InfluxDBClient influxDBClient;
	
	@Autowired
	private BrightskyApiService brightskyApiService;

	@Value("${influx.bucket1}")
	private String bucketRt;

	@Value("${influx.bucket2}")
	private String bucketHistorical;

	@Value("${influx.measurement}")
	private String measurement;

	public void retrieveRTWeatherData(LocalDate startDate, String timeInterval) {
		log.info("[Starte retrieve real time weather data service] - with time interval " + timeInterval
				+ " and startDate: " + startDate);

		if (startDate != null && !StringUtils.isBlank(timeInterval)) {

			List<FluxTable> results = influxDBClient.getQueryApi()
					.query(FluxQueryTemplate.REALTIME_1H.render(TimeFormatterUtil.formatAsUtcIso(startDate)));
			List<WeatherRealtimeData> data = FluxTableMapper.mapToWeatherRealtimeData(results);

/*			for (WeatherRealtimeData entry : data) {
				log.info("Zeit: {}, Glob: {}, Temp: {}, Wind: {}, Windgeschwindigkeit: {}", entry.getTime(),
						formatDouble(entry.getGlobRT()), formatDouble(entry.getTempRT()),
						formatDouble(entry.getWindRT()), formatDouble(entry.getWindgeschwindigRT()));
				log.info(data.toString());
			} */

			List<DwdSourceData> locations = brightskyApiService.getDwdStations(null, null);
			// Sortiere nach Distanz aufsteigend und nimm die ersten 3
			List<DwdSourceData> top3Locations = locations.stream()
			    .filter(source -> source.getDistance() != null) // optional: falls distance null sein kann
			    .sorted(Comparator.comparing(DwdSourceData::getDistance))
			    .limit(3)
			    .collect(Collectors.toList());
			
			// die 3 Locations gefiltert welche unserer location am nahsten kommen
			log.info(top3Locations.toString());
			List<String> dwdStationIds = new ArrayList<>();
            List<String> wmoStationIds = new ArrayList<>();
            
			for (DwdSourceData source : top3Locations) {
	            if (source.getDwdStationId() != null && !source.getDwdStationId().isEmpty()) {
	                dwdStationIds.add(source.getDwdStationId());
	            }
	            if (source.getWmoStationId() != null && !source.getWmoStationId().isEmpty()) {
	                wmoStationIds.add(source.getWmoStationId());
	            }
	        }
			
			/* @todo Extraktion der Wetterdaten von DWD.
			BrightskySynopResponse synopResponse = 
					brightskyApiService.getDwdData10MinutesInterval(startDate, dwdStationIds, wmoStationIds);
			
			log.info(synopResponse.toString());
			*/
			return;
		}
	}

	private String formatDouble(Double value) {
		return value != null ? String.format("%.2f", value) : "null";
	}
}
