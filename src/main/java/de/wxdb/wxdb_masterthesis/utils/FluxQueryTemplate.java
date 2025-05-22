package de.wxdb.wxdb_masterthesis.utils;

import io.micrometer.common.util.StringUtils;

/**
 * Enum für Flux Query Templates. Muss vor Ausführung gerendert werden.
 * @author Kaan Mustafa Celik
 */
public enum FluxQueryTemplate {

    REALTIME_10M("""
            option startTime = :startTime
            from(bucket: ":bucket")
              |> range(start: startTime, stop: now())
              |> filter(fn: (r) => r["_measurement"] == ":measurement")
              |> filter(fn: (r) => r["_field"] == "glob_RT" or r["_field"] == "temp_RT" or r["_field"] == "wind_RT"
                  or r["_field"] == "windgeschwindig_RT")
              |> aggregateWindow(every: 10m, fn: last, createEmpty: true)
              |> pivot(rowKey: ["_time"], columnKey: ["_field"], valueColumn: "_value")
              |> yield(name: "wxdb_10m_RT")
            """, "10m", true),

    REALTIME_1H("""
            option startTime = :startTime
            from(bucket: ":bucket")
              |> range(start: startTime, stop: now())
              |> filter(fn: (r) => r["_measurement"] == ":measurement")
              |> filter(fn: (r) => r["_field"] == "glob_RT" or r["_field"] == "temp_RT" or r["_field"] == "wind_RT"
                  or r["_field"] == "windgeschwindig_RT")
              |> aggregateWindow(every: 1h, fn: last, createEmpty: true)
              |> pivot(rowKey: ["_time"], columnKey: ["_field"], valueColumn: "_value")
              |> yield(name: "wxdb_1h_RT")
            """, "1h", true),

    HISTORICAL_10M("""
            option startTime = :startTime
            option stopTime = :endTime
            option interval = 10m
            from(bucket: ":bucket")
              |> range(start: startTime, stop: stopTime)
              |> filter(fn: (r) => r["_measurement"] == ":measurement")
              |> filter(fn: (r) => r["_field"] == "glob_AVG" or r["_field"] == "temp_AVG" or r["_field"] == "wind_AVG"
                  or r["_field"] == "windgeschwindig_AVG")
              |> aggregateWindow(every: interval, fn: last, createEmpty: true)
              |> pivot(rowKey: ["_time"], columnKey: ["_field"], valueColumn: "_value")
              |> yield(name: "wxdb_10m_historical")
            """, "10m", false),

    HISTORICAL_1H("""
            option startTime = :startTime
            option stopTime = :endTime
            option interval = 1h
            from(bucket: ":bucket")
              |> range(start: startTime, stop: stopTime)
              |> filter(fn: (r) => r["_measurement"] == ":measurement")
              |> filter(fn: (r) => r["_field"] == "glob_AVG" or r["_field"] == "temp_AVG" or r["_field"] == "wind_AVG"
                  or r["_field"] == "windgeschwindig_AVG")
              |> aggregateWindow(every: interval, fn: last, createEmpty: true)
              |> pivot(rowKey: ["_time"], columnKey: ["_field"], valueColumn: "_value")
              |> yield(name: "wxdb_1h_historical")
            """, "1h", false);

    private final String template;
    private final String timeInterval;
    private final boolean isRealtime;

    FluxQueryTemplate(String template, String timeInterval, boolean isRealtime) {
        this.template = template;
        this.timeInterval = timeInterval;
        this.isRealtime = isRealtime;
    }

    /**
     * Rendert die Query mit allen Parametern. Keine Werte dürfen null oder leer sein.
     * 
     * @param startTime Start-Zeit im Flux-Format (z. B. -1h oder 2024-01-01T00:00:00Z)
     * @param endTime   End-Zeit (nur bei historischen Abfragen notwendig)
     * @param bucket    InfluxDB Bucket Name
     * @param measurement InfluxDB Measurement Name
     * @return Gerenderte Flux Query oder leere Zeichenkette bei Fehler
     */
    public String render(String startTime, String endTime, String bucket, String measurement) {
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(bucket) || StringUtils.isBlank(measurement)) {
            return "";
        }

        String rendered = template
            .replace(":startTime", startTime)
            .replace(":bucket", bucket)
            .replace(":measurement", measurement);

        if (!isRealtime) {
            if (StringUtils.isBlank(endTime)) {
                return ""; 
            }
            rendered = rendered.replace(":endTime", endTime);
        }

        return rendered;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public boolean isRealtime() {
        return isRealtime;
    }
}