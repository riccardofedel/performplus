package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.Over;

import lombok.Data;

@SuppressWarnings("serial")
@Data

public class QuadraturaRisorsaStrumentale implements Serializable, Comparable<QuadraturaRisorsaStrumentale> {
	private Long idOrganizzazione;
	private Long idRisorsaStrumentale;
	private String descRisorsaStrumentale;
	private String descTipologia;
	private String descConservazione;
	private String descOrganizzazione;
	private String codiceOrganizzazione;
	private Disponibile disponibile;
	private Over over;
	private String avvertimento;
	private double disponibilitaTotale;
	private List<String> orgs = new ArrayList<>();
	private Integer disponibilita;
	private Integer quantita;
	private BigDecimal valore;
	private BigDecimal valoreTotale;

	private String prefix;
	private String nome;
	private RisorsaStrumentaleDTO risorsa;
	private int gruppo;

	private OrganizzazioneDTO organizzazione;

	public QuadraturaRisorsaStrumentale(RisorsaStrumentaleOrganizzazioneDTO risorsa, int oreSettimanaliMax) {
		idOrganizzazione = risorsa.getOrganizzazione().getId();
		organizzazione = risorsa.getOrganizzazione();
		idRisorsaStrumentale = risorsa.getRisorsaStrumentale().getId();
		descTipologia = risorsa.getRisorsaStrumentale().getTipologia().getDescrizione();
		disponibile = risorsa.getRisorsaStrumentale().getDisponibile();
		descRisorsaStrumentale = Disponibile.DISPONIBILE != disponibile ? "Da acquisire [" + idRisorsaStrumentale + "]"
				: risorsa.getRisorsaStrumentale().getDescrizione();
		quantita = risorsa.getRisorsaStrumentale().getQuantita();
		valore = risorsa.getRisorsaStrumentale().getValore();
		valoreTotale = risorsa.getRisorsaStrumentale().getValoreTotale();
		disponibilita = risorsa.getDisponibilita() == null ? 0 : risorsa.getDisponibilita();
		descOrganizzazione = risorsa.getOrganizzazione().getIntestazione();
		nome = risorsa.getOrganizzazione().getIntestazione();

	}

	public QuadraturaRisorsaStrumentale(RisorsaStrumentaleDTO risorsaStrumentale) {
		idRisorsaStrumentale = risorsaStrumentale.getId();
		risorsa = risorsaStrumentale;
		descTipologia = risorsaStrumentale.getTipologia().getDescrizione();
		disponibile = risorsaStrumentale.getDisponibile();
		descRisorsaStrumentale = Disponibile.DISPONIBILE != disponibile ? "Da acquisire [" + idRisorsaStrumentale + "]"
				: risorsaStrumentale.getDescrizione();
		quantita = risorsaStrumentale.getQuantita();
		valore = risorsaStrumentale.getValore();
		valoreTotale = risorsaStrumentale.getValoreTotale();
		disponibilita = 0;
		disponibilitaTotale = 0;
	}

	public QuadraturaRisorsaStrumentale(OrganizzazioneDTO figlio, RisorsaStrumentaleOrganizzazioneDTO elem) {
		this(elem.getRisorsaStrumentale());
		idOrganizzazione = figlio.getId();
		organizzazione = figlio;
		descOrganizzazione = figlio.getIntestazione();
		nome = figlio.getIntestazione();
	}

	@Override
	public int compareTo(QuadraturaRisorsaStrumentale o) {
		if (o == null)
			return -1;

		int comp = confrontaCodici(getRisorsa().getCodice(), o.getRisorsa().getCodice());
		if (comp != 0)
			return comp;

		comp = confrontaCodici(getOrganizzazione().getCodice(), o.getOrganizzazione().getCodice());
		return comp;
	}

	private int confrontaCodici(String a, String b) {
		int comp = (a == null ? "" : a).compareTo(b == null ? "" : b);
		return comp;
	}
	public boolean isOrganizzazione() {
		return idOrganizzazione != null;
	}
	public String orgs2String() {
		final StringJoiner joiner = new StringJoiner(", ");
		getOrgs().forEach(a -> joiner.add(a));
		return joiner.toString();

	}
}
