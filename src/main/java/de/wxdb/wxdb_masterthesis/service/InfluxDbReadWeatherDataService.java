package de.wxdb.wxdb_masterthesis.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxTable;

import de.wxdb.wxdb_masterthesis.dto.WeatherHistoricalData;
import de.wxdb.wxdb_masterthesis.dto.WeatherRealtimeData;
import de.wxdb.wxdb_masterthesis.utils.FluxQueryTemplate;
import de.wxdb.wxdb_masterthesis.utils.FluxTableMapper;
import de.wxdb.wxdb_masterthesis.utils.TimeFormatterUtil;

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
public class InfluxDbReadWeatherDataService {
	private static final Logger log = LoggerFactory.getLogger(InfluxDbReadWeatherDataService.class);

	@Autowired
	private InfluxDBClient influxDBClient;

	@Value("${influx.bucket1}")
	private String bucketRt;

	@Value("${influx.bucket2}")
	private String bucketHistorical;

	@Value("${influx.measurement}")
	private String measurement;

	/**
	 * Method to retrieve Realtime Weather Data from the InfluxDB.
	 * @param startDate start Date 
	 * @param timeInterval time interval 10m or 1h
	 * @return List of {@link WeatherRealtimeData}
	 */
	public List<WeatherRealtimeData> retrieveRTWeatherData(LocalDate startDate, FluxQueryTemplate timeInterval) {
		log.info("[Starte retrieve real time weather data service] - with time interval " + timeInterval.getTimeInterval()
				+ " and startDate: " + startDate);
		if (startDate == null || timeInterval == null) {
			log.error("[Retrieve real time weather data] - parameter missing, return empty list ");
			return Collections.emptyList();
		}
		
		// prepare influx db query
		String query = timeInterval.render(TimeFormatterUtil.formatAsUtcIso(startDate), null, bucketRt, measurement);
		
		// retrieve datasets from influxdb
		List<FluxTable> results = influxDBClient.getQueryApi().query(query);
		
		// map and return retrieved data
		return FluxTableMapper.mapToWeatherRealtimeData(results);
	}
	
	/**
	 * Method to retrieve historical Weather Data from the InfluxDB.
	 * @param startDate start Date 
	 * @param endDate end Date
	 * @param timeInterval time interval 10m or 1h
	 * @return List of {@link WeatherHistoricalData}
	 */
	public List<WeatherHistoricalData> retrieveHistoricalWeatherData(LocalDate startDate, LocalDate endDate, FluxQueryTemplate timeInterval) {
		log.info("[Retrieve historical weather data] - with time interval " + timeInterval.getTimeInterval()
				+ " and startDate: " + startDate + " and endDate: " + endDate);
		if (startDate == null || timeInterval == null || endDate == null) {
			log.error("[Retrieve historical weather data] - parameter missing, return empty list ");
			return Collections.emptyList();
		}
		// prepare influx db query
		String query = timeInterval.render(TimeFormatterUtil.formatAsUtcIso(startDate), TimeFormatterUtil.formatAsUtcIso(endDate), bucketHistorical, measurement);
		
		// retrieve datasets from influxdb
		List<FluxTable> results = influxDBClient.getQueryApi().query(query);
		
		// map and return retrieved data
		return FluxTableMapper.mapToWeatherHistoricalData(results);
	}
}