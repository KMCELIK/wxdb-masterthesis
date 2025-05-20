package de.wxdb.wxdb_masterthesis.dto;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class WeatherRealtimeData {
	private LocalDateTime time;
	private Double globRT;
	private Double tempRT;
	private Double windRT;
	private Double windgeschwindigRT;

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Double getGlobRT() {
		return globRT;
	}

	public void setGlobRT(Double globRT) {
		this.globRT = globRT;
	}

	public Double getTempRT() {
		return tempRT;
	}

	public void setTempRT(Double tempRT) {
		this.tempRT = tempRT;
	}

	public Double getWindRT() {
		return windRT;
	}

	public void setWindRT(Double windRT) {
		this.windRT = windRT;
	}

	public Double getWindgeschwindigRT() {
		return windgeschwindigRT;
	}

	public void setWindgeschwindigRT(Double windgeschwindigRT) {
		this.windgeschwindigRT = windgeschwindigRT;
	}
}
