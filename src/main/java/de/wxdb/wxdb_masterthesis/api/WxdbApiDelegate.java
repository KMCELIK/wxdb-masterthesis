package de.wxdb.wxdb_masterthesis.api;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import de.wxdb.wxdb_masterthesis.dto.WxdbApiResponse;
import de.wxdb.wxdb_masterthesis.process.WeatherImportProcess;

@Component
public class WxdbApiDelegate implements WxdbApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(WxdbApiDelegate.class);

	@Autowired
	private WeatherImportProcess importProcess;

	@Override
	public WxdbApiResponse importCsv(MultipartFile csv, String weatherStation) {
		LOGGER.debug("Start manual csv import");
		WxdbApiResponse response = null;

		try {
			importProcess.importCsv(csv, weatherStation);
			response = new WxdbApiResponse();
		} catch (RuntimeException e) {
			LOGGER.error("Error while triggering csv import", e);
			response = new WxdbApiResponse(e, "ERROR", "Error while triggering initial import.");
		}

		return response;
	}

	@Override
	public WxdbApiResponse triggerInitialImport() {
		LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
		LocalDate endDate = LocalDate.now();
		LOGGER.debug("Start initial import from date {} till {}", startDate, endDate);

		WxdbApiResponse response = null;

		try {
			importProcess.importWeatherData(startDate, endDate);
			response = new WxdbApiResponse();
		} catch (RuntimeException e) {
			LOGGER.error("Error while triggering initial import", e);
			response = new WxdbApiResponse(e, "ERROR", "Error while triggering initial import.");
		}

		return response;
	}

}
