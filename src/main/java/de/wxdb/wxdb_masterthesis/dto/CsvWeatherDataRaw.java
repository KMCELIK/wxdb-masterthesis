package de.wxdb.wxdb_masterthesis.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CsvWeatherDataRaw {
	@JsonProperty("Datum")
	private String datum;

	@JsonProperty("Zeit")
	private String zeit;

	@JsonProperty("Lufttemperatur_13 C")
	private String temperatur;

	@JsonProperty("Windgeschw._13 m/s")
	private String windSpeed;

	@JsonProperty("Windrichtung_13 deg")
	private String windDirection;

	@JsonProperty("Globalstrahlung_13 W/m2")
	private String globalstrahlung;

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getZeit() {
		return zeit;
	}

	public void setZeit(String zeit) {
		this.zeit = zeit;
	}

	public String getTemperatur() {
		return temperatur;
	}

	public void setTemperatur(String temperatur) {
		this.temperatur = temperatur;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getGlobalstrahlung() {
		return globalstrahlung;
	}

	public void setGlobalstrahlung(String globalstrahlung) {
		this.globalstrahlung = globalstrahlung;
	}

	public CsvWeatherDataRaw(String datum, String zeit, String temperatur, String windSpeed, String windDirection,
			String globalstrahlung) {
		super();
		this.datum = datum;
		this.zeit = zeit;
		this.temperatur = temperatur;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.globalstrahlung = globalstrahlung;
	}

	public CsvWeatherDataRaw() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(datum, globalstrahlung, temperatur, windDirection, windSpeed, zeit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CsvWeatherDataRaw other = (CsvWeatherDataRaw) obj;
		return Objects.equals(datum, other.datum) && Objects.equals(globalstrahlung, other.globalstrahlung)
				&& Objects.equals(temperatur, other.temperatur) && Objects.equals(windDirection, other.windDirection)
				&& Objects.equals(windSpeed, other.windSpeed) && Objects.equals(zeit, other.zeit);
	}

	@Override
	public String toString() {
		return "CsvWeatherDataRaw [datum=" + datum + ", zeit=" + zeit + ", temperatur=" + temperatur + ", windSpeed="
				+ windSpeed + ", windDirection=" + windDirection + ", globalstrahlung=" + globalstrahlung + "]";
	}
}