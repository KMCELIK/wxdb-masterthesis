package de.wxdb.wxdb_masterthesis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Klasse zum verknüpfen der properties für den CRON-Scheduler.
 * 
 * @author Kaan Mustafa Celik
 */
@Component
@ConfigurationProperties(prefix = "wxdb.import")
public class ImportScheduleProperties {

    private String cron;

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}