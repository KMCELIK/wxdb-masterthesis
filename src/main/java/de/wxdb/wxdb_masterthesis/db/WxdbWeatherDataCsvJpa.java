package de.wxdb.wxdb_masterthesis.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wxdb.wxdb_masterthesis.schema.WxdbWeatherDataCsvPojo;

public interface WxdbWeatherDataCsvJpa extends JpaRepository<WxdbWeatherDataCsvPojo, Long> {

}
