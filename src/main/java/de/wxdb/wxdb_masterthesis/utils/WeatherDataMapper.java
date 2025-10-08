package de.wxdb.wxdb_masterthesis.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.wxdb.wxdb_masterthesis.dto.CsvWeatherDataRaw;
import de.wxdb.wxdb_masterthesis.dto.DwdHourlyWeatherData;
import de.wxdb.wxdb_masterthesis.dto.DwdSynopWeatherData;
import de.wxdb.wxdb_masterthesis.dto.WeatherHistoricalData;
import de.wxdb.wxdb_masterthesis.dto.WeatherRealtimeData;
import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;
import de.wxdb.wxdb_masterthesis.schema.WxdbWeatherDataCsvPojo;

/**
 * Mapper class to map the different weather Data types to
 * {@link WxdbWeatherData} or to transform {@link WxdbWeatherData} to other
 * Weatherdata types.
 * 
 * All mappings in this class assume source = "InfluxDB" and
 * weatherStationSource = "FH-Dortmund".
 * 
 * @author Kaan Mustafa Celik
 */
public class WeatherDataMapper {
	private static final String DEFAULT_INFLUXDB_SOURCE = "InfluxDB";
	private static final String MANUAL_CSV_IMPORT = "CSV-Import";
	private static final String DEFAULT_INFLUXDB_STATION = "FH-Dortmund";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	/**
	 * Method to map from {@link WeatherRealtimeData}.
	 * 
	 * @param realtimeData real time data.
	 * @return WxdbWeatherData.
	 */
	public static WxdbWeatherData fromRealtimeData(WeatherRealtimeData realtimeData) {
		WxdbWeatherData data = new WxdbWeatherData();
		data.setTime(realtimeData.getTime());
		data.setGlobalRadiation(realtimeData.getGlobRT());
		data.setTemperature(realtimeData.getTempRT());
		data.setWindDirection(realtimeData.getWindRT());
		data.setWindSpeed(realtimeData.getWindgeschwindigRT());

		data.setDatasource(DEFAULT_INFLUXDB_SOURCE);
		data.setWeatherStationSource(DEFAULT_INFLUXDB_STATION);
		data.setLastChangedBy("SYSTEM");
		data.setLastChangedTime(LocalDateTime.now());
		data.setRealtime(true);
		data.setVersion(0);

		return data;
	}
	
