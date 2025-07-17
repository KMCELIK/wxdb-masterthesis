package de.wxdb.wxdb_masterthesis.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;
import de.wxdb.wxdb_masterthesis.schema.ImputationLogPojo;
import de.wxdb.wxdb_masterthesis.schema.ImputationSummary;

/**
 * Class to validate Weatherdata.
 * 
 * @author Kaan Mustafa Celik
 */
public class WeatherDataValidator {

	private static final Logger log = LoggerFactory.getLogger(WeatherDataValidator.class);

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

	public static boolean isNotImputable(WxdbWeatherData data) {
		return data.getWindDirection() == null && data.getTemperature() == null && data.getGlobalRadiation() == null
				&& data.getWindSpeed() == null;
	}

	public static boolean isSynopDaterange(WxdbWeatherData data) {
		// possible NullPointer
		if (data == null)
			return false;

		return data.getTime() != null && data.getTime().isAfter(LocalDateTime.now().minusHours(31));
	}

	public static List<WxdbWeatherData> deduplicateAndImproveValidInfluxDbDatasets(
			List<WxdbWeatherData> validDatasets) {
		Map<LocalDateTime, WxdbWeatherData> timeToBestEntry = new HashMap<>();

		for (WxdbWeatherData data : validDatasets) {
			LocalDateTime time = data.getTime();
			WxdbWeatherData existing = timeToBestEntry.get(time);

			if (existing == null) {
				timeToBestEntry.put(time, data);
			} else {
				WxdbWeatherData better = chooseBetterValidInfluxDbDataset(existing, data);
				timeToBestEntry.put(time, better);
			}
		}

		return new ArrayList<>(timeToBestEntry.values());
	}

	/**
	 * Logik zum priorisieren von InfluxDB Datensätze.
	 * 
	 * @param datasets
	 * @return
	 */
	public static List<WxdbWeatherData> deduplicateByTimestamp(List<WxdbWeatherData> datasets) {
		Map<LocalDateTime, WxdbWeatherData> timeToBestEntry = new HashMap<>();

		for (WxdbWeatherData data : datasets) {
			if (data == null || data.getTime() == null)
				continue;

			LocalDateTime time = data.getTime();
			WxdbWeatherData existing = timeToBestEntry.get(time);

			if (existing == null) {
				timeToBestEntry.put(time, data);
			} else {
				// Bevorzuge Influx-Daten
				boolean isExistingInflux = "InfluxDB".equalsIgnoreCase(existing.getDatasource());
				boolean isCurrentInflux = "InfluxDB".equalsIgnoreCase(data.getDatasource());

				if (!isExistingInflux && isCurrentInflux) {
					timeToBestEntry.put(time, data); // überschreiben durch InfluxDB
				}
				// Wenn beide gleichwertig oder existierender ist Influx, behalte bestehenden
			}
		}

		return new ArrayList<>(timeToBestEntry.values());
	}

	private static WxdbWeatherData chooseBetterValidInfluxDbDataset(WxdbWeatherData d1, WxdbWeatherData d2) {
		WxdbWeatherData betterDataset = new WxdbWeatherData();
		// Ziehe Realtime Data dem anderen vor, außer es sind beide Realtime oder beide
		// nicht realtime
		WxdbWeatherData primary = (d1.isRealtime() && !d2.isRealtime()) ? d1
				: (!d1.isRealtime() && d2.isRealtime()) ? d2 : d1;
		WxdbWeatherData secondary = (primary == d1) ? d2 : d1;

		betterDataset = primary;
		// bei validen InfluxDB-Datensätzen wird nur die GLobale Strahlung ausgebessert,
		// da die anderen Variablen identisch sind.
		if (primary.getGlobalRadiation() < secondary.getGlobalRadiation()) {
			betterDataset.setGlobalRadiation(secondary.getGlobalRadiation());
			betterDataset.setVersion(betterDataset.getVersion() + 1);
		}

		return betterDataset;
	}

