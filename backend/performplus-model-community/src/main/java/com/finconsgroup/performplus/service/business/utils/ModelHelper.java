package com.finconsgroup.performplus.service.business.utils;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class ModelHelper {
	private ModelHelper() {
	}

	public static String toName(Enum<?> tipo) {
		if (tipo == null)
			return null;
		return toName(tipo.name());
	}

	public static String toName(String name) {
		if (name == null || name.trim().length() == 0)
			return name;
		char[] cc = name.toCharArray();
		boolean toUpper = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cc.length; i++) {
			if (cc[i] == '_') {
				toUpper = true;
				continue;
			}
			if (toUpper)
				sb.append(Character.toUpperCase(cc[i]));
			else
				sb.append(Character.toLowerCase(cc[i]));
			toUpper = false;
		}
		return sb.toString();
	}

	public static String toTitle(String name) {
		if (name == null || name.trim().length() == 0)
			return name;
		char[] cc = name.toCharArray();
		boolean toUpper = true;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cc.length; i++) {
			if (cc[i] == '_') {
				sb.append(' ');
				toUpper = true;
				continue;
			}
			if (toUpper)
				sb.append(Character.toUpperCase(cc[i]));
			else
				sb.append(Character.toLowerCase(cc[i]));
			toUpper = false;
		}
		return sb.toString().trim();
	}

	public static Enum<?> successivo(Enum<?> tipo) {
		if (tipo == null)
			return null;
		int ord = tipo.ordinal() + 1;
		Enum[] items = tipo.getClass().getEnumConstants();
		for (Enum item : items) {
			if (item.ordinal() == ord)
				return item;
		}
		return null;

	}

	public static Enum<?> int2Enum(Integer order, Class<? extends Enum> e) {
		int ord = order == null ? 0 : order;
		if (ord < 0 || e == null)
			return null;
		Enum[] items = e.getEnumConstants();
		for (Enum item : items) {
			if (item.ordinal() == ord)
				return item;
		}
		return null;

	}

	public static Enum<?> name2Enum(String name, Class<? extends Enum> e) {
		if (name == null || e == null)
			return null;
		Enum[] items = e.getEnumConstants();
		for (Enum item : items) {
			if (item.name().toUpperCase().equals(name.toUpperCase()))
				return item;
		}
		return null;
	}

	public static String toString(BigDecimal spese) {
		if (spese == null)
			return null;
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.ITALY);
		nf.setMaximumFractionDigits(2);
		return nf.format(spese.doubleValue());
	}

	public static String toString(float perc) {
		NumberFormat nf = NumberFormat.getPercentInstance(Locale.ITALY);
		nf.setMaximumFractionDigits(2);
		return nf.format(perc);
	}

	public static String toString(Double spese) {
		if (spese == null)
			return null;
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.ITALY);
		nf.setMaximumFractionDigits(2);
		return nf.format(spese.doubleValue());
	}

	public static String toStringDec(Double dec) {
		if (dec == null)
			return null;
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
		nf.setMaximumFractionDigits(2);
		return nf.format(dec.doubleValue());
	}

	public static String toStringDec(Double nro, Integer decimali) {
		if (nro == null)
			return null;
		int dec = 0;
		if (decimali != null)
			dec = decimali;
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALY);
		nf.setMaximumFractionDigits(dec);
		return nf.format(nro.doubleValue());
	}

	public static String toString(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(date);
	}
	public static String toString(LocalDate date) {
		if (date == null)
			return null;
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return df.format(date);
	}
	public static Date toDate(Calendar c) {
		if (c == null)
			return null;
		String mese = ModelHelper.riempiZero(c.get(Calendar.MONTH) + 1, 2);
		String giorno = ModelHelper.riempiZero(c.get(Calendar.DAY_OF_MONTH), 2);
		return java.sql.Date.valueOf(c.get(Calendar.YEAR) + "-" + mese + "-" + giorno);
	}

	public static LocalDate toDate(LocalDate dt) {
		if (dt == null)
			return null;
		return dt.plusDays(0);
	}

	public static LocalDate succDateInAnno(LocalDate dt) {
		if (dt == null)
			return null;
		LocalDate date = succDate(dt);
		if (cambiatoAnno(date, dt))
			return toDate(dt);
		return date;
	}

	public static LocalDate succDate(LocalDate dt) {
		if (dt == null)
			return null;
		return dt.plusDays(1);
	}

	public static LocalDate precDate(LocalDate dt) {
		if (dt == null)
			return null;
		return dt.minusDays(1);
	}

	public static boolean cambiatoAnno(LocalDate date, LocalDate scadenza) {
		return getAnno(date) > getAnno(scadenza);
	}

	public static int getAnno(LocalDate date) {
		if (date == null)
			return 0;
		return date.getYear();

	}

	public static LocalDate precAnno(LocalDate date) {
		if (date == null)
			return null;
		return date.minusYears(1);
	}

	public static LocalDate succAnno(LocalDate date) {
		if (date == null)
			return null;
		return date.plusYears(1);
	}

	public static int getMese(LocalDate date) {
		return date.getMonthValue();
	}

	public static String toString(Boolean b) {
		if (b == null || !b)
			return "no";
		return "sì";
	}

	public static String spaceless(String name) {
		if (StringUtils.isBlank(name))
			return name;
		return name.replace(' ', '_');

	}
	public static String undescoreless(String name) {
		if (StringUtils.isBlank(name))
			return name;
		return name.replace('_', ' ');

	}
	public static int delta(double d, double c) {
		return Double.valueOf(d * 100d - c * 100d).intValue();
	}

	public static String riempiZero(long codice, int lunghezza) {

		return riempiZero(Long.toString(codice), lunghezza);

	}

	public static String riempiZero(int codice, int lunghezza) {

		return riempiZero(Integer.toString(codice), lunghezza);

	}

	public static String riempiZero(String codice, int lunghezza) {

		if (lunghezza < 1) {
			return null;
		}
		return StringUtils.leftPad(codice == null ? "" : codice, lunghezza, '0');
	}

	public static LocalDate succGiorni(LocalDate dt, int giorni) {
		if (dt == null)
			return null;
		return dt.plusDays(giorni);
	}

	public static String normalize(String name) {
		return undescoreless(StringUtils.capitalize(name.toLowerCase()));
	}

	public static String normalize(Enum<?> e) {
		if(e==null)return "";
		return normalize(e.name());
	}


}