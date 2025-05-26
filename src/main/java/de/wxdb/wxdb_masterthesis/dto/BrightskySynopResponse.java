package de.wxdb.wxdb_masterthesis.dto;

import java.util.List;

public class BrightskySynopResponse {
    private List<DwdSynopWeatherData> weather;
    private List<DwdSourceData> sources;

    public List<DwdSynopWeatherData> getWeather() {
        return weather;
    }

    public void setWeather(List<DwdSynopWeatherData> weather) {
        this.weather = weather;
    }

    public List<DwdSourceData> getSources() {
        return sources;
    }

    public void setSources(List<DwdSourceData> sources) {
        this.sources = sources;
    }
}
