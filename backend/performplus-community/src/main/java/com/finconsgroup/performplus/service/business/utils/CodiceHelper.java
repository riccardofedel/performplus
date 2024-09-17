package com.finconsgroup.performplus.service.business.utils;

import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodiceHelper {
	private static Logger logger = LoggerFactory.getLogger(CodiceHelper.class);

	private CodiceHelper() {}
	
	public static String successivo(String max) {
		return successivo(max, 3);
	}

	public static String successivo(String max, int len) {

		int code = 0;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(len);
		nf.setMaximumFractionDigits(0);
		nf.setGroupingUsed(false);
		Number num = null;
		try {
			if (max != null)
				num = nf.parse(max);
			if (num != null)
				code = num.intValue();
			code++;
			return nf.format(code);
		} catch (Exception e) {
			logger.error("successivo", e);
		}
		return null;
	}

	public static String toCodice(int nro, int len) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(len);
		nf.setMaximumFractionDigits(0);
		nf.setGroupingUsed(false);
		try {
			return nf.format(nro);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
