package de.wxdb.wxdb_masterthesis.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import de.wxdb.wxdb_masterthesis.db.ImputationSummaryRepository;
import de.wxdb.wxdb_masterthesis.dto.BrightskyApiSourceResponse;
import de.wxdb.wxdb_masterthesis.dto.BrightskySynopResponse;
import de.wxdb.wxdb_masterthesis.dto.CsvWeatherDataRaw;
import de.wxdb.wxdb_masterthesis.dto.DwdSourceData;
import de.wxdb.wxdb_masterthesis.dto.WxdbApiResponse;
import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;
import de.wxdb.wxdb_masterthesis.schema.ImputationLogPojo;
import de.wxdb.wxdb_masterthesis.schema.ImputationSummary;
import de.wxdb.wxdb_masterthesis.schema.NotificationPojo;
import de.wxdb.wxdb_masterthesis.schema.Processlog;
import de.wxdb.wxdb_masterthesis.service.BrightskyApiService;
import de.wxdb.wxdb_masterthesis.service.InfluxDbReadWeatherDataService;
import de.wxdb.wxdb_masterthesis.service.InsertWxdbService;
import de.wxdb.wxdb_masterthesis.utils.FluxQueryTemplate;
import de.wxdb.wxdb_masterthesis.utils.LocalDateTimeRange;
import de.wxdb.wxdb_masterthesis.utils.WeatherDataMapper;
import de.wxdb.wxdb_masterthesis.utils.WeatherDataValidator;

/**
 * Diese Klasse beschreibt die Ausführung aller Importierungsprozesse.
 * 
 * @author Kaan Mustafa Celik
 */
@Component
public class WeatherImportProcess {

	private static final Logger log = LoggerFactory.getLogger(WeatherImportProcess.class);

	@Autowired
	private InfluxDbReadWeatherDataService weatherDataService;

	@Autowired
	private BrightskyApiService brightskyApiService;

	@Autowired
	private InsertWxdbService insertWxdbService;

	@Autowired
	private ImputationSummaryRepository imputationSumRepo;

	/**
	 * Gesammter Importierungsprozess von Wxdb-Wetterdaten. - Erhalt von Daten aus
	 * der InfluxDB - Erhalt von BrightskyAPI Daten - Validierung von erhaltenen
	 * Daten, Prüfung auf Fehler. - Imputation von fehlerhaften oder fehlenden Daten
	 * - Integration in die Wxdb Datenbank
	 * 
	 * @param startDate startdatum
	 * @param endDate   enddatum für den ausgewählten Zeitraum
	 */
	public void importWeatherData(LocalDate startDate, LocalDate endDate) {
		// 1. Prüfen ob Zeitraum gültig ist.
		if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
			log.warn("Ungültiger Zeitraum für den Import.");
			return;
		}
		// Datenbank logeintrag
		Processlog processLog = insertWxdbService.startProcessLog("IMPORT-WeatherData", LocalDateTime.now());

		// initialisierung der Listen und Maps
		List<WxdbWeatherData> validData = new ArrayList<WxdbWeatherData>();
		List<WxdbWeatherData> invalidData = new ArrayList<WxdbWeatherData>();
		List<WxdbWeatherData> notImputableData = new ArrayList<WxdbWeatherData>();
		List<WxdbWeatherData> dwdDatasets = new ArrayList<WxdbWeatherData>();

		Map<Long, Integer> stationIdToDistance = new HashMap<Long, Integer>();
		Map<LocalDateTime, WxdbWeatherData> correctedDataMap = new HashMap<>();

		// 1. Realtime Daten (sofern Zeitraum gültig) - InfluxDB
		if (!endDate.isBefore(LocalDate.now())) {
			log.info("Erhalte Echtzeitdaten aus der InfluxDB-Datenbank");
			validData.addAll(weatherDataService.retrieveRTWeatherData(startDate, FluxQueryTemplate.REALTIME_10M)
					.stream().map(WeatherDataMapper::fromRealtimeData).toList());
			log.debug("Echtzeitdaten wurden extrahiert - Menge der extrahierten Datensätze: {}", validData.size());

			validData.addAll(weatherDataService.retrieveRTWeatherData(startDate, FluxQueryTemplate.REALTIME_1H).stream()
					.map(WeatherDataMapper::fromRealtimeData).toList());
			log.debug("Echtzeitdaten wurden extrahiert - Menge aller Echtzeitdatensätze: {}", validData.size());
		}

