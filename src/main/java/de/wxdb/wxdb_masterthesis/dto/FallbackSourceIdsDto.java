package de.wxdb.wxdb_masterthesis.dto;

public class FallbackSourceIdsDto {
	private Integer pressure_msl;
	private Integer wind_speed_30;

	public Integer getPressure_msl() {
		return pressure_msl;
	}

	public void setPressure_msl(Integer pressure_msl) {
		this.pressure_msl = pressure_msl;
	}

	public Integer getWind_speed_30() {
		return wind_speed_30;
	}

	public void setWind_speed_30(Integer wind_speed_30) {
		this.wind_speed_30 = wind_speed_30;
	}
}
