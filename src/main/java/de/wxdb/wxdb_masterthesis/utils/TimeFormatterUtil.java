package de.wxdb.wxdb_masterthesis.utils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Development Kit für Formatter Methoden, um LocalDate oder andere Zeitklassen
 * zu transformieren.
 * 
 * @author Kaan Mustafa Celik
 */
public class TimeFormatterUtil {

	private static final Logger log = LoggerFactory.getLogger(TimeFormatterUtil.class);
	private static final DateTimeFormatter ISO_INSTANT_FORMATTER = DateTimeFormatter.ISO_INSTANT;

	/**
	 * Wandelt ein LocalDate in einen UTC-Zeitstempel im Format
	 * yyyy-MM-dd'T'00:00:00Z um. Falls das Datum null ist, wird das aktuelle Datum
	 * verwendet.
	 *
	 * @param date das zu formatierende Datum
	 * @return formatierter UTC-Zeitstempel als String. Bei einem Fehlerfall wird ein leerer String zurückgegeben.
	 */
	public static String formatAsUtcIso(LocalDate date) {
		if (date == null) {
			log.warn("LocalDate war null. Rückgabe eines leeren Strings.");
			return "";
		}

		try {
			return date.atStartOfDay().atZone(ZoneOffset.UTC).format(ISO_INSTANT_FORMATTER);
		} catch (Exception e) {
			log.error("[TimeFormatterUtil] - [Fehler beim Formatieren des Datums: {}]", date, e);
			return "";
		}
	}
}
