package de.wxdb.wxdb_masterthesis.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.wxdb.wxdb_masterthesis.dto.WxdbApiResponse;
import de.wxdb.wxdb_masterthesis.process.WeatherImportProcess;

/**
 * Ausführungsklasse des {@link WxdbApi} interfaces.
 * 
 * @author Kaan Mustafa Celik
 */
@RestController
public class WxdbApiDelegate implements WxdbApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(WxdbApiDelegate.class);

	@Autowired
	private WeatherImportProcess importProcess;

	@Override
	public WxdbApiResponse importCsv(@RequestPart("csvFile") MultipartFile csvFile,
			@RequestPart("weatherStation") String weatherStation) {
		LOGGER.info("Start manual csv import for weather station: " + weatherStation);
		WxdbApiResponse response = null;

		try {
			response = importProcess.importCsv(csvFile, weatherStation);
		} catch (RuntimeException e) {
			LOGGER.error("Error while triggering csv import", e);
			response = new WxdbApiResponse(e, "ERROR", "Error while triggering initial import.");
		}

		return response;
	}

	@Override
	public WxdbApiResponse triggerInitialImport(String startDate) {
		LocalDate beginDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
		LocalDate endDate = LocalDate.now();

		if (startDate != null && !startDate.isEmpty()) {
			try {
	            // Fallback: Parser mit explizitem Locale + symbolischen Ziffern
	            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
	                    .parseLenient()
	                    .appendPattern("dd.MM.yyyy")
	                    .toFormatter(Locale.GERMANY);

	            beginDate = LocalDate.parse(startDate, formatter);
			} catch (DateTimeParseException e) {
				// Optional: Logging, falls ungültiges Format übergeben wurde
				LOGGER.warn("Invalid date format for startDate: {}. Expected format: dd.MM.yyyy", startDate);
			}
		}

		LOGGER.info("Date Range for Import is set to {} till {}", beginDate, endDate);
		WxdbApiResponse response = null;
		if (beginDate.isEqual(endDate) || beginDate.isAfter(endDate)) {
			response = new WxdbApiResponse(null, "ERROR",
					"startDate is equals or after current Date, please choose a startDate which is before the current date.");
		} else {

			LOGGER.debug("Start initial import from date {} till {}", beginDate, endDate);
			try {
				importProcess.importWeatherData(beginDate, endDate);
				response = new WxdbApiResponse();
			} catch (RuntimeException e) {
				LOGGER.error("Error while triggering initial import", e);
				response = new WxdbApiResponse(e, "ERROR", "Error while triggering initial import.");
			}
		}

		return response;
	}
	
	@Override
	public WxdbApiResponse triggerDailyImport() {
		LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
		LocalDate endDate = LocalDate.now();
		LOGGER.debug("Start initial import from date {} till {}", startDate, endDate);

		WxdbApiResponse response = null;

		try {
			importProcess.importRealtimeWeatherData();
			response = new WxdbApiResponse();
		} catch (RuntimeException e) {
			LOGGER.error("Error while triggering initial import", e);
			response = new WxdbApiResponse(e, "ERROR", "Error while triggering initial import.");
		}

		return response;
	}
}
