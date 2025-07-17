package de.wxdb.wxdb_masterthesis;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.wxdb.wxdb_masterthesis.process.WeatherImportProcess;

public class DailyImportJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(DailyImportJob.class);

	@Autowired
	private WeatherImportProcess importProcess;

	@Override
	public void execute(JobExecutionContext context) {
		LOGGER.info("Start Quartz-Job: Import realtimeWeatherData");

		try {
			importProcess.importRealtimeWeatherData();
			LOGGER.info("Finished Quartz-Job: Import realtimeWeatherData");
		} catch (Exception e) {
			LOGGER.error("❌ Fehler beim Ausführen des Jobs: {}", e.getMessage(), e);
		}

	}
}
