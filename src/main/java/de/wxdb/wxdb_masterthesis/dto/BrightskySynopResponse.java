package de.wxdb.wxdb_masterthesis.dto;

import java.util.List;

public class BrightskySynopResponse {
    private List<DwdWeatherData> weather;
    private List<DwdSourceData> sources;

    public List<DwdWeatherData> getWeather() {
        return weather;
    }

    public void setWeather(List<DwdWeatherData> weather) {
        this.weather = weather;
    }

    public List<DwdSourceData> getSources() {
        return sources;
    }

    public void setSources(List<DwdSourceData> sources) {
        this.sources = sources;
    }
}
