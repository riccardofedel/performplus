package com.finconsgroup.performplus.service.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.finconsgroup.performplus.enumeration.TipoNodo;

public class RisorsaUmanaPianoReport {
	private NodoPianoDTO nodo;
	private RisorsaUmanaDTO risorsa;
	private String nomeNodo;
	private String nomeRisorsa;
	private String organizzazioni;
	private TipoNodo tipoNodo;

	public RisorsaUmanaPianoReport(NodoPianoDTO np, RisorsaUmanaNodoPianoDTO r) {
		this(np);
		this.risorsa = r.getRisorsaUmana();
		nomeRisorsa = risorsa.getCognomeNome();
	}

	public RisorsaUmanaPianoReport(NodoPianoDTO np) {
		this.nodo = np;
		this.tipoNodo = np.getTipoNodo();
		nomeNodo = nodo.getNomeCompleto();
	}

	public NodoPianoDTO getNodo() {
		return nodo;
	}

	public void setNodo(NodoPianoDTO nodo) {
		this.nodo = nodo;
	}

	public RisorsaUmanaDTO getRisorsa() {
		return risorsa;
	}

	public void setRisorsa(RisorsaUmanaDTO risorsa) {
		this.risorsa = risorsa;
	}

	public String getNomeNodo() {
		return nomeNodo;
	}

	public void setNomeNodo(String nomeNodo) {
		this.nomeNodo = nomeNodo;
	}

	public String getNomeRisorsa() {
		return nomeRisorsa;
	}

	public void setNomeRisorsa(String nomeRisorsa) {
		this.nomeRisorsa = nomeRisorsa;
	}

	public RisorsaUmanaPianoReport organizzazioni(List<OrganizzazioneEasyDTO> items) {
		if (items == null || items.isEmpty())
			return null;
		List<String> list = new ArrayList<>();
		for (OrganizzazioneEasyDTO o : items) {
			String desc = o.getNomeCompletoDaLivello();
			if (!list.contains(desc))
				list.add(desc);
		}
		list.sort(Comparator.naturalOrder());
		StringBuilder sb = new StringBuilder();
		String virgola = "";
		for (String desc : list) {
			sb.append(virgola).append(desc);
			virgola = ", ";
		}
		setOrganizzazioni( sb.toString());
		return this;
	}

	public String getOrganizzazioni() {
		return organizzazioni;
	}

	public void setOrganizzazioni(String organizzazioni) {
		this.organizzazioni = organizzazioni;
	}

	public TipoNodo getTipoNodo() {
		return tipoNodo;
	}

	public void setTipoNodo(TipoNodo tipoNodo) {
		this.tipoNodo = tipoNodo;
	}

}
