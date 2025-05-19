package de.wxdb.wxdb_masterthesis.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import de.wxdb.wxdb_masterthesis.dto.WeatherRealtimeData;

/**
 * Mapper Klasse zum transformieren von Flux Ergebnissen zu einer Liste von {@link WeatherRealtimeData} Objekten.
 * 
 * @author Kaan Mustafa Celik
 */
public class FluxTableMapper {

	/**
	 * Methode zum transformieren von Flux-Ergebnissen zu einer Liste von {@link WeatherRealtimeData}.
	 * @param tables Query Ergebnis von einer Fluxabfrage -  {@link FluxTable}.
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