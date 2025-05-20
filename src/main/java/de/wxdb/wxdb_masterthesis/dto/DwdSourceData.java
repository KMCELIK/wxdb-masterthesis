package de.wxdb.wxdb_masterthesis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object für den Response der Sources Schnittstelle der
 * Brightsky-API.
 * 
 * @author Kaan Mustafa Celik
 */
public class DwdSourceData {

	@JsonProperty("id")
	private long id;

	@JsonProperty("dwd_station_id")
	private String dwdStationId;

	@JsonProperty("observation_type")
	private String observationType;

	@JsonProperty("lat")
	private Double lat;

	@JsonProperty("lon")
	private Double lon;

	@JsonProperty("height")
	private Integer height;

	@JsonProperty("station_name")
	private String stationName;

	@JsonProperty("wmo_station_id")
	private String wmoStationId;

	@JsonProperty("first_record")
	private String firstRecord;

	@JsonProperty("last_record")
	private String lastRecord;

	@JsonProperty("distance")
	private Integer distance;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDwdStationId() {
		return dwdStationId;
	}

	public void setDwdStationId(String dwdStationId) {
		this.dwdStationId = dwdStationId;
	}

	public String getObservationType() {
		return observationType;
	}

	public void setObservationType(String observationType) {
		this.observationType = observationType;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getWmoStationId() {
		return wmoStationId;
	}

	public void setWmoStationId(String wmoStationId) {
		this.wmoStationId = wmoStationId;
	}

	public String getFirstRecord() {
		return firstRecord;
	}

	public void setFirstRecord(String firstRecord) {
		this.firstRecord = firstRecord;
	}

	public String getLastRecord() {
		return lastRecord;
	}

	public void setLastRecord(String lastRecord) {
		this.lastRecord = lastRecord;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

}
