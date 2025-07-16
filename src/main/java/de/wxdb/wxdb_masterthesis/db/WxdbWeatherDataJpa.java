package de.wxdb.wxdb_masterthesis.db;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.wxdb.wxdb_masterthesis.dto.WxdbWeatherData;

/**
 * JPA Interface for {@link WxdbWeatherData} Wetterdaten Table.
 * 
 * @author Kaan Mustafa Celik
 */
public interface WxdbWeatherDataJpa extends JpaRepository<WxdbWeatherData, Long> {

	@Query("SELECT w.time FROM WxdbWeatherData w WHERE w.time BETWEEN :start AND :end")
	List<LocalDateTime> findExistingTimestamps(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}