	/**
	 * Method to map from {@link WeatherRealtimeData}.
	 * 
	 * @param realtimeData real time data.
	 * @return WxdbWeatherData.
	 */
	public static WxdbWeatherDataCsvPojo fromCsvWeatherData(CsvWeatherDataRaw csvWeatherData, String weatherStationName) {
		WxdbWeatherDataCsvPojo data = new WxdbWeatherDataCsvPojo();
		
	    // Zeitstempel setzen (Datum + Uhrzeit)
	    try {
	        LocalDate date = LocalDate.parse(csvWeatherData.getDatum(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	        LocalTime time = LocalTime.parse(csvWeatherData.getZeit(), DateTimeFormatter.ofPattern("HH:mm:ss"));
	        data.setTime(LocalDateTime.of(date, time));
	    } catch (Exception e) {
	        throw new RuntimeException("Fehler beim Parsen von Datum/Zeit: " + csvWeatherData.getDatum() + " " + csvWeatherData.getZeit(), e);
	    }

	    // Wetterdaten setzen (Komma → Punkt)
	    data.setTemperature(parse(csvWeatherData.getTemperatur()));
	    data.setWindSpeed(parse(csvWeatherData.getWindSpeed()));
	    data.setWindDirection(parse(csvWeatherData.getWindDirection()));
	    data.setGlobalRadiation(parse(csvWeatherData.getGlobalstrahlung()));
		
		data.setDatasource(MANUAL_CSV_IMPORT);
		data.setWeatherStationSource(weatherStationName);
		data.setLastChangedBy("MANUAL-IMPORT");
		data.setLastChangedTime(LocalDateTime.now());
		data.setRealtime(true);
		data.setVersion(0);

		return data;
	}

	/**
	 * Method to map from {@link WeatherHistoricalData}.
	 * 
	 * @param historical historical data sets.
	 * @return WxdbWeatherData.
	 */
	public static WxdbWeatherData fromHistoricalData(WeatherHistoricalData historicalData) {
		WxdbWeatherData data = new WxdbWeatherData();
		data.setTime(historicalData.getTime());
		data.setGlobalRadiation(historicalData.getGlobAVG());
		data.setTemperature(historicalData.getTempAVG());
		data.setWindDirection(historicalData.getWindAVG());
		data.setWindSpeed(historicalData.getWindgeschwindigAVG());

		data.setDatasource(DEFAULT_INFLUXDB_SOURCE);
		data.setWeatherStationSource(DEFAULT_INFLUXDB_STATION);
		data.setLastChangedBy("SYSTEM");
		data.setLastChangedTime(LocalDateTime.now());
		data.setVersion(0);
		data.setRealtime(false);

		return data;
	}

	public static List<WxdbWeatherData> mapSynopDataList(List<DwdSynopWeatherData> dwdList) {
		return dwdList.stream().map(WeatherDataMapper::mapSingle).collect(Collectors.toList());
	}
	
	public static List<WxdbWeatherDataCsvPojo> mapCsvWeatherDataList(List<CsvWeatherDataRaw> csvWeatherList, String weatherStationName) {
		return csvWeatherList.stream().map(cwd -> WeatherDataMapper.fromCsvWeatherData(cwd, weatherStationName)).collect(Collectors.toList());
	}

	public static WxdbWeatherData mapSingle(DwdSynopWeatherData dwd) {
		WxdbWeatherData wxdb = new WxdbWeatherData();
		wxdb.setTime(dwd.getTimestamp() != null ? dwd.getTimestamp().toLocalDateTime() : null);
		wxdb.setTemperature(dwd.getTemperature());

		// Windrichtung, eigentlich sollen nur die 10 minuten intervalle hier erhalten werden.
		Integer windDirection = dwd.getWind_direction_10();
		if (windDirection == null) {
		    windDirection = dwd.getWind_direction_30();
		}
		if (windDirection == null) {
		    windDirection = dwd.getWind_direction_60();
		}
		wxdb.setWindDirection(windDirection != null ? windDirection.doubleValue() : null);

		// Windgeschwindigkeit
		Double windSpeed = dwd.getWind_speed_10();
		if (windSpeed == null) {
		    windSpeed = dwd.getWind_speed_30();
		}
		if (windSpeed == null) {
		    windSpeed = dwd.getWind_speed_60();
		}
		wxdb.setWindSpeed(windSpeed != null ? windSpeed.doubleValue() : null);

		// Globalstrahlung (kWh/m² pro Intervall)
		Double globalRadiation = dwd.getSolar_10();
		if (globalRadiation != null && Double.isFinite(globalRadiation)) {
		    // unboxing ist sicher, weil globalRadiation != null
		    globalRadiation = globalRadiation * 6000d; // 10 min -> Faktor 6000
		} else {
		    // Fallback auf 60-min-Wert (falls vorhanden)
		    Double solar60 = dwd.getSolar_60();
		    if (solar60 != null && Double.isFinite(solar60)) {
		        globalRadiation = solar60 * 1000d; // 60 min -> Faktor 1000
		    } else {
		        globalRadiation = null; // kein Wert verfügbar
		    }
		}
		
		wxdb.setGlobalRadiation(globalRadiation);

		wxdb.setDatasource("DWD");
		wxdb.setWeatherStationSource("SYNOP-Brightsky-" + dwd.getSource_id());
		wxdb.setStationSourceId(dwd.getSource_id());

		wxdb.setLastChangedBy("SYSTEM");
		wxdb.setLastChangedTime(LocalDateTime.now());
		wxdb.setVersion(1);
		wxdb.setRealtime(true);

		return wxdb;
	}

	public static List<WxdbWeatherData> mapDwdWeatherDataList(List<DwdHourlyWeatherData> dwdDataList) {
		return dwdDataList.stream().map(WeatherDataMapper::map).collect(Collectors.toList());
	}
	
	 /**
     * Mappt eine Liste von {@link WxdbWeatherData} nach
     * {@link WxdbWeatherDataCsvPojo}.
     *
     * @param source Liste von WxdbWeatherData
     * @return Liste von WxdbWeatherDataCsvPojo (nie null)
     */
    public static List<WxdbWeatherDataCsvPojo> mapToCsvPojoList(List<WxdbWeatherData> source) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream()
                     .filter(Objects::nonNull)
                     .map(WeatherDataMapper::mapToCsvPojo)
                     .collect(Collectors.toList());
    }

    /**
     * Einzelmapping von WxdbWeatherData -> WxdbWeatherDataCsvPojo
     */
    private static WxdbWeatherDataCsvPojo mapToCsvPojo(WxdbWeatherData src) {
        if (src == null) return null;

        WxdbWeatherDataCsvPojo dst = new WxdbWeatherDataCsvPojo();

        // Weather related
        dst.setTime(src.getTime());
        dst.setTemperature(src.getTemperature());
        dst.setWindDirection(src.getWindDirection());
        dst.setWindSpeed(src.getWindSpeed());
        dst.setGlobalRadiation(src.getGlobalRadiation());

        // Data source related (wenn source null, benutze Default)
        dst.setDatasource(src.getDatasource() != null ? src.getDatasource() : DEFAULT_INFLUXDB_SOURCE);
        dst.setWeatherStationSource(src.getWeatherStationSource() != null ? src.getWeatherStationSource() : DEFAULT_INFLUXDB_STATION);
        dst.setStationSourceId(src.getStationSourceId());
        dst.setRealtime(src.isRealtime());

        // POJO meta (behalte vorhandene Werte, sonst Default)
        dst.setLastChangedBy(src.getLastChangedBy() != null ? src.getLastChangedBy() : "SYSTEM");
        dst.setLastChangedTime(src.getLastChangedTime() != null ? src.getLastChangedTime() : LocalDateTime.now());
        dst.setVersion(src.getVersion());

        // Imputation
        dst.setImputed(src.isImputed());
        dst.setZusammenfassung(src.getZusammenfassung());

        // ID bewusst nicht gesetzt (DB/insert-Operation soll ID vergeben)
        return dst;
    }

	public static WxdbWeatherData map(DwdHourlyWeatherData dwdData) {
		WxdbWeatherData wxdb = new WxdbWeatherData();

		// Zeitkonvertierung
		wxdb.setTime(LocalDateTime.parse(dwdData.getTimestamp(), formatter));

		wxdb.setStationSourceId(dwdData.getSource_id());
		wxdb.setTemperature(dwdData.getTemperature());
		wxdb.setWindDirection(dwdData.getWind_direction() != null ? dwdData.getWind_direction().doubleValue() : null);
		wxdb.setWindSpeed(dwdData.getWind_speed());
		
		// Globale Strahlung in InfluxDB W/m² umwandeln.
		Double solarDwd = dwdData.getSolar();
		if (solarDwd != null && Double.isFinite(solarDwd)) {
			wxdb.setGlobalRadiation(solarDwd*1000d);
		}  else {
			wxdb.setGlobalRadiation(null);
		}

		wxdb.setDatasource("DWD");
		wxdb.setWeatherStationSource("DWD-" + dwdData.getSource_id());
		wxdb.setLastChangedBy("SYSTEM");
		wxdb.setStationSourceId(dwdData.getSource_id());
		wxdb.setLastChangedTime(LocalDateTime.now());
		wxdb.setRealtime(false);

		return wxdb;
	}
	
	// Hilfsmethode für Komma-Dezimalzahlen
	private static Double parse(String input) {
	    if (input == null || input.isBlank()) return null;
	    try {
	        return Double.parseDouble(input.replace(",", "."));
	    } catch (NumberFormatException e) {
	        return null;
	    }
	}
}
