package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.Over;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.RisorsaUmanaHelper;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class QuadraturaRisorseProg implements Serializable, Comparable<QuadraturaRisorseProg> {
	private Long idRisorsaUmana;
	private String descRisorsaUmana;
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
	private String codiceCompletoNodoPiano;
	private String descNodoPiano;
	private String prefix;
	private String nome;
	private String organizzazioni;
	private int oreSettimanaliMax;
	private RisorsaUmanaSmartVM risorsa;
	private String organizzazione;
	private int gruppo;
	boolean utilizzato;




	public QuadraturaRisorseProg(RisorsaUmana risorsaUmana, int oreSettimanaliMax) {
		idRisorsaUmana = risorsaUmana.getId();
		risorsa = Mapping.mapping(risorsaUmana, RisorsaUmanaSmartVM.class);
		esterna = risorsaUmana.getEsterna();
		disponibile = risorsaUmana.getDisponibile();
		descRisorsaUmana = RisorsaUmanaHelper.getCognomeNome(risorsaUmana);
		parttime = risorsaUmana.getPartTime();
		mesi = risorsaUmana.getMesi() == null ? 12 : risorsaUmana.getMesi();
		disponibilita = 0;
		disponibilitaTotale = 0;
		oreParttime = risorsaUmana.getOrePartTime() == null ? 0 : risorsaUmana.getOrePartTime();
		this.oreSettimanaliMax = oreSettimanaliMax;
		nome = RisorsaUmanaHelper.getCognomeNomeMatricola(risorsaUmana);
	}


	public QuadraturaRisorseProg(NodoPiano np, RisorsaUmana ris, int oreSettimanaliMax) {
		this(ris, oreSettimanaliMax);
		idNodoPiano = np.getId();
		descNodoPiano = NodoPianoHelper.ridotto(np.getCodiceCompleto())+" "+np.getDenominazione();
		codiceNodoPiano = NodoPianoHelper.ridotto(np.getCodiceCompleto());
		nome = np.getDenominazione();
	}

	@Override
	public int compareTo(QuadraturaRisorseProg o) {
		if (o == null)
			return -1;

		int comp = confrontaCodici(getDescRisorsaUmana(),o.getDescRisorsaUmana());
		if (comp != 0)
			return comp;

		return confrontaCodici(getCodiceNodoPiano(), o.getCodiceNodoPiano());
	}

	private int confrontaCodici(String a, String b) {
		int comp = (a == null ? "" : a).compareTo(b == null ? "" : b);
		return comp;
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
