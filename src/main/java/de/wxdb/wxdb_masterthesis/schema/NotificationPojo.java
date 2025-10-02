package de.wxdb.wxdb_masterthesis.schema;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * POJO-Klasse für die Hinweise Tabelle.
 * 
 * @author Kaan Mustafa Celik
 */
@Entity
@Table(name = "hinweise")
public class NotificationPojo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hinweisid;

	@Column(nullable = false)
	private LocalDateTime zeitstempel;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String beschreibung;

	@Column(length = 50, nullable = false)
	private String typ;

	// Default Konstruktor
	public NotificationPojo() {
	}

	// Konstruktor mit Feldern
	public NotificationPojo(LocalDateTime zeitstempel, String beschreibung, String typ) {
	        this.zeitstempel = zeitstempel;
	        this.beschreibung = beschreibung;
	        this.typ = typ;
	    }

	// Getter und Setter
	public Integer getHinweisid() {
		return hinweisid;
	}

	public void setHinweisid(Integer hinweisid) {
		this.hinweisid = hinweisid;
	}

	public LocalDateTime getZeitstempel() {
		return zeitstempel;
	}

	public void setZeitstempel(LocalDateTime zeitstempel) {
		this.zeitstempel = zeitstempel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}
}
