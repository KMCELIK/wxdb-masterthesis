package de.wxdb.wxdb_masterthesis.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import de.wxdb.wxdb_masterthesis.dto.WeatherHistoricalData;
import de.wxdb.wxdb_masterthesis.dto.WeatherRealtimeData;

/**
 * Mapper Klasse zum transformieren von Flux Ergebnissen zu einer Liste von
 * {@link WeatherRealtimeData} Objekten. 
 * Mapper class to map flux query
 * responses to a list of {@link WeatherRealtimeData}.
 * 
 * @author Kaan Mustafa Celik
 */
public class FluxTableMapper {

	/**
	 * Methode zum transformieren von Flux-Ergebnissen zu einer Liste von
	 * {@link WeatherRealtimeData}.
	 * 
	 * @param tables Query Ergebnis von einer Fluxabfrage - {@link FluxTable}.
	 * @return Liste von {@link WeatherRealtimeData}.
	 */
	public static List<WeatherRealtimeData> mapToWeatherRealtimeData(List<FluxTable> tables) {
		List<WeatherRealtimeData> result = new ArrayList<>();

		for (FluxTable table : tables) {
			for (FluxRecord record : table.getRecords()) {
				WeatherRealtimeData data = new WeatherRealtimeData();

				// Zeit konvertieren: Instant -> LocalDateTime (UTC)
				Instant timeInstant = record.getTime();
				if (timeInstant != null) {
					LocalDateTime time = LocalDateTime.ofInstant(timeInstant, ZoneOffset.UTC);
					data.setTime(time);
				}

				// Feldwerte aus gepivoteter Zeile extrahieren
				data.setGlobRT(toDouble(record.getValueByKey("glob_RT")));
				data.setTempRT(toDouble(record.getValueByKey("temp_RT")));
				data.setWindRT(toDouble(record.getValueByKey("wind_RT")));
				data.setWindgeschwindigRT(toDouble(record.getValueByKey("windgeschwindig_RT")));

				result.add(data);
			}
		}

		return result;
	}
	
	/**
	 * Methode zum Transformieren von Flux-Ergebnissen zu einer Liste von
	 * {@link WeatherHistoricalData}.
	 *
	 * @param tables Query-Ergebnis einer Flux-Abfrage - {@link FluxTable}.
	 * @return Liste von {@link WeatherHistoricalData}.
	 */
	public static List<WeatherHistoricalData> mapToWeatherHistoricalData(List<FluxTable> tables) {
		List<WeatherHistoricalData> result = new ArrayList<>();

		for (FluxTable table : tables) {
			for (FluxRecord record : table.getRecords()) {
				WeatherHistoricalData data = new WeatherHistoricalData();

				Instant timeInstant = record.getTime();
				if (timeInstant != null) {
					LocalDateTime time = LocalDateTime.ofInstant(timeInstant, ZoneOffset.UTC);
					data.setTime(time);
				}

				// Feldwerte aus gepivoteter Zeile extrahieren
				data.setGlobAVG(toDouble(record.getValueByKey("glob_AVG")));
				data.setTempAVG(toDouble(record.getValueByKey("temp_AVG")));
				data.setWindAVG(toDouble(record.getValueByKey("wind_AVG")));
				data.setWindgeschwindigAVG(toDouble(record.getValueByKey("windgeschwindig_AVG")));

				result.add(data);
			}
		}

		return result;
	}

	/**
	 * Transform Object to Double
	 * 
	 * @param value object
	 * @return double value
	 */
	private static Double toDouble(Object value) {
		if (value instanceof Number num) {
			return num.doubleValue();
		}
		try {
			return value != null ? Double.parseDouble(value.toString()) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}