package de.wxdb.wxdb_masterthesis.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.wxdb.wxdb_masterthesis.db.NotificationRepository;
import de.wxdb.wxdb_masterthesis.db.ProzessLogRepository;
import de.wxdb.wxdb_masterthesis.db.WxdbWeatherDataCsvJpa;
import de.wxdb.wxdb_masterthesis.db.WxdbWeatherDataJpa;
import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;
import de.wxdb.wxdb_masterthesis.schema.NotificationPojo;
import de.wxdb.wxdb_masterthesis.schema.Processlog;
import de.wxdb.wxdb_masterthesis.schema.WxdbWeatherDataCsvPojo;

@Service
public class InsertWxdbService {

	private static final String DUPLICATE_ERROR = "DUPLIKAT";
	private static final String DUPLICATE_ERROR_DESCRIPTION = "Datensatz wird herausgefiltert.";

	@Autowired
	private WxdbWeatherDataJpa weatherRepo;
	
	@Autowired
	private WxdbWeatherDataCsvJpa csvWeatherRepo;

	@Autowired
	private ProzessLogRepository logRepo;
	
	@Autowired
	private NotificationRepository notificationRepo;

	@Transactional
	public Processlog startProcessLog(String processName, LocalDateTime timestamp) {
		Processlog log = new Processlog();
		log.setProzessName(processName);
		log.setStatus("STARTED");
		log.setZeitstempel(timestamp);

		return logRepo.save(log);
	}

	@Transactional
	public void completeProcessLog(Processlog log, boolean isSuccessful, String description) {
		log.setStatus(isSuccessful ? "COMPLETED" : "ERROR");
		log.setBeschreibung(description);
		logRepo.save(log);
	}

	@Transactional
	public void insertWeatherData(List<WxdbWeatherData> weatherDataList) {
		weatherRepo.saveAll(weatherDataList);
	}
	
	@Transactional
	public void insertWeatherDataCsv(List<WxdbWeatherDataCsvPojo> weatherDataList) {
		csvWeatherRepo.saveAll(weatherDataList);
	}
	
	@Transactional
	public void insertNotifications(List<NotificationPojo> notifications) {
		notificationRepo.saveAll(notifications);
	}

	/**
	 * 	/**
	 * Filtert Wetterdaten welche schon in der Datenbank existent sind aus.
	 * 
	 * @param weatherDataList wetterdaten
	 * @param startDate       start Datum
	 * @param endDate         end Datum
	 * @return liefert eine Liste von {@link NotificationPojo} um die ausgefilterten Datensätze als Hinweise zu loggen.
	 */
	public List<NotificationPojo> filterDuplicates(List<WxdbWeatherData> weatherDataList, LocalDate startDate,
			LocalDate endDate) {
		List<NotificationPojo> notifications = new ArrayList<NotificationPojo>();
		Set<LocalDateTime> existingTimestamps = new HashSet<>(
				weatherRepo.findExistingTimestamps(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()));

		weatherDataList.removeIf(wd -> {
			if (existingTimestamps.contains(wd.getTime())) {
				notifications.add(new NotificationPojo(wd.getTime(), DUPLICATE_ERROR_DESCRIPTION, DUPLICATE_ERROR));
				return true;
			}
			return false;
		});

		return notifications;
	}

}
