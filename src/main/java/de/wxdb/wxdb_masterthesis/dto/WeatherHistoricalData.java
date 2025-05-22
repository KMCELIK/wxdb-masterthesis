package de.wxdb.wxdb_masterthesis.dto;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * DTO for historical Datasets from the InfluxDB.
 * 
 * @author Kaan Mustafa Celik
 */
@ToString
@EqualsAndHashCode
public class WeatherHistoricalData {
	private LocalDateTime time;
	private Double globAVG;
	private Double tempAVG;
	private Double windAVG;
	private Double windgeschwindigAVG;

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Double getGlobAVG() {
		return globAVG;
	}

	public void setGlobAVG(Double globAVG) {
		this.globAVG = globAVG;
	}

	public Double getTempAVG() {
		return tempAVG;
	}

	public void setTempAVG(Double tempAVG) {
		this.tempAVG = tempAVG;
	}

	public Double getWindAVG() {
		return windAVG;
	}

	public void setWindAVG(Double windAVG) {
		this.windAVG = windAVG;
	}

	public Double getWindgeschwindigAVG() {
		return windgeschwindigAVG;
	}

	public void setWindgeschwindigAVG(Double windgeschwindigAVG) {
		this.windgeschwindigAVG = windgeschwindigAVG;
	}
}