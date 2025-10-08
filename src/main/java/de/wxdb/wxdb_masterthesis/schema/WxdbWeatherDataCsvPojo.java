package de.wxdb.wxdb_masterthesis.schema;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wetterdaten_csv")
public class WxdbWeatherDataCsvPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Weather related
	private LocalDateTime time;
	private Double temperature; // °C
	private Double windDirection; // degrees
	private Double windSpeed; // m/s
	private Double globalRadiation; // W/m²

	// Data Source related
	private String Datasource; // e.g. InfluxDB, DWD
	private String weatherStationSource; // e.g. Dortmund Weatherstation
	private long stationSourceId;
	private boolean isRealtime;

	// POJO related -- DB-Username, dataset integration time, version of the dataset
	// in case of updates
	private String lastChangedBy = "SYSTEM";
	private LocalDateTime lastChangedTime;
	private int version = 0;

	// Imputation:
	private boolean imputed;

	@ManyToOne
	@JoinColumn(name = "imputation_zusammenfassung_id")
	private ImputationSummary zusammenfassung;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Double windDirection) {
		this.windDirection = windDirection;
	}

	public Double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(Double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public Double getGlobalRadiation() {
		return globalRadiation;
	}

	public void setGlobalRadiation(Double globalRadiation) {
		this.globalRadiation = globalRadiation;
	}

	public String getDatasource() {
		return Datasource;
	}

	public void setDatasource(String datasource) {
		Datasource = datasource;
	}

	public String getWeatherStationSource() {
		return weatherStationSource;
	}

	public void setWeatherStationSource(String weatherStationSource) {
		this.weatherStationSource = weatherStationSource;
	}

	public String getLastChangedBy() {
		return lastChangedBy;
	}

	public void setLastChangedBy(String lastChangedBy) {
		this.lastChangedBy = lastChangedBy;
	}

	public LocalDateTime getLastChangedTime() {
		return lastChangedTime;
	}

	public void setLastChangedTime(LocalDateTime lastChangedTime) {
		this.lastChangedTime = lastChangedTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public boolean isImputed() {
		return imputed;
	}

	public void setImputed(boolean imputed) {
		this.imputed = imputed;
	}

	public ImputationSummary getZusammenfassung() {
		return zusammenfassung;
	}

	public void setZusammenfassung(ImputationSummary zusammenfassung) {
		this.zusammenfassung = zusammenfassung;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Datasource, globalRadiation, id, imputed, isRealtime, lastChangedBy, lastChangedTime,
				stationSourceId, temperature, time, version, weatherStationSource, windDirection, windSpeed,
				zusammenfassung);
	}

	@Override
	public String toString() {
		return "WxdbWeatherData [time=" + time + ", temperature=" + temperature + ", windDirection=" + windDirection
				+ ", windSpeed=" + windSpeed + ", globalRadiation=" + globalRadiation + ", Datasource=" + Datasource
				+ ", weatherStationSource=" + weatherStationSource + ", lastChangedBy=" + lastChangedBy
				+ ", lastChangedTime=" + lastChangedTime + ", version=" + version + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WxdbWeatherDataCsvPojo other = (WxdbWeatherDataCsvPojo) obj;
		return Objects.equals(Datasource, other.Datasource) && Objects.equals(globalRadiation, other.globalRadiation)
				&& Objects.equals(id, other.id) && imputed == other.imputed && isRealtime == other.isRealtime
				&& Objects.equals(lastChangedBy, other.lastChangedBy)
				&& Objects.equals(lastChangedTime, other.lastChangedTime) && stationSourceId == other.stationSourceId
				&& Objects.equals(temperature, other.temperature) && Objects.equals(time, other.time)
				&& version == other.version && Objects.equals(weatherStationSource, other.weatherStationSource)
				&& Objects.equals(windDirection, other.windDirection) && Objects.equals(windSpeed, other.windSpeed)
				&& Objects.equals(zusammenfassung, other.zusammenfassung);
	}

	public long getStationSourceId() {
		return stationSourceId;
	}

	public void setStationSourceId(long stationSourceId) {
		this.stationSourceId = stationSourceId;
	}

	public boolean isRealtime() {
		return isRealtime;
	}

	public void setRealtime(boolean isRealtime) {
		this.isRealtime = isRealtime;
	}

}