		// 2. Historische Daten - InfluxDB
		log.info("Erhalte historische Daten aus der InfluxDB-Datenbank");
		validData.addAll(
				weatherDataService.retrieveHistoricalWeatherData(startDate, endDate, FluxQueryTemplate.HISTORICAL_10M)
						.stream().map(WeatherDataMapper::fromHistoricalData).toList());
		validData.addAll(
				weatherDataService.retrieveHistoricalWeatherData(startDate, endDate, FluxQueryTemplate.HISTORICAL_1H)
						.stream().map(WeatherDataMapper::fromHistoricalData).toList());
		log.info("Alle Daten wurden erfolgreich extrahiert - Menge aller Datensätze: {}", validData.size());

		// 3. unvollständige Datensätze herausfiltern
		log.info("Starte Ausfilterung von nicht validen Datensätzen");
		removeInvalidDatasets(validData, invalidData, notImputableData, correctedDataMap);
		log.info("Filterung abgeschlossen - Menge der vollkommenen Datensätze: {}", validData.size());

		log.debug("Schließe duplizierte Datensätze aus den validen InfluxDB-Datensätzen aus.");
		// Fall von duplizierten InfluxDB-Daten existiert, da wir erst einmal alles
		// ziehen 1h/10min und historisch und Echtzeit
		validData = WeatherDataValidator.deduplicateAndImproveValidInfluxDbDatasets(validData);
		log.info(
				"Ausschließen von duplizierten InfluxDB-Datensätzen abgeschlossen - Menge der vollkommenen Datensätze: {}",
				validData.size());
		// Analyse der fehlerhaften Datensätze falls vorhanden - InfluxDB
		if (!invalidData.isEmpty() || !notImputableData.isEmpty()) {
			log.info("Es existieren noch {} ungültige Datensätze. Versuche externe Quellen (Brightsky-DWD)",
					invalidData.size() + notImputableData.size());

			// Bestimmen der Zeiträume für die fehlerhaften Zeiträume falls es mehrere
			// Zeitraum lücken gibt.
			Set<LocalDateTime> invalidTimestamps = correctedDataMap.keySet();

			// wenn es Lücken gibt welche zu lang sind z.B. 30 Stunden, dann werden
			// verteilte Zeiträume erstellt (um die Schnittstelle nicht zu sehr auszulasten)
			List<LocalDateTimeRange> invalidRanges = LocalDateTimeRange
					.groupToBroadRanges(new ArrayList<>(invalidTimestamps), 30);

			// Filterung der 100 nahsten Wetterstationen in einem Umkreis von 50km
			List<DwdSourceData> stations = brightskyApiService.getDwdStations(null, null);
			stationIdToDistance = filterNearestSources(stations, 100).stream()
					.collect(Collectors.toMap(DwdSourceData::getId, DwdSourceData::getDistance));

			for (LocalDateTimeRange range : invalidRanges) {
				boolean endsWithinLast31h = range.getEndDate().isAfter(LocalDateTime.now().minusHours(31));

				if (endsWithinLast31h) {
					// In diesem Fall ist der Zeitraum ganz oder teilweise innerhalb der letzten 31
					// Stunden - 10min Datensätze
					BrightskySynopResponse synop = brightskyApiService
							.getDwdData10MinutesInterval(new ArrayList<>(stationIdToDistance.keySet()));
					dwdDatasets.addAll(extractValidSynopWeatherData(synop));
				}

				BrightskyApiSourceResponse hourly = brightskyApiService.getDwdWeatherDataHourlyInterval(
						range.getStartDate().toLocalDate(), range.getEndDate().toLocalDate(),
						new ArrayList<>(stationIdToDistance.keySet()));
				// erhalte DWD-Stundendatensätze, filtere jedoch direkt die fehlerhaften
				// Datensätze raus.
				dwdDatasets.addAll(WeatherDataMapper.mapDwdWeatherDataList(hourly.getWeather()).stream()
						.filter(vd -> WeatherDataValidator.isValid(vd)).collect(Collectors.toList()));
			}
		}

		log.debug("Es existieren {} komplett fehlerhafte Datensätze.", notImputableData.size());
		// Bereinigen und verbessern der fehlerhaften Datensätze
		validData.addAll(WeatherDataValidator.deduplicateAndCorrectInvalidData(invalidData, notImputableData,
				new TreeSet<LocalDateTime>(correctedDataMap.keySet()), dwdDatasets, stationIdToDistance));
		validData = WeatherDataValidator.deduplicateByTimestamp(validData);

