package de.wxdb.wxdb_masterthesis;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import de.wxdb.wxdb_masterthesis.process.WeatherImportProcess;

/**
 * Direkte Ausführung bestimmter Methoden ohne API-Einbindung.
 * Diese Klasse schafft sich im Verlauf des Projektes ab,
 * sie wird nur genutzt um bei Bedarf die direkte Ausführung von diversen Services zu ermöglichen.
 *
 * @author Kaan Mustafa Celik
 */
@Component
public class StartupExecutor implements ApplicationRunner {
	
	@Autowired
    private WeatherImportProcess importProcess;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("[StartupRunner] Starte WeatherDataService in 1 Sekunde ...");
		importProcess.importWeatherData(LocalDate.now().minusDays(7), null, true, true);
		
		// other test cases:
	//	importProcess.importWeatherData(LocalDate.now().minusDays(7), LocalDate.now(), false, true);
	//	importProcess.importWeatherData(LocalDate.now().minusDays(7), LocalDate.now().minusDays(1), true, false);
	//	importProcess.importWeatherData(LocalDate.now().minusDays(7), LocalDate.now().minusDays(1), false, false);
		
	}

}
