package de.wxdb.wxdb_masterthesis.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wxdb.wxdb_masterthesis.schema.NotificationPojo;

public interface NotificationRepository extends JpaRepository<NotificationPojo, Integer> {

}