		// sortiere die Datensätze
		validData.sort(Comparator.comparing(WxdbWeatherData::getTime));

		// filtere Duplikate welche sich schon auf der Datenbank befinden.
		List<NotificationPojo> notifications = insertWxdbService.filterDuplicates(validData, startDate, endDate);

		// zusammenfügen der Imputationslogs
		List<ImputationSummary> summaries = validData.stream().map(WxdbWeatherData::getZusammenfassung)
				.filter(Objects::nonNull).collect(Collectors.toList());
		for (ImputationSummary summary : summaries) {
			for (ImputationLogPojo log : summary.getLogs()) {
				log.setZusammenfassung(summary);
			}
		}
		try {
			// Insert Transaktion für die Datensätze
			imputationSumRepo.saveAll(summaries);
			insertWxdbService.insertWeatherData(validData);
			insertWxdbService.completeProcessLog(processLog, true,
					validData.size() + " Datensätze wurden erfolgreich importiert.");
			insertWxdbService.insertNotifications(notifications);
			log.info("insert completed - datasets: {}", validData.toString());
		} catch (RuntimeException e) {
			String shortMessage = e.getMessage();
			if (shortMessage.length() > 1000) {
				shortMessage = shortMessage.substring(0, 1000);
			}

			insertWxdbService.completeProcessLog(processLog, false, e.getMessage() + shortMessage);
			log.error("Wxdb Insert failed due to: ", e);
		}

