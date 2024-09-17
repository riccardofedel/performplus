package com.finconsgroup.performplus.service.business.utils;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateHelper {
	protected DateHelper() {
	}

	public static final LocalDate MIN = LocalDate.of(0001,01,01);
	public static final LocalDate MAX = LocalDate.of(9999,12,31);

	public static LocalDate now() {
		return LocalDate.now();
	}

	public static int getMese() {
		return ModelHelper.getMese(now());
	}

	public static int getAnno() {
		return ModelHelper.getAnno(now());
	}

	public static LocalDate max() {
		return MAX;
	}

	public static LocalDate inizioAnno(int anno) {
		return LocalDate.of(anno,01,01);
	}

	public static LocalDate fineAnno(int anno) {
		return LocalDate.of(anno,12,31);
	}

	public static Date asDate(LocalDate localDate) {
		if (localDate == null)
			return null;
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate asLocalDate(Date date) {
		if (date == null)
			return null;
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date asDate(LocalDateTime localDateTime) {
		if (localDateTime == null)
			return null;
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		if (date == null)
			return null;
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static String toString(Date date) {
		if(date==null)
		return null;
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	public static String toString(LocalDate date) {
		if(date==null)
		return null;
		return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date);
	}
	public static boolean before(Date a, Date b) {
		return asLocalDate(a).isBefore(asLocalDate(b));
	}
	public static boolean after(Date a, Date b) {
		return asLocalDate(a).isAfter(asLocalDate(b));
	}

	public static LocalDate inizioAnnoInCorso() {
		return inizioAnno(getAnno());
	}

}
