package de.wxdb.wxdb_masterthesis.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;

/**
 * JPA Interface for {@link WxdbWeatherData} Wetterdaten Table.
 * 
 * @author Kaan Mustafa Celik
 */
public interface WxdbWeatherDataJpa extends JpaRepository<WxdbWeatherData, Long> {

}