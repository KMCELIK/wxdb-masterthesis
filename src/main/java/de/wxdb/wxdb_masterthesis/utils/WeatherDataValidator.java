package de.wxdb.wxdb_masterthesis.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;
import de.wxdb.wxdb_masterthesis.schema.ImputationLogPojo;

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
		return data != null && data.getTemperature() != null && data.getGlobalRadiation() != null
				&& data.getWindSpeed() != null && data.getWindDirection() != null;
	}

	public static boolean isSynopDaterange(WxdbWeatherData data) {
		// possible NullPointer
		if (data == null)
			return false;

		return data.getTime() != null && data.getTime().isAfter(LocalDateTime.now().minusHours(31));
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
		// Realtime bevorzugen
		WxdbWeatherData primary = (d1.isRealtime() && !d2.isRealtime()) ? d1
				: (!d1.isRealtime() && d2.isRealtime()) ? d2 : d1;
		WxdbWeatherData secondary = (primary == d1) ? d2 : d1;

		WxdbWeatherData result = new WxdbWeatherData();
		result.setTime(d1.getTime()); // Beide haben denselben Timestamp

		// Globale Strahlung bevorzugt hohen positiven Wert
		result.setGlobalRadiation(betterGlobalRadiation(d1.getGlobalRadiation(), d2.getGlobalRadiation()));

		// Genauere Temperaturdaten bevorzugen
		result.setTemperature(betterPrecision(d1.getTemperature(), d2.getTemperature()));
		result.setWindSpeed(betterPrecision(d1.getWindSpeed(), d2.getWindSpeed()));
		result.setWindDirection(betterPrecision(d1.getWindDirection(), d2.getWindDirection()));

		// Metadaten vom bevorzugten (primary) Datensatz
		/*
		 * hier noch mal logik bzgl der metadaten implementieren. also ich muss
		 * entwieder seperieren woher die einzelnen Daten kommen oder zusammenfügen
		 */
		result.setStationSourceId(
				primary.getStationSourceId() != 0 ? primary.getStationSourceId() : secondary.getStationSourceId());
		result.setDatasource(primary.getDatasource() != null ? primary.getDatasource() : secondary.getDatasource());
		result.setWeatherStationSource(primary.getWeatherStationSource() != null ? primary.getWeatherStationSource()
				: secondary.getWeatherStationSource());

		result.setRealtime(primary.isRealtime());
		result.setLastChangedBy("SYSTEM");
		result.setLastChangedTime(LocalDateTime.now());
		result.setVersion(Math.max(d1.getVersion(), d2.getVersion()));

		return result;
	}

	private static Double betterGlobalRadiation(Double g1, Double g2) {
		Double v1 = (g1 != null && g1 >= 0) ? g1 : null;
		Double v2 = (g2 != null && g2 >= 0) ? g2 : null;

		if (v1 == null)
			return v2;
		if (v2 == null)
			return v1;

		return v1 > v2 ? v1 : v2;
	}

	private static Double betterPrecision(Double d1, Double d2) {
		if (isInvalid(d1) && isValid(d2))
			return d2;
		if (isInvalid(d2) && isValid(d1))
			return d1;
		if (isValid(d1) && isValid(d2)) {
			int precision1 = getDecimalPrecision(d1);
			int precision2 = getDecimalPrecision(d2);
			return precision2 > precision1 ? d2 : d1;
		}
		return null;
	}

	private static int getDecimalPrecision(Double d) {
		String[] parts = d.toString().split("\\.");
		return (parts.length == 2) ? parts[1].length() : 0;
	}

	private static boolean isValid(Double d) {
		return d != null && d != 0.0;
	}

	private static boolean isInvalid(Double d) {
		return !isValid(d);
	}

	private static ImputationLogPojo prepareImputationLog(String field, Double value, Double valueA, Double valueB,
			WxdbWeatherData a, WxdbWeatherData b, String reason) {
		WxdbWeatherData chosen = (Objects.equals(value, valueA)) ? a : b;
		ImputationLogPojo log = new ImputationLogPojo();
		log.setWertName(field);
		log.setWert(value);
		log.setSourceStationId((int) chosen.getStationSourceId());
		log.setSourceName(chosen.getDatasource());
		log.setInformation(createLogReason(chosen.getStationSourceId()));
		log.setCreatedAt(LocalDateTime.now());
		log.setTriggeredBy("SYSTEM");
		return log;
	}

	private static String createLogReason(long stationId) {
		return "Imputation durch Deutschen Wetterdienst Wetterstationsid: " + stationId;
	}
}
