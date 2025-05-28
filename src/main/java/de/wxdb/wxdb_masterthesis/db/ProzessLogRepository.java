package de.wxdb.wxdb_masterthesis.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProzessLogRepository extends JpaRepository<Prozesslog, Long> {

    // Optional: Prozesse nach Name filtern
    List<Prozesslog> findByProzessName(String prozessName);

    // Optional: Letzter Eintrag zu einem Prozess
    Prozesslog findTopByProzessNameOrderByZeitstempelDesc(String prozessName);
}