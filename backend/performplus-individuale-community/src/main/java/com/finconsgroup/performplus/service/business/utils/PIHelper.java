package com.finconsgroup.performplus.service.business.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.enumeration.TipoFunzione;
import com.finconsgroup.performplus.service.dto.ProfiloDTO;
import com.finconsgroup.performplus.service.dto.UtenteDTO;



public class PIHelper {

	public static Boolean isOIV(UtenteDTO user){
		if(user==null||user.getProfili()==null)return false;
		for (ProfiloDTO p : user.getProfili()) {
				if(p.getRuolo().equals(Ruolo.OIV)){
					return true;
				}
		}
		return false;
	}
	public static Boolean isRisorsa(UtenteDTO user){
		if(user==null||user.getProfili()==null||user.getRisorsaUmana()==null)return false;
		for (ProfiloDTO p : user.getProfili()) {
				if(p.getRuolo().equals(Ruolo.RISORSA)||p.getRuolo().equals(Ruolo.REFERENTE)){
					return true;
				}
				
		}
		return false;
	}
	public static Boolean isResponsabile(UtenteDTO user){
		if(user==null||user.getProfili()==null||user.getRisorsaUmana()==null)return false;
		for (ProfiloDTO p : user.getProfili()) {
				if(p.getRuolo().equals(Ruolo.DIRETTORE_GENERALE)||p.getRuolo().equals(Ruolo.POSIZIONE_ORGANIZZATIVA)){
					return true;
				}
				
		}
		return false;
	}
	public static Boolean isPosizioneOrganizzativa(UtenteDTO user){
		if(user==null||user.getProfili()==null)return false;
		for (ProfiloDTO p : user.getProfili()) {
				if(p.getRuolo().equals(Ruolo.POSIZIONE_ORGANIZZATIVA)){
					return true;
				}
		}
		return false;
	}
	public static Boolean isPresidente(UtenteDTO user) {
		if(user==null||user.getProfili()==null||user.getRisorsaUmana()==null)return false;
		if(TipoFunzione.PRESIDENTE.equals(user.getRisorsaUmana().getFunzione()))return true;
		return false;
	}
	public static double perc(BigDecimal target,int anno) {
		if(target==null){
			return 0.0;
		} 
		return perc(target.doubleValue(),anno);
	}
	public static double percOld(Double target) {
		if(target==null){
			return 0.0;
		} 
		if(target<70.0){
				return 0.0;
		}
		if(target<99.0){
				return 0.6;
		}
		return 1.0;
	}
	/*
	 Modifica della scala di valutazione per il parziale raggiungimento degli obiettivi:
	 PR1 = moltiplicatore 0,6 per un valore compreso tra il 60% e il 70% del valore target;
	 PR2 = moltiplicatore 0,8 per un valore compreso tra il 70,01% e il 90% del valore target;
	 PR3 = moltiplicatore 0,9 per un valore compreso tra il 90,01% e il 99% del valore target.
	 */
	public static double perc(Double target, int anno) {
		if(anno>=2023) {
			return perc2023(target);
		}
		if(target==null){
			return 0.0;
		} 
		if(target<60.0){
				return 0.0;
		}
		if(target<=70.0){
			return 0.6;
		}
		if(target<=90.0){
			return 0.8;
		}

		if(target<=99.0){
				return 0.9;
		}
		return 1.0;
	}
	public static double perc2023(Double target) {
		if(target==null){
			return 0.0;
		} 
		if(target<60.0){
				return 0.0;
		}

		if(target<=99.0){
			double d= BigDecimal.valueOf(target).divide(BigDecimal.valueOf(100d),2,RoundingMode.HALF_UP).doubleValue();
			return Math.min(d, 1.0);
		}
		return 1.0;
	}
}
