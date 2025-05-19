package de.wxdb.wxdb_masterthesis.utils;

public enum FluxQueryTemplate {
	REALTIME_10M("""
			option startTime = :startTime
			from(bucket: "Wetterstation_RT")
			  |> range(start: startTime, stop: now())
			  |> filter(fn: (r) => r["_measurement"] == "Station Dach Haus A")
			  |> filter(fn: (r) => r["_field"] == "glob_RT" or r["_field"] == "temp_RT" or r["_field"] == "wind_RT"
			      or r["_field"] == "windgeschwindig_RT")
			  |> aggregateWindow(every: 10m, fn: last, createEmpty: true)
			  |> pivot(rowKey: ["_time"], columnKey: ["_field"], valueColumn: "_value")
			  |> yield(name: "wxdb_10m_RT")
			""", "10m"),

	REALTIME_1H("""
			option startTime = :startTime
			from(bucket: "Wetterstation_RT")
			  |> range(start: startTime, stop: now())
			  |> filter(fn: (r) => r["_measurement"] == "Station Dach Haus A")
			  |> filter(fn: (r) => r["_field"] == "glob_RT" or r["_field"] == "temp_RT" or r["_field"] == "wind_RT"
			      or r["_field"] == "windgeschwindig_RT")
			  |> aggregateWindow(every: 1h, fn: last, createEmpty: true)
			  |> pivot(rowKey: ["_time"], columnKey: ["_field"], valueColumn: "_value")
			  |> yield(name: "wxdb_1h_RT")
			""","1h");

	private final String template;
	private final String timeInterval;

	FluxQueryTemplate(String template, String timeInterval) {
		this.template = template;
		this.timeInterval = timeInterval;
	}

	public String render(String startTime) {
		if (startTime == null)
			return "";

		return template.replace(":startTime", startTime);
	}

	public String getTimeInterval() {
		return timeInterval;
	}

}
