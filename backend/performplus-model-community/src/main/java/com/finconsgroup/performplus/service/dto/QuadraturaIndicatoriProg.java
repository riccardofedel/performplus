package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.StatoIndicatore;
import com.finconsgroup.performplus.enumeration.TipoIndicatore;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class QuadraturaIndicatoriProg implements Serializable,
		Comparable<QuadraturaIndicatoriProg> {
	private Long idNodoPiano;
	private Long idIndicatorePiano;
	private Long idIndicatore;
	private String codice;
	private String descNodoPiano; // tipo, codice, denominazione
	private LocalDate inizioValidita;
	private LocalDate fineValidita;
	private String aree;
	private String responsabili;
	private String categorieStakeholders;
	private String stakeholders;
	private String organizzazione;
	private String organizzazioni;

	private Integer priorita; // (%),
	private String indicatore;
	private String preventivo1;
	private String preventivo2;
	private String preventivo3;
	private String preventivo4;
	private String preventivo5;
	private String preventivo6;
	
	private String target;
	private String storico;

	private Integer anno;

	private Integer peso;
	private Integer pesoRelativo;
	private StatoIndicatore stato;
	private String prefix;
	private String nome;
	private int gruppo = -1;

	private LocalDate scadenzaIndicatore;
	private String scadenzaIndicatoreSt;
	private String tipoIndicatore;
	
	public QuadraturaIndicatoriProg(NodoPianoDTO nodo) {
		super();
		setDescNodoPiano(nodo.getDenominazione());
		setNome(nodo.getDenominazione());
		setIdNodoPiano(nodo.getId());
		setResponsabili(NodoPianoHelper.responsabili(nodo));
	}


	public QuadraturaIndicatoriProg(IndicatorePianoDTO indicatore) {
		super();
		setIdNodoPiano(indicatore.getNodoPiano().getId());
		setPrefix("");
		setIdIndicatore(indicatore.getIndicatore().getId());
		setIdIndicatorePiano(indicatore.getId());
		setNome(getIndicatore());
	}



	public QuadraturaIndicatoriProg() {
		super();
	}

	
	@Override
	public int compareTo(QuadraturaIndicatoriProg o) {
		if (o == null)
			return -1;
		String a = getDescNodoPiano();
		String b = o.getDescNodoPiano();
		int comp = (a == null ? "" : a).compareTo(b == null ? "" : b);
		if (comp != 0)
			return comp;
		a = getIndicatore();
		b = o.getIndicatore();
		comp = (a == null ? "" : a).compareTo(b == null ? "" : b);
		if (comp != 0)
			return comp;
		int p1 = peso == null ? 0 : peso;
		int p2 = o.getPeso() == null ? 0 : o.getPeso();
		return p1 - p2;
	}


	
}
