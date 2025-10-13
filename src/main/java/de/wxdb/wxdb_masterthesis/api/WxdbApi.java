package de.wxdb.wxdb_masterthesis.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.wxdb.wxdb_masterthesis.dto.WxdbApiResponse;

/**
 * API interface.
 * 
 * @author Kaan Mustafa Celik
 */
@RestController
@RequestMapping("/api")
public interface WxdbApi {

	/**
	 * Method for manual import of a csv weather file.
	 * 
	 * @param csv file
	 * @return {@link WxdbApiResponse}
	 */
	@PostMapping(path = "/import/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	WxdbApiResponse importCsv(@RequestPart("csvFile") MultipartFile csvFile,
			@RequestPart("weatherStation") String weatherStation);

	/**
	 * Triggers the initial Import which imports weather data beginning from the
	 * current year.
	 * 
	 * @param startDate optional start date for the import in format dd.MM.yyyy
	 * @return {@link WxdbApiResponse}
	 */
	@PostMapping("/import/initialize")
	WxdbApiResponse triggerInitialImport( @RequestParam(required = false) String startDate);

	/**
	 * Triggers the daily Import which imports the realtime weather data of the last
	 * 30 hours.
	 * 
	 * @return {@link WxdbApiResponse}
	 */
	@PostMapping("/import/daily")
	WxdbApiResponse triggerDailyImport();

}
