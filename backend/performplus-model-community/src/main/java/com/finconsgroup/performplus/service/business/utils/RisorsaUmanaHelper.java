package com.finconsgroup.performplus.service.business.utils;

import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.TipoFunzione;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaDTO;

public class RisorsaUmanaHelper {

	
	public static String getNomeCognome(final RisorsaUmana risorsaUmana) {
		if(risorsaUmana==null)return "";
		return (risorsaUmana.getNome() == null ? "" : risorsaUmana.getNome()) + " "
				+ (risorsaUmana.getCognome() == null ? "" : risorsaUmana.getCognome());
	}

	
	public static String getCognomeNome(final RisorsaUmana risorsaUmana) {
        if(risorsaUmana==null)return "";
		return (risorsaUmana.getCognome() == null ? "" : risorsaUmana.getCognome()) + " "
		+ (risorsaUmana.getNome() == null ? "" : risorsaUmana.getNome());
	}

	public static String getCognomeNomeMatricola(final RisorsaUmana risorsaUmana) {
        if(risorsaUmana==null)return "";
		return (risorsaUmana.getCognome() == null ? "" : risorsaUmana.getCognome()) + " "
		+ (risorsaUmana.getNome() == null ? "" : risorsaUmana.getNome())
		+ (risorsaUmana.getCodiceInterno() == null ? "" : " ["+risorsaUmana.getCodiceInterno()+"]");
	}
	
	public static String getNomePolitico(final RisorsaUmana risorsaUmana) {
		return (risorsaUmana.getCognome() == null ? "" : risorsaUmana.getCognome()) + " "
				+ (risorsaUmana.getNome() == null ? "" : risorsaUmana.getNome()) + " "
				+ (risorsaUmana.getFunzione() == null ? "" : risorsaUmana.getFunzione().name()) + " "
				+ (risorsaUmana.getDelega() == null ? "" : risorsaUmana.getDelega());
	}

	
	public static String getNomeAssessore(final RisorsaUmana risorsaUmana) {
		return (risorsaUmana.getCognome() == null ? "" : risorsaUmana.getCognome()) + " "
				+ (risorsaUmana.getNome() == null ? "" : risorsaUmana.getNome())
				+ (risorsaUmana.getDelega() == null ? "" : " - " + risorsaUmana.getDelega());
	}

	
	public static String getNomeAssessorato(final RisorsaUmana risorsaUmana) {
		return (risorsaUmana.getDelega() == null ? "" : risorsaUmana.getDelega() + " - ")
				+ (risorsaUmana.getCognome() == null ? "" : risorsaUmana.getCognome()) + " "
				+ (risorsaUmana.getNome() == null ? "" : risorsaUmana.getNome())
				+ (TipoFunzione.SINDACO.equals(risorsaUmana.getFunzione())?" - Sindaco":"");
	}


	public static String getCognomeNomeMatricola(RisorsaUmanaDTO risorsaUmana) {
        if(risorsaUmana==null)return "";
		return (risorsaUmana.getCognome() == null ? "" : risorsaUmana.getCognome()) + " "
		+ (risorsaUmana.getNome() == null ? "" : risorsaUmana.getNome())
		+ (risorsaUmana.getCodiceInterno() == null ? "" : " ["+risorsaUmana.getCodiceInterno()+"]");
	}
}
