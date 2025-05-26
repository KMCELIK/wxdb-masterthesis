package de.wxdb.wxdb_masterthesis.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.wxdb.wxdb_masterthesis.dto.BrightskyApiSourceResponse;
import de.wxdb.wxdb_masterthesis.dto.BrightskySynopResponse;

@FeignClient(name = "brightskyClient", url = "${brightsky.api.baseurl}")
public interface BrightskyFeignClient {

	/**
	 * Returns hourly weather data only works for Forecasts and current daily
	 * weather.
	 * 
	 * @param startDate start Date in format yyyy-mm-dd
	 * @param lastDate  end date in format yyyy-mm-dd
	 * @param sourceIds works primarely for forecast type stations.
	 * @return {@link BrightskyApiSourceResponse}
	 */
	@GetMapping("/weather")
	BrightskyApiSourceResponse getWeatherBySourceIds(@RequestParam("date") String startDate,
			@RequestParam("last_date") String lastDate, @RequestParam("source_id") List<Long> sourceIds);

	/**
	 * Returns hourly weather data only works for Forecasts and current daily
	 * weather.
	 * 
	 * @param startDate start Date in format yyyy-mm-dd
	 * @param lastDate  end date in format yyyy-mm-dd
	 * @param sourceIds works primarely for forecast type stations.
	 * @return {@link BrightskyApiSourceResponse}
	 */
	@GetMapping("/weather")
	BrightskyApiSourceResponse getWeatherByGeography(@RequestParam("date") String startDate,
			@RequestParam("last_date") String lastDate, @RequestParam("lat") double latitude,
			@RequestParam("lon") double longitude);

	/**
	 * Returns a list of ten-minutely SYNOP observations for the time range given by
	 * date and last_date. Note that Bright Sky only stores SYNOP observations from
	 * the past 30 hours.
	 * 
	 * @param startDate start Date
	 * @param lastDate  end date
	 * @param sourceIds station source Ids
	 * @return {@link BrightskySynopResponse}
	 */
	@GetMapping("/synop")
	BrightskySynopResponse getSynop(@RequestParam("date") String startDate, @RequestParam("last_date") String lastDate,
			@RequestParam("source_id") List<Long> sourceIds);
}
