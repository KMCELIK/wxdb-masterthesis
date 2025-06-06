package de.wxdb.wxdb_masterthesis.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wxdb.wxdb_masterthesis.schema.Processlog;

public interface ProzessLogRepository extends JpaRepository<Processlog, Long> {

    // Optional: Prozesse nach Name filtern
    List<Processlog> findByProzessName(String prozessName);

    // Optional: Letzter Eintrag zu einem Prozess
    Processlog findTopByProzessNameOrderByZeitstempelDesc(String prozessName);
}