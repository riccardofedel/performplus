package com.finconsgroup.performplus.pi.utils;

import java.lang.RuntimeException;

import org.apache.commons.lang3.StringUtils;

import com.finconsgroup.performplus.domain.RisorsaUmana;

public class CalcoloValutazioneHelper {

	public static String calcolaCodiceUnivoco(RisorsaUmana v) {
		if(v==null) throw new RuntimeException("RisorsaUmana null");
		if(StringUtils.isNotBlank(v.getCodiceFiscale()))
			return v.getCodiceFiscale();
		return (v.getCognome()+"/"+v.getNome()+"/"+v.getIdEnte()+"/"+v.getPolitico()).toUpperCase();
	}


}
