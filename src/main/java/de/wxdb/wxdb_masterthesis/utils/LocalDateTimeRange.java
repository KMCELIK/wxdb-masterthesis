package de.wxdb.wxdb_masterthesis.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Utils Klasse um einen Zeitraum zu bestimmen. - Nötig zur Verarbeitung von
 * nicht validen Datensätzen.
 * 
 * @author Kaan Mustafa Celik
 */
public class LocalDateTimeRange {
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	public LocalDateTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "LocalDateTimeRange{" + "startDate=" + startDate + ", endDate=" + endDate + '}';
	}

	/**
	 * Methodik um zu große abstände bei einer sortierten Liste ausfindig zu machen
	 * und in eine Liste von Zeiträumen zu mappen.
	 * 
	 * @param maxGapInHours    maximaler Abstand in Stunden
	 * @param sortedTimestamps
	 * @return
	 */
	public static List<LocalDateTimeRange> groupToBroadRanges(List<LocalDateTime> timestamps, int maxGapInHours) {
		List<LocalDateTimeRange> ranges = new ArrayList<>();
		if (timestamps == null || timestamps.isEmpty())
			return ranges;

		// Safety: Sortiere die Liste
		List<LocalDateTime> sortedTimestamps = new ArrayList<>(timestamps);
		sortedTimestamps.sort(Comparator.naturalOrder());

		if (maxGapInHours <= 0) {
			maxGapInHours = 50;
		}
		Duration maxGap = Duration.ofHours(maxGapInHours);

		LocalDateTime start = sortedTimestamps.get(0);
		LocalDateTime previous = start;

		for (int i = 1; i < sortedTimestamps.size(); i++) {
			LocalDateTime current = sortedTimestamps.get(i);

			if (Duration.between(previous, current).compareTo(maxGap) > 0) {
				// Nur hinzufügen, wenn Range korrekt ist
				if (!start.isAfter(previous)) {
					ranges.add(new LocalDateTimeRange(start, previous));
				}
				start = current;
			}
			previous = current;
		}

		// Finaler Bereich
		if (!start.isAfter(previous)) {
			ranges.add(new LocalDateTimeRange(start, previous));
		}

		return ranges;
	}

}
