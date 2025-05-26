package de.wxdb.wxdb_masterthesis.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;

/**
 * Class to validate Weatherdata.
 * 
 * @author Kaan Mustafa Celik
 */
public class WeatherDataValidator {

	/**
	 * Method to validate {@link WxdbWeatherData}.x
	 * 
	 * @param data dataset
	 * @return true if complete dataset.
	 */
	public static boolean isValid(WxdbWeatherData data) {
		// possible NullPointer
		if (data == null)
			return false;

		return data.getTemperature() != null && data.getGlobalRadiation() != null && data.getWindSpeed() != null
				&& data.getWindDirection() != null;
	}
	
	public static List<WxdbWeatherData> deduplicateAndImprove(List<WxdbWeatherData> inputList) {
	    Map<LocalDateTime, WxdbWeatherData> timeToBestEntry = new HashMap<>();

	    for (WxdbWeatherData data : inputList) {
	        LocalDateTime time = data.getTime();
	        WxdbWeatherData existing = timeToBestEntry.get(time);

	        if (existing == null) {
	            timeToBestEntry.put(time, data);
	        } else {
	            WxdbWeatherData better = chooseBetter(existing, data);
	            timeToBestEntry.put(time, better);
	        }
	    }

	    return new ArrayList<>(timeToBestEntry.values());
	}
	
	private static WxdbWeatherData chooseBetter(WxdbWeatherData d1, WxdbWeatherData d2) {
	    WxdbWeatherData result = new WxdbWeatherData();
	    result.setTime(d1.getTime()); // beide haben denselben Timestamp

	    result.setTemperature(betterDouble(d1.getTemperature(), d2.getTemperature()));
	    result.setWindSpeed(betterDouble(d1.getWindSpeed(), d2.getWindSpeed()));
	    result.setWindDirection(betterDouble(d1.getWindDirection(), d2.getWindDirection()));
	    result.setGlobalRadiation(betterDouble(d1.getGlobalRadiation(), d2.getGlobalRadiation()));

	    // Übernehme Meta-Felder vom besseren Datensatz – oder setze Defaults
	    result.setStationSourceId(d1.getStationSourceId()); // optional: vergleichen
	    result.setDatasource(d1.getDatasource() != null ? d1.getDatasource() : d2.getDatasource());
	    result.setWeatherStationSource(d1.getWeatherStationSource() != null ? d1.getWeatherStationSource() : d2.getWeatherStationSource());

	    result.setLastChangedBy("SYSTEM");
	    result.setLastChangedTime(LocalDateTime.now());
	    result.setVersion(Math.max(d1.getVersion(), d2.getVersion())); // falls relevant

	    return result;
	}
	
	private static Double betterDouble(Double d1, Double d2) {
	    if (isInvalid(d1) && isValid(d2)) return d2;
	    if (isInvalid(d2) && isValid(d1)) return d1;
	    if (isValid(d1) && isValid(d2)) return Math.abs(d2) > Math.abs(d1) ? d2 : d1; // z.B. stärkere Messung bevorzugen
	    return null;
	}

	private static boolean isValid(Double d) {
	    return d != null && d != 0.0;
	}

	private static boolean isInvalid(Double d) {
	    return !isValid(d);
	}
}