		log.info("Import abgeschlossen. {} Datensätze nach Kompensation generiert.", validData.size());
	}

	/**
	 * Importierungsprozess ausschließlich für Echtzeitdaten.
	 */
	public void importRealtimeWeatherData() {
		// Datenbank logeintrag
		Processlog processLog = insertWxdbService.startProcessLog("IMPORT-Realtime-Weatherdata", LocalDateTime.now());
		LocalDateTime startDate = LocalDateTime.now().minusHours(24); // get the data of 1 day before, not 2 days before.
		// initialisierung der Listen und Maps
		List<WxdbWeatherData> validData = new ArrayList<WxdbWeatherData>();
		List<WxdbWeatherData> invalidData = new ArrayList<WxdbWeatherData>();
		List<WxdbWeatherData> notImputableData = new ArrayList<WxdbWeatherData>();
		List<WxdbWeatherData> dwdDatasets = new ArrayList<WxdbWeatherData>();

		Map<Long, Integer> stationIdToDistance = new HashMap<Long, Integer>();
		Map<LocalDateTime, WxdbWeatherData> correctedDataMap = new HashMap<>();

		// 1. Realtime Daten (sofern Zeitraum gültig) - InfluxDB
		log.info("Erhalte Echtzeitdaten aus der InfluxDB-Datenbank");
		validData.addAll(
				weatherDataService.retrieveRTWeatherData(startDate.toLocalDate(), FluxQueryTemplate.REALTIME_10M)
						.stream().map(WeatherDataMapper::fromRealtimeData).toList());
		log.debug("Echtzeitdaten wurden extrahiert - Menge der extrahierten Datensätze: {}", validData.size());

		// falls keine Echtzeitdaten vorhanden sind ziehe historische daten aus der InfluxDB.
		if (validData.isEmpty()) {
			validData.addAll(weatherDataService.retrieveHistoricalWeatherData(startDate.toLocalDate(), LocalDate.now(), 
					FluxQueryTemplate.HISTORICAL_10M).stream().map(WeatherDataMapper::fromHistoricalData).toList());
		}
		
		// 2. unvollständige Datensätze herausfiltern
		log.info("Starte Ausfilterung von nicht validen Datensätzen");
		removeInvalidDatasets(validData, invalidData, notImputableData, correctedDataMap);
		log.info("Filterung abgeschlossen - Menge der vollkommenen Datensätze: {}", validData.size());

		// Analyse der fehlerhaften Datensätze falls vorhanden - InfluxDB - falls keine Daten bezogen wurden ebenfalls ziehen
		if (!invalidData.isEmpty() || !notImputableData.isEmpty() || validData.isEmpty()) {
			log.info("Es existieren noch {} ungültige Datensätze. Versuche externe Quellen (Brightsky-DWD)",
					invalidData.size() + notImputableData.size());
			// Filterung der 100 nahsten Wetterstationen in einem Umkreis von 50km
			List<DwdSourceData> stations = brightskyApiService.getDwdStations(null, null);
			stationIdToDistance = filterNearestSources(stations, 100).stream()
					.collect(Collectors.toMap(DwdSourceData::getId, DwdSourceData::getDistance));

			BrightskySynopResponse synop = brightskyApiService
					.getDwdData10MinutesInterval(new ArrayList<>(stationIdToDistance.keySet()));

			// keine InfluxDB Daten, somit keine Zeitstempel
			if (validData.isEmpty()) {
				validData.addAll(extractValidSynopWeatherData(synop));
			} else {
				dwdDatasets.addAll(extractValidSynopWeatherData(synop));
			}
		}

		log.debug("Es existieren {} komplett fehlerhafte Datensätze.", notImputableData.size());
		// Bereinigen und verbessern der fehlerhaften Datensätze
		validData.addAll(WeatherDataValidator.deduplicateAndCorrectInvalidData(invalidData, notImputableData,
				new TreeSet<LocalDateTime>(correctedDataMap.keySet()), dwdDatasets, stationIdToDistance));
		validData = WeatherDataValidator.deduplicateByTimestamp(validData);

		// sortiere die Datensätze
		validData.sort(Comparator.comparing(WxdbWeatherData::getTime));

		// filtere Duplikate welche sich schon auf der Datenbank befinden.
		List<NotificationPojo> notifications = insertWxdbService.filterDuplicates(validData, startDate.toLocalDate(),
				LocalDate.now());

		// zusammenfügen der Imputationslogs
		List<ImputationSummary> summaries = validData.stream().map(WxdbWeatherData::getZusammenfassung)
				.filter(Objects::nonNull).collect(Collectors.toList());
		for (ImputationSummary summary : summaries) {
			for (ImputationLogPojo log : summary.getLogs()) {
				log.setZusammenfassung(summary);
			}
		}

		try {
			// Insert Transaktion für die Datensätze
			imputationSumRepo.saveAll(summaries);
			insertWxdbService.insertWeatherData(validData);
			insertWxdbService.completeProcessLog(processLog, true,
					validData.size() + " Datensätze wurden erfolgreich importiert.");
			insertWxdbService.insertNotifications(notifications);
			log.info("insert completed - datasets: {}", validData.toString());
		} catch (RuntimeException e) {
			String shortMessage = e.getMessage();
			if (shortMessage.length() > 1000) {
				shortMessage = shortMessage.substring(0, 1000);
			}

			insertWxdbService.completeProcessLog(processLog, false, e.getMessage() + shortMessage);
			log.error("Wxdb Insert failed due to: ", e);
		}

		log.info("Import abgeschlossen. {} Datensätze nach Kompensation generiert.", validData.size());

	}

	public WxdbApiResponse importCsv(MultipartFile csvFile, String weatherStation) {
		WxdbApiResponse response = new WxdbApiResponse();
		log.info("Reading CSV-File from " + weatherStation);
		
		// 1. Lese die CSV-Datei
		Processlog processLog = insertWxdbService.startProcessLog("MANUAL-CSV-IMPORT", LocalDateTime.now());
		List<CsvWeatherDataRaw> csvDatasets = new ArrayList<CsvWeatherDataRaw>();
		
		try {
			csvDatasets = readCsv(csvFile);
		} catch (RuntimeException e) {
			log.error("Error occured in reading the csv-file", e);
			String shortMessage = e.getMessage();
			if (shortMessage.length() > 1000) {
				shortMessage = shortMessage.substring(0, 1000);
			}

			insertWxdbService.completeProcessLog(processLog, false, e.getMessage() + shortMessage);
			response = new WxdbApiResponse(e, "Error occured while reading the csv-file", shortMessage);
			return response;
		}
		log.info("Finished Mapping CSV File to csv-datasets");

		// 2. Mappe die Daten in eine Liste von WxdbWetterdaten
		List<WxdbWeatherData> wxdbCsvWeatherData = new ArrayList<>();
		try {
			wxdbCsvWeatherData = WeatherDataMapper.mapCsvWeatherDataList(csvDatasets, weatherStation);
		} catch (RuntimeException e) {
			log.error("Error occured in mapping the csv-file", e);
			String shortMessage = e.getMessage();
			if (shortMessage.length() > 1000) {
				shortMessage = shortMessage.substring(0, 1000);
			}

			insertWxdbService.completeProcessLog(processLog, false, e.getMessage() + shortMessage);
			response = new WxdbApiResponse(e, "Error occured while mapping the csv-file", shortMessage);
			return response;
		}
		log.info("Finished mapping csv file to wxdb-weatherdatasets");
		
		// filtere fehlerhafte Datensätze aus
		wxdbCsvWeatherData = wxdbCsvWeatherData.stream()
				.filter(wd -> (WeatherDataValidator.isValid(wd) && WeatherDataValidator.isAllDataValid(wd)))
				.collect(Collectors.toList());
		log.info("Filtered all invalid datasets out, start inserting weatherData");
		
		// 3. Inserte die Wxdb Wetterdaten in unsere Datenbank
		try {
			insertWxdbService.insertWeatherData(wxdbCsvWeatherData);
			insertWxdbService.completeProcessLog(processLog, true,
					wxdbCsvWeatherData.size() + " Datensätze wurden erfolgreich importiert.");
		} catch (RuntimeException e) {
			String shortMessage = e.getMessage();
			if (shortMessage.length() > 1000) {
				shortMessage = shortMessage.substring(0, 1000);
			}

			insertWxdbService.completeProcessLog(processLog, false, e.getMessage() + shortMessage);
			response = new WxdbApiResponse(e, "Error occured while inserting the csv-import", shortMessage);
			return response;
		}
		
		return response;
	}

	/**
	 * Filters nearest Locations limited by count.
	 * 
	 * @param locations       Weatherstations
	 * @param limitationCount count to limit the set Array.
	 * @return List of Weatherstations - {@link DwdSourceData}.
	 */
	private List<DwdSourceData> filterNearestSources(List<DwdSourceData> locations, int limitationCount) {
		List<DwdSourceData> nearestLocations = locations.stream().filter(source -> source.getDistance() != null)
				.sorted(Comparator.comparing(DwdSourceData::getDistance)).limit(limitationCount)
				.collect(Collectors.toList());
		return nearestLocations;
	}

	private void removeInvalidDatasets(List<WxdbWeatherData> validData, List<WxdbWeatherData> invalidData,
			List<WxdbWeatherData> notImputableData, Map<LocalDateTime, WxdbWeatherData> correctedDataMap) {
		validData.removeIf(wd -> {
			if (wd == null || !WeatherDataValidator.isValid(wd)) {
				if (wd != null && wd.getTime() != null) {
					// füge den Zeitstempel in die correctedDataMap.
					correctedDataMap.putIfAbsent(wd.getTime(), null);
					if (WeatherDataValidator.isNotImputable(wd)) {
						// Liste der Datensätze die komplett überarbeitet werden müssen
						notImputableData.add(wd);
					} else {
						// Liste der Datensätze die fehlerhaft sind und Imputiert werden müssen
						invalidData.add(wd);
					}
					log.debug("Fehlerhafter Datensatz wurde ausgefiltert: {}", wd.toString());
				}
				return true;
			}
			return false;
		});
	}

	private List<WxdbWeatherData> extractValidSynopWeatherData(BrightskySynopResponse synop) {
		return WeatherDataMapper.mapSynopDataList(synop.getWeather()).stream()
				.filter(synopData -> WeatherDataValidator.isValid(synopData)
						&& WeatherDataValidator.isSynopDaterange(synopData))
				.collect(Collectors.toList());
	}

	private List<CsvWeatherDataRaw> readCsv(MultipartFile csvFile) {
	    CsvMapper mapper = CsvMapper.builder()
	        .enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.TRIM_SPACES)
	        .build();

	    // Unbekannte Eigenschaften tolerieren (zusätzlich zur POJO-Annotation)
	    mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	    CsvSchema schema = CsvSchema.emptySchema()
	        .withHeader()
	        .withColumnSeparator(';');

	    try (InputStream in = csvFile.getInputStream();
	         Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
	         BufferedReader br = new BufferedReader(reader)) {

	        // Header zu Debug-Zwecken einmal loggen
	        br.mark(8192);
	        String header = br.readLine();
	        log.info("CSV-Header: {}", header);
	        br.reset();

	        MappingIterator<CsvWeatherDataRaw> it = mapper
	            .readerFor(CsvWeatherDataRaw.class)
	            .with(schema)
	            .readValues(br);

	        return it.readAll();

	    } catch (Exception e) { // nicht nur IOException
	    	log.error("Error occured while reading CSV: ", e);
	        throw new RuntimeException("Fehler beim Lesen/Deserialisieren der CSV-Datei", e);
	    }
	}
}
