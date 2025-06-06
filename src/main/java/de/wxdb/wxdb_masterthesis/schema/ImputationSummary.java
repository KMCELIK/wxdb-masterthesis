package de.wxdb.wxdb_masterthesis.schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "imputation_zusammenfassung")
public class ImputationSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime createdAt = LocalDateTime.now();

	private String triggeredBy;

	@Lob
	private String information;

	@OneToMany(mappedBy = "zusammenfassung", cascade = CascadeType.ALL)
	private List<ImputationLogPojo> logs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public List<ImputationLogPojo> getLogs() {
		return logs;
	}

	public void setLogs(List<ImputationLogPojo> logs) {
		this.logs = logs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, id, information, logs, triggeredBy);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImputationSummary other = (ImputationSummary) obj;
		return Objects.equals(createdAt, other.createdAt) && Objects.equals(id, other.id)
				&& Objects.equals(information, other.information) && Objects.equals(logs, other.logs)
				&& Objects.equals(triggeredBy, other.triggeredBy);
	}

	@Override
	public String toString() {
		return "ImputationSummary [id=" + id + ", createdAt=" + createdAt + ", triggeredBy=" + triggeredBy
				+ ", information=" + information + ", logs=" + logs + "]";
	}

}
