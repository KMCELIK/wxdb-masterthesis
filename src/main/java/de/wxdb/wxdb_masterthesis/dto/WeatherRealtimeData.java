package de.wxdb.wxdb_masterthesis.dto;

import java.time.LocalDateTime;

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

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
