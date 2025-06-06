package de.wxdb.wxdb_masterthesis.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wxdb.wxdb_masterthesis.schema.ImputationLogPojo;

public interface ImputationLogRepository extends JpaRepository<ImputationLogPojo, Long> {

}
