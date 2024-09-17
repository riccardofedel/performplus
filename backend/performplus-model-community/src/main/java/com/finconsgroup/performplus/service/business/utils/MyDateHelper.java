package com.finconsgroup.performplus.service.business.utils;
import java.time.LocalDate;
import java.util.Date;

public class MyDateHelper extends DateHelper {
	public static LocalDate now() {
		return LocalDate.now();
	}

	public static int getMese() {
		return ModelHelper.getMese(MyDateHelper.now());
	}

	public static int getAnno() {
		return ModelHelper.getAnno(MyDateHelper.now());
	}
}