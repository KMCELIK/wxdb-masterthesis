package de.wxdb.wxdb_masterthesis.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wxdb.wxdb_masterthesis.schema.ImputationSummary;

/**
 * JPA Repository für die imputation_zusammenfassung Klasse.
 * 
 * @author Kaan Mustafa Celik
 */
public interface ImputationSummaryRepository extends JpaRepository<ImputationSummary, Long> {

}
