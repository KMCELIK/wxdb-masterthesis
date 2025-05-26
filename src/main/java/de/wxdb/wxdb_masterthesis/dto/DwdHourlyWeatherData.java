package de.wxdb.wxdb_masterthesis.dto;

import java.util.Objects;

public class DwdHourlyWeatherData {
	private String timestamp;
	private int source_id;
	private Double precipitation;
	private Double pressure_msl;
	private Integer sunshine;
	private Double temperature;
	private Integer wind_direction;
	private Double wind_speed;
	private Integer cloud_cover;
	private Double dew_point;
	private Double relative_humidity;
	private Integer visibility;
	private Integer wind_gust_direction;
	private Double wind_gust_speed;
	private String condition;
	private Integer precipitation_probability;
	private Integer precipitation_probability_6h;
	private Double solar;
	private String icon;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getSource_id() {
		return source_id;
	}

	public void setSource_id(int source_id) {
		this.source_id = source_id;
	}

	public Double getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(Double precipitation) {
		this.precipitation = precipitation;
	}

	public Double getPressure_msl() {
		return pressure_msl;
	}

	public void setPressure_msl(Double pressure_msl) {
		this.pressure_msl = pressure_msl;
	}

	public Integer getSunshine() {
		return sunshine;
	}

	public void setSunshine(Integer sunshine) {
		this.sunshine = sunshine;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Integer getWind_direction() {
		return wind_direction;
	}

	public void setWind_direction(Integer wind_direction) {
		this.wind_direction = wind_direction;
	}

	public Double getWind_speed() {
		return wind_speed;
	}

	public void setWind_speed(Double wind_speed) {
		this.wind_speed = wind_speed;
	}

	public Integer getCloud_cover() {
		return cloud_cover;
	}

	public void setCloud_cover(Integer cloud_cover) {
		this.cloud_cover = cloud_cover;
	}

	public Double getDew_point() {
		return dew_point;
	}

	public void setDew_point(Double dew_point) {
		this.dew_point = dew_point;
	}

	public Double getRelative_humidity() {
		return relative_humidity;
	}

	public void setRelative_humidity(Double relative_humidity) {
		this.relative_humidity = relative_humidity;
	}

	public Integer getVisibility() {
		return visibility;
	}

	public void setVisibility(Integer visibility) {
		this.visibility = visibility;
	}

	public Integer getWind_gust_direction() {
		return wind_gust_direction;
	}

	public void setWind_gust_direction(Integer wind_gust_direction) {
		this.wind_gust_direction = wind_gust_direction;
	}

	public Double getWind_gust_speed() {
		return wind_gust_speed;
	}

	public void setWind_gust_speed(Double wind_gust_speed) {
		this.wind_gust_speed = wind_gust_speed;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Integer getPrecipitation_probability() {
		return precipitation_probability;
	}

	public void setPrecipitation_probability(Integer precipitation_probability) {
		this.precipitation_probability = precipitation_probability;
	}

	public Integer getPrecipitation_probability_6h() {
		return precipitation_probability_6h;
	}

	public void setPrecipitation_probability_6h(Integer precipitation_probability_6h) {
		this.precipitation_probability_6h = precipitation_probability_6h;
	}

	public Double getSolar() {
		return solar;
	}

	public void setSolar(Double solar) {
		this.solar = solar;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cloud_cover, condition, dew_point, icon, precipitation, precipitation_probability,
				precipitation_probability_6h, pressure_msl, relative_humidity, solar, source_id, sunshine, temperature,
				timestamp, visibility, wind_direction, wind_gust_direction, wind_gust_speed, wind_speed);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DwdHourlyWeatherData other = (DwdHourlyWeatherData) obj;
		return Objects.equals(cloud_cover, other.cloud_cover) && Objects.equals(condition, other.condition)
				&& Objects.equals(dew_point, other.dew_point) && Objects.equals(icon, other.icon)
				&& Objects.equals(precipitation, other.precipitation)
				&& Objects.equals(precipitation_probability, other.precipitation_probability)
				&& Objects.equals(precipitation_probability_6h, other.precipitation_probability_6h)
				&& Objects.equals(pressure_msl, other.pressure_msl)
				&& Objects.equals(relative_humidity, other.relative_humidity) && Objects.equals(solar, other.solar)
				&& source_id == other.source_id && Objects.equals(sunshine, other.sunshine)
				&& Objects.equals(temperature, other.temperature) && Objects.equals(timestamp, other.timestamp)
				&& Objects.equals(visibility, other.visibility) && Objects.equals(wind_direction, other.wind_direction)
				&& Objects.equals(wind_gust_direction, other.wind_gust_direction)
				&& Objects.equals(wind_gust_speed, other.wind_gust_speed)
				&& Objects.equals(wind_speed, other.wind_speed);
	}

	@Override
	public String toString() {
		return "DwdHourlyWeatherData [timestamp=" + timestamp + ", source_id=" + source_id + ", precipitation="
				+ precipitation + ", pressure_msl=" + pressure_msl + ", sunshine=" + sunshine + ", temperature="
				+ temperature + ", wind_direction=" + wind_direction + ", wind_speed=" + wind_speed + ", cloud_cover="
				+ cloud_cover + ", dew_point=" + dew_point + ", relative_humidity=" + relative_humidity
				+ ", visibility=" + visibility + ", wind_gust_direction=" + wind_gust_direction + ", wind_gust_speed="
				+ wind_gust_speed + ", condition=" + condition + ", precipitation_probability="
				+ precipitation_probability + ", precipitation_probability_6h=" + precipitation_probability_6h
				+ ", solar=" + solar + ", icon=" + icon + "]";
	}

}
