package de.wxdb.wxdb_masterthesis.dto;

import java.util.List;

public class BrightskyApiSourceResponse {
    private List<DwdSourceData> sources;

    public List<DwdSourceData> getSources() {
        return sources;
    }

    public void setSources(List<DwdSourceData> sources) {
        this.sources = sources;
    }
}
