package de.wxdb.wxdb_masterthesis.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Api Response Object for Brightsky Api sources and weather endpoint.
 * 
 * @author Kaan Mustafa Celik
 */
public class BrightskyApiSourceResponse {
	private List<DwdSourceData> sources = new ArrayList<DwdSourceData>();
	private List<DwdHourlyWeatherData> weather = new ArrayList<DwdHourlyWeatherData>();

	public List<DwdSourceData> getSources() {
		return sources;
	}

	public void setSources(List<DwdSourceData> sources) {
		this.sources = sources;
	}

	public List<DwdHourlyWeatherData> getWeather() {
		return weather;
	}

	public void setWeather(List<DwdHourlyWeatherData> weather) {
		this.weather = weather;
	}

}
