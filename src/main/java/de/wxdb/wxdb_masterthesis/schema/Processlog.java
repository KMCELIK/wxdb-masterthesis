package de.wxdb.wxdb_masterthesis.schema;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "prozess_log")
public class Processlog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "prozess_name", nullable = false, length = 255)
	private String prozessName;

	@Column(nullable = false, length = 50)
	private String status; // z. B. STARTED, SUCCESS, ERROR

	@Lob
	@Column(name = "beschreibung")
	private String beschreibung; // optional, bei Fehlern

	@Column(nullable = false)
	private LocalDateTime zeitstempel;

	// Konstruktoren
	public Processlog() {
		this.zeitstempel = LocalDateTime.now();
	}

	public Processlog(String prozessName, String status, String beschreibung) {
		this.prozessName = prozessName;
		this.status = status;
		this.beschreibung = beschreibung;
		this.zeitstempel = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getProzessName() {
		return prozessName;
	}

	public void setProzessName(String prozessName) {
		this.prozessName = prozessName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public LocalDateTime getZeitstempel() {
		return zeitstempel;
	}

	public void setZeitstempel(LocalDateTime zeitstempel) {
		this.zeitstempel = zeitstempel;
	}
}
