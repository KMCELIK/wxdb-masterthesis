package de.wxdb.wxdb_masterthesis.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.query.FluxTable;

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

			for (WeatherRealtimeData entry : data) {
				log.info("Zeit: {}, Glob: {}, Temp: {}, Wind: {}, Windgeschwindigkeit: {}", entry.getTime(),
						formatDouble(entry.getGlobRT()), formatDouble(entry.getTempRT()),
						formatDouble(entry.getWindRT()), formatDouble(entry.getWindgeschwindigRT()));
				log.info(data.toString());
			}

			return;
		}
	}

	private String formatDouble(Double value) {
		return value != null ? String.format("%.2f", value) : "null";
	}
}
