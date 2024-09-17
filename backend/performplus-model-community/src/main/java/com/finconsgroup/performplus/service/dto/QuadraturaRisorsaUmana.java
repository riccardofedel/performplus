package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.Over;
import com.finconsgroup.performplus.service.business.utils.RisorsaUmanaHelper;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class QuadraturaRisorsaUmana implements Serializable, Comparable<QuadraturaRisorsaUmana> {
	private Long idOrganizzazione;
	private Long idRisorsaUmana;
	private String descRisorsaUmana;
	private String descOrganizzazione;
	private String codiceOrganizzazione;
	private Boolean esterna;
	private Boolean parttime;
	private Disponibile disponibile;
	private Over over;
	private String avvertimento;
	private Integer mesi;
	private Integer oreParttime;
	private double disponibilitaTotale;
	private List<String> orgs = new ArrayList<>();
	private Integer disponibilita;

	private Long idNodoPiano;
	private String codiceNodoPiano;
	private String descNodoPiano;
	private String prefix;
	private String nome;
	private String organizzazioni;
	private int oreSettimanaliMax;
	private RisorsaUmanaDTO risorsa;
	private OrganizzazioneDTO organizzazione;
	private int gruppo;
	boolean utilizzato;

	public QuadraturaRisorsaUmana(RisorsaUmanaOrganizzazioneDTO risorsa, int oreSettimanaliMax) {
		organizzazione = risorsa.getOrganizzazione();
		idOrganizzazione = risorsa.getOrganizzazione().getId();
		idRisorsaUmana = risorsa.getRisorsaUmana().getId();
		esterna = risorsa.getRisorsaUmana().getEsterna();
		disponibile = risorsa.getRisorsaUmana().getDisponibile();
		descRisorsaUmana = RisorsaUmanaHelper.getCognomeNomeMatricola(risorsa.getRisorsaUmana());
		parttime = risorsa.getRisorsaUmana().getPartTime();
		mesi = risorsa.getRisorsaUmana().getMesi() == null ? 12 : risorsa.getRisorsaUmana().getMesi();
		disponibilita = risorsa.getDisponibilita() == null ? 0 : risorsa.getDisponibilita();
		descOrganizzazione = risorsa.getOrganizzazione().getIntestazione();
		oreParttime = risorsa.getRisorsaUmana().getOrePartTime() == null ? 0
				: risorsa.getRisorsaUmana().getOrePartTime();
		nome = risorsa.getOrganizzazione().getIntestazione();
		this.oreSettimanaliMax = oreSettimanaliMax;

	}

	public QuadraturaRisorsaUmana(RisorsaUmanaNodoPianoDTO risorsa, int oreSettimanaliMax) {
		this(risorsa.getRisorsaUmana(), oreSettimanaliMax);
		idNodoPiano = risorsa.getNodoPiano().getId();
		codiceNodoPiano = risorsa.getNodoPiano().getCodiceRidotto();
		descNodoPiano = risorsa.getNodoPiano().getDenominazione();
		nome = risorsa.getNodoPiano().getDenominazione();
	}

	public QuadraturaRisorsaUmana(RisorsaUmanaDTO risorsaUmana, int oreSettimanaliMax) {
		idRisorsaUmana = risorsaUmana.getId();
		risorsa = risorsaUmana;
		esterna = risorsaUmana.getEsterna();
		disponibile = risorsaUmana.getDisponibile();
		descRisorsaUmana = RisorsaUmanaHelper.getCognomeNomeMatricola(risorsaUmana);
		parttime = risorsaUmana.getPartTime();
		mesi = risorsaUmana.getMesi() == null ? 12 : risorsaUmana.getMesi();
		disponibilita = 0;
		disponibilitaTotale = 0;
		oreParttime = risorsaUmana.getOrePartTime() == null ? 0 : risorsaUmana.getOrePartTime();
		this.oreSettimanaliMax = oreSettimanaliMax;
	}

	public QuadraturaRisorsaUmana(OrganizzazioneDTO figlio, RisorsaUmanaOrganizzazioneDTO elem, int oreSettimanaliMax) {
		this(elem.getRisorsaUmana(), oreSettimanaliMax);
		organizzazione = figlio;
		idOrganizzazione = figlio.getId();
		descOrganizzazione = figlio.getIntestazione();
		nome = figlio.getIntestazione();
	}

	public QuadraturaRisorsaUmana(NodoPianoDTO figlio, RisorsaUmanaNodoPianoDTO elem, int oreSettimanaliMax) {
		this(elem.getRisorsaUmana(), oreSettimanaliMax);
		idNodoPiano = figlio.getId();
		descNodoPiano = figlio.getDenominazione();
		codiceNodoPiano = figlio.getCodiceRidotto();
		nome = figlio.getDenominazione();
	}

	@Override
	public int compareTo(QuadraturaRisorsaUmana o) {
		if (o == null)
			return -1;

		int comp = confrontaCodici(getDescRisorsaUmana() == null ? "" : getDescRisorsaUmana().toUpperCase(),
				o.getDescRisorsaUmana() == null ? "" : o.getDescRisorsaUmana().toUpperCase());
		if (comp != 0)
			return comp;

		comp = confrontaCodici(getCodiceNodoPiano(), o.getCodiceNodoPiano());
		if (comp != 0)
			return comp;

		comp = confrontaCodici(getCodiceOrganizzazione(), o.getCodiceOrganizzazione());
		return comp;
	}

	private int confrontaCodici(String a, String b) {
		int comp = (a == null ? "" : a).compareTo(b == null ? "" : b);
		return comp;
	}

	public boolean isOrganizzazione() {
		return idOrganizzazione != null;
	}

	public boolean isNodoPiano() {
		return idNodoPiano != null;
	}

	public String orgs2String() {
		final StringJoiner joiner = new StringJoiner(", ");
		getOrgs().forEach(a -> joiner.add(a));
		return joiner.toString();

	}

	public float getEffettivaTemporale() {
		if (oreSettimanaliMax == 0)
			return 0;
		int percentualeTempo = 100;
		int mesi = risorsa.getMesi() == null ? 12 : risorsa.getMesi();
		boolean part = risorsa.getPartTime() == null ? false : risorsa.getPartTime();
		if (part) {
			int ore = risorsa.getOrePartTime() == null ? 0 : risorsa.getOrePartTime();
			percentualeTempo = (ore * 100) / oreSettimanaliMax;
		}
		float perc = (float) mesi / (float) 12 * (float) percentualeTempo;
		return perc / (float) 100;
	}

	public float getEffettivaFinale() {
		return getEffettivaTemporale() * (float) getDisponibilitaTotale() / (float) 100;
	}
}