	public static List<WxdbWeatherData> deduplicateAndCorrectInvalidData(List<WxdbWeatherData> invalidDatasets,
			List<WxdbWeatherData> notImputableDatasets, TreeSet<LocalDateTime> invalidTimestamps,
			List<WxdbWeatherData> dwdDatasets, Map<Long, Integer> stationIdAndDistance) {
		List<WxdbWeatherData> correctedDatasets = new ArrayList<WxdbWeatherData>();
		
		for (LocalDateTime invalidTimestamp : invalidTimestamps) {
			Optional<WxdbWeatherData> invalidOptional = invalidDatasets.stream()
					.filter(iD -> iD.getTime().equals(invalidTimestamp)).findFirst();
			List<WxdbWeatherData> dwdDatasetsDay = dwdDatasets.stream()
					.filter(dwd -> dwd.getTime().equals(invalidTimestamp))
					.sorted(getDwdQualityComparator(stationIdAndDistance)).toList();
			Optional<WxdbWeatherData> optionalDwd = dwdDatasetsDay.stream().findFirst();

			if (invalidOptional.isPresent()) {
				if (optionalDwd.isPresent()) {
					correctedDatasets.add(imputeInvalidDataset(invalidOptional.get(), optionalDwd.get()));

				} else {
					log.warn("There is no Imputation possible for Dataset: " + invalidOptional.get());
					continue;
				}
			} else {
				if (optionalDwd.isPresent()) {
					// in diesem Fall wird der gesammte DWD-Datensatz hinzugefügt. Echtzeitdaten und
					// wenig Distanz haben vorrang.
					correctedDatasets.add(optionalDwd.get());
				} else {
					log.warn("There is no Imputation possible for Dataset at time: " + invalidTimestamp);
					continue;
				}
			}
		}

		return correctedDatasets;
	}

	private static Comparator<WxdbWeatherData> getDwdQualityComparator(Map<Long, Integer> stationIdAndDistance) {
		return Comparator.comparing(WxdbWeatherData::isRealtime).reversed().thenComparing(d -> {
			Integer distance = stationIdAndDistance.get(d.getStationSourceId());
			return distance != null ? distance : Integer.MAX_VALUE;
		});
	}

	/**
	 * Imputation of missing values in the invalid dataset.
	 * 
	 * @param invalidDataset invalid Dataset
	 * @param validDataset   valid Dataset
	 * @return corrected Dataset
	 */
	private static WxdbWeatherData imputeInvalidDataset(WxdbWeatherData invalidDataset, WxdbWeatherData validDataset) {
		ImputationSummary imputationSummary = new ImputationSummary();
		List<ImputationLogPojo> logs = new ArrayList<ImputationLogPojo>();

		WxdbWeatherData result = new WxdbWeatherData();
		result.setTime(invalidDataset.getTime());
		result.setImputed(true);
		result.setDatasource(invalidDataset.getDatasource() + " / " + validDataset.getDatasource());
		result.setWeatherStationSource(invalidDataset.getWeatherStationSource() + " Imputation durch Wetterstation: "
				+ validDataset.getWeatherStationSource());
		result.setStationSourceId(validDataset.getStationSourceId());

		if (invalidDataset.getGlobalRadiation() == null) {

			result.setGlobalRadiation(null);
			logs.add(prepareImputationLog("Globale Strahlung", validDataset.getGlobalRadiation(),
					invalidDataset.getGlobalRadiation(), validDataset.getGlobalRadiation(), invalidDataset,
					validDataset));

		} else {
			result.setGlobalRadiation(invalidDataset.getGlobalRadiation());
		}

		if (invalidDataset.getWindDirection() == null || invalidDataset.getWindSpeed() == null) {
			result.setWindDirection(validDataset.getWindDirection());
			result.setWindSpeed(validDataset.getWindSpeed());

			logs.add(prepareImputationLog("Windrichtung", validDataset.getWindDirection(),
					invalidDataset.getWindDirection(), validDataset.getWindDirection(), invalidDataset, validDataset));
			logs.add(prepareImputationLog("Windgeschwindigkeit", validDataset.getWindSpeed(),
					invalidDataset.getWindSpeed(), validDataset.getWindSpeed(), invalidDataset, validDataset));

		} else {
			result.setWindDirection(invalidDataset.getWindDirection());
			result.setWindSpeed(invalidDataset.getWindSpeed());
		}

		if (invalidDataset.getTemperature() == null
				|| (invalidDataset.getTemperature() < -90.0 && invalidDataset.getTemperature() > 60.0)) {
			result.setTemperature(validDataset.getTemperature());

			logs.add(prepareImputationLog("Temperature", validDataset.getTemperature(), invalidDataset.getTemperature(),
					validDataset.getTemperature(), invalidDataset, validDataset));
		} else {
			result.setTemperature(invalidDataset.getTemperature());
		}

		imputationSummary.setLogs(logs);
		imputationSummary.setCreatedAt(LocalDateTime.now());
		imputationSummary.setTriggeredBy("SYSTEM");
		imputationSummary.setInformation(logs.size() + " Werte imputiert.");
		result.setVersion(invalidDataset.getVersion() + logs.size());
		result.setLastChangedTime(LocalDateTime.now());
		result.setZusammenfassung(imputationSummary);

		return result;
	}

	private static ImputationLogPojo prepareImputationLog(String field, Double value, Double valueA, Double valueB,
			WxdbWeatherData a, WxdbWeatherData b) {
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
