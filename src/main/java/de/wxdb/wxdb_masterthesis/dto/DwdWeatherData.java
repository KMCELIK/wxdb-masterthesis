package de.wxdb.wxdb_masterthesis.dto;

import java.time.ZonedDateTime;
import java.util.Objects;

public class DwdWeatherData {
	private ZonedDateTime timestamp;
	private int source_id;
	private Double cloud_cover;
	private String condition;
	private Double dew_point;
	private String icon;
	private Double pressure_msl;
	private Integer relative_humidity;
	private Double temperature;
	private Integer visibility;
	private FallbackSourceIdsDto fallback_source_ids;

	private Double precipitation_10;
	private Double precipitation_30;
	private Double precipitation_60;
	private Double solar_10;
	private Double solar_30;
	private Double solar_60;
	private Integer sunshine_10;
	private Integer sunshine_30;
	private Integer sunshine_60;

	private Integer wind_direction_10;
	private Integer wind_direction_30;
	private Integer wind_direction_60;

	private Double wind_speed_10;
	private Double wind_speed_30;
	private Double wind_speed_60;

	private Integer wind_gust_direction_10;
	private Integer wind_gust_direction_30;
	private Integer wind_gust_direction_60;

	private Double wind_gust_speed_10;
	private Double wind_gust_speed_30;
	private Double wind_gust_speed_60;
	public ZonedDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public int getSource_id() {
		return source_id;
	}
	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}
	public Double getCloud_cover() {
		return cloud_cover;
	}
	public void setCloud_cover(Double cloud_cover) {
		this.cloud_cover = cloud_cover;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Double getDew_point() {
		return dew_point;
	}
	public void setDew_point(Double dew_point) {
		this.dew_point = dew_point;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Double getPressure_msl() {
		return pressure_msl;
	}
	public void setPressure_msl(Double pressure_msl) {
		this.pressure_msl = pressure_msl;
	}
	public Integer getRelative_humidity() {
		return relative_humidity;
	}
	public void setRelative_humidity(Integer relative_humidity) {
		this.relative_humidity = relative_humidity;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Integer getVisibility() {
		return visibility;
	}
	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}
	public FallbackSourceIdsDto getFallback_source_ids() {
		return fallback_source_ids;
	}
	public void setFallback_source_ids(FallbackSourceIdsDto fallback_source_ids) {
		this.fallback_source_ids = fallback_source_ids;
	}
	public Double getPrecipitation_10() {
		return precipitation_10;
	}
	public void setPrecipitation_10(Double precipitation_10) {
		this.precipitation_10 = precipitation_10;
	}
	public Double getPrecipitation_30() {
		return precipitation_30;
	}
	public void setPrecipitation_30(Double precipitation_30) {
		this.precipitation_30 = precipitation_30;
	}
	public Double getPrecipitation_60() {
		return precipitation_60;
	}
	public void setPrecipitation_60(Double precipitation_60) {
		this.precipitation_60 = precipitation_60;
	}
	public Double getSolar_10() {
		return solar_10;
	}
	public void setSolar_10(Double solar_10) {
		this.solar_10 = solar_10;
	}
	public Double getSolar_30() {
		return solar_30;
	}
	public void setSolar_30(Double solar_30) {
		this.solar_30 = solar_30;
	}
	public Double getSolar_60() {
		return solar_60;
	}
	public void setSolar_60(Double solar_60) {
		this.solar_60 = solar_60;
	}
	public Integer getSunshine_10() {
		return sunshine_10;
	}
	public void setSunshine_10(Integer sunshine_10) {
		this.sunshine_10 = sunshine_10;
	}
	public Integer getSunshine_30() {
		return sunshine_30;
	}
	public void setSunshine_30(Integer sunshine_30) {
		this.sunshine_30 = sunshine_30;
	}
	public Integer getSunshine_60() {
		return sunshine_60;
	}
	public void setSunshine_60(Integer sunshine_60) {
		this.sunshine_60 = sunshine_60;
	}
	public Integer getWind_direction_10() {
		return wind_direction_10;
	}
	public void setWind_direction_10(Integer wind_direction_10) {
		this.wind_direction_10 = wind_direction_10;
	}
	public Integer getWind_direction_30() {
		return wind_direction_30;
	}
	public void setWind_direction_30(Integer wind_direction_30) {
		this.wind_direction_30 = wind_direction_30;
	}
	public Integer getWind_direction_60() {
		return wind_direction_60;
	}
	public void setWind_direction_60(Integer wind_direction_60) {
		this.wind_direction_60 = wind_direction_60;
	}
	public Double getWind_speed_10() {
		return wind_speed_10;
	}
	public void setWind_speed_10(Double wind_speed_10) {
		this.wind_speed_10 = wind_speed_10;
	}
	public Double getWind_speed_30() {
		return wind_speed_30;
	}
	public void setWind_speed_30(Double wind_speed_30) {
		this.wind_speed_30 = wind_speed_30;
	}
	public Double getWind_speed_60() {
		return wind_speed_60;
	}
	public void setWind_speed_60(Double wind_speed_60) {
		this.wind_speed_60 = wind_speed_60;
	}
	public Integer getWind_gust_direction_10() {
		return wind_gust_direction_10;
	}
	public void setWind_gust_direction_10(Integer wind_gust_direction_10) {
		this.wind_gust_direction_10 = wind_gust_direction_10;
	}
	public Integer getWind_gust_direction_30() {
		return wind_gust_direction_30;
	}
	public void setWind_gust_direction_30(Integer wind_gust_direction_30) {
		this.wind_gust_direction_30 = wind_gust_direction_30;
	}
	public Integer getWind_gust_direction_60() {
		return wind_gust_direction_60;
	}
	public void setWind_gust_direction_60(Integer wind_gust_direction_60) {
		this.wind_gust_direction_60 = wind_gust_direction_60;
	}
	public Double getWind_gust_speed_10() {
		return wind_gust_speed_10;
	}
	public void setWind_gust_speed_10(Double wind_gust_speed_10) {
		this.wind_gust_speed_10 = wind_gust_speed_10;
	}
	public Double getWind_gust_speed_30() {
		return wind_gust_speed_30;
	}
	public void setWind_gust_speed_30(Double wind_gust_speed_30) {
		this.wind_gust_speed_30 = wind_gust_speed_30;
	}
	public Double getWind_gust_speed_60() {
		return wind_gust_speed_60;
	}
	public void setWind_gust_speed_60(Double wind_gust_speed_60) {
		this.wind_gust_speed_60 = wind_gust_speed_60;
	}
	@Override
	public String toString() {
		return "DwdWeatherData [timestamp=" + timestamp + ", source_id=" + source_id + ", cloud_cover=" + cloud_cover
				+ ", condition=" + condition + ", dew_point=" + dew_point + ", icon=" + icon + ", pressure_msl="
				+ pressure_msl + ", relative_humidity=" + relative_humidity + ", temperature=" + temperature
				+ ", visibility=" + visibility + ", fallback_source_ids=" + fallback_source_ids + ", precipitation_10="
				+ precipitation_10 + ", precipitation_30=" + precipitation_30 + ", precipitation_60=" + precipitation_60
				+ ", solar_10=" + solar_10 + ", solar_30=" + solar_30 + ", solar_60=" + solar_60 + ", sunshine_10="
				+ sunshine_10 + ", sunshine_30=" + sunshine_30 + ", sunshine_60=" + sunshine_60 + ", wind_direction_10="
				+ wind_direction_10 + ", wind_direction_30=" + wind_direction_30 + ", wind_direction_60="
				+ wind_direction_60 + ", wind_speed_10=" + wind_speed_10 + ", wind_speed_30=" + wind_speed_30
				+ ", wind_speed_60=" + wind_speed_60 + ", wind_gust_direction_10=" + wind_gust_direction_10
				+ ", wind_gust_direction_30=" + wind_gust_direction_30 + ", wind_gust_direction_60="
				+ wind_gust_direction_60 + ", wind_gust_speed_10=" + wind_gust_speed_10 + ", wind_gust_speed_30="
				+ wind_gust_speed_30 + ", wind_gust_speed_60=" + wind_gust_speed_60 + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(cloud_cover, condition, dew_point, fallback_source_ids, icon, precipitation_10,
				precipitation_30, precipitation_60, pressure_msl, relative_humidity, solar_10, solar_30, solar_60,
				source_id, sunshine_10, sunshine_30, sunshine_60, temperature, timestamp, visibility, wind_direction_10,
				wind_direction_30, wind_direction_60, wind_gust_direction_10, wind_gust_direction_30,
				wind_gust_direction_60, wind_gust_speed_10, wind_gust_speed_30, wind_gust_speed_60, wind_speed_10,
				wind_speed_30, wind_speed_60);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DwdWeatherData other = (DwdWeatherData) obj;
		return Objects.equals(cloud_cover, other.cloud_cover) && Objects.equals(condition, other.condition)
				&& Objects.equals(dew_point, other.dew_point)
				&& Objects.equals(fallback_source_ids, other.fallback_source_ids) && Objects.equals(icon, other.icon)
				&& Objects.equals(precipitation_10, other.precipitation_10)
				&& Objects.equals(precipitation_30, other.precipitation_30)
				&& Objects.equals(precipitation_60, other.precipitation_60)
				&& Objects.equals(pressure_msl, other.pressure_msl)
				&& Objects.equals(relative_humidity, other.relative_humidity)
				&& Objects.equals(solar_10, other.solar_10) && Objects.equals(solar_30, other.solar_30)
				&& Objects.equals(solar_60, other.solar_60) && source_id == other.source_id
				&& Objects.equals(sunshine_10, other.sunshine_10) && Objects.equals(sunshine_30, other.sunshine_30)
				&& Objects.equals(sunshine_60, other.sunshine_60) && Objects.equals(temperature, other.temperature)
				&& Objects.equals(timestamp, other.timestamp) && Objects.equals(visibility, other.visibility)
				&& Objects.equals(wind_direction_10, other.wind_direction_10)
				&& Objects.equals(wind_direction_30, other.wind_direction_30)
				&& Objects.equals(wind_direction_60, other.wind_direction_60)
				&& Objects.equals(wind_gust_direction_10, other.wind_gust_direction_10)
				&& Objects.equals(wind_gust_direction_30, other.wind_gust_direction_30)
				&& Objects.equals(wind_gust_direction_60, other.wind_gust_direction_60)
				&& Objects.equals(wind_gust_speed_10, other.wind_gust_speed_10)
				&& Objects.equals(wind_gust_speed_30, other.wind_gust_speed_30)
				&& Objects.equals(wind_gust_speed_60, other.wind_gust_speed_60)
				&& Objects.equals(wind_speed_10, other.wind_speed_10)
				&& Objects.equals(wind_speed_30, other.wind_speed_30)
				&& Objects.equals(wind_speed_60, other.wind_speed_60);
	}
	
	
}
