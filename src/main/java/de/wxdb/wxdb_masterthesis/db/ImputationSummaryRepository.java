package de.wxdb.wxdb_masterthesis.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wxdb.wxdb_masterthesis.schema.ImputationSummary;

public interface ImputationSummaryRepository extends JpaRepository<ImputationSummary, Long> {

}
