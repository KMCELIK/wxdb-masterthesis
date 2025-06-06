package de.wxdb.wxdb_masterthesis.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.wxdb.wxdb_masterthesis.db.ProzessLogRepository;
import de.wxdb.wxdb_masterthesis.db.WxdbWeatherDataJpa;
import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;
import de.wxdb.wxdb_masterthesis.schema.Processlog;

@Service
public class InsertWxdbService {
	
	@Autowired
	private WxdbWeatherDataJpa weatherRepo;
	
	@Autowired
	private ProzessLogRepository logRepo;
	
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

}
