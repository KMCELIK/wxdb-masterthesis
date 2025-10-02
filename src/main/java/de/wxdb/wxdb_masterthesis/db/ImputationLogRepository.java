package de.wxdb.wxdb_masterthesis.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wxdb.wxdb_masterthesis.schema.ImputationLogPojo;

/**
 * JPA Repository für die imputation_log Tabelle.
 * 
 * @author Kaan Mustafa Celik
 */
public interface ImputationLogRepository extends JpaRepository<ImputationLogPojo, Long> {

}
