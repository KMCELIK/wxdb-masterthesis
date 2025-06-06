package de.wxdb.wxdb_masterthesis.schema;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "imputation_log")
public class ImputationLogPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String wertName;

	private Double wert;

	private Integer sourceStationId;

	private String sourceName;

	@Lob
	private String information;

	private LocalDateTime createdAt = LocalDateTime.now();

	private String triggeredBy;

	@ManyToOne
	@JoinColumn(name = "imputation_zusammenfassung_id")
	private ImputationSummary zusammenfassung;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWertName() {
		return wertName;
	}

	public void setWertName(String wertName) {
		this.wertName = wertName;
	}

	public Double getWert() {
		return wert;
	}

	public void setWert(Double wert) {
		this.wert = wert;
	}

	public Integer getSourceStationId() {
		return sourceStationId;
	}

	public void setSourceStationId(Integer sourceStationId) {
		this.sourceStationId = sourceStationId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getTriggeredBy() {
		return triggeredBy;
	}

	public void setTriggeredBy(String triggeredBy) {
		this.triggeredBy = triggeredBy;
	}

	public ImputationSummary getZusammenfassung() {
		return zusammenfassung;
	}

	public void setZusammenfassung(ImputationSummary zusammenfassung) {
		this.zusammenfassung = zusammenfassung;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, id, information, sourceName, sourceStationId, triggeredBy, wert, wertName,
				zusammenfassung);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImputationLogPojo other = (ImputationLogPojo) obj;
		return Objects.equals(createdAt, other.createdAt) && Objects.equals(id, other.id)
				&& Objects.equals(information, other.information) && Objects.equals(sourceName, other.sourceName)
				&& Objects.equals(sourceStationId, other.sourceStationId)
				&& Objects.equals(triggeredBy, other.triggeredBy) && Objects.equals(wert, other.wert)
				&& Objects.equals(wertName, other.wertName) && Objects.equals(zusammenfassung, other.zusammenfassung);
	}

	@Override
	public String toString() {
		return "ImputationLogPojo [id=" + id + ", wertName=" + wertName + ", wert=" + wert + ", sourceStationId="
				+ sourceStationId + ", sourceName=" + sourceName + ", information=" + information + ", createdAt="
				+ createdAt + ", triggeredBy=" + triggeredBy + ", zusammenfassung=" + zusammenfassung + "]";
	}

}
