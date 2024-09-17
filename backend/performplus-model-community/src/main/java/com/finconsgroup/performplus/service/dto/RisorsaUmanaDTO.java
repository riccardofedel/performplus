package com.finconsgroup.performplus.service.dto;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.TipoFunzione;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class RisorsaUmanaDTO extends EntityDTO implements
		Comparable<RisorsaUmanaDTO>, EnteInterface {

	private String nome;
	private String cognome;
	private String codiceInterno;

	private Date dataNascita;

	private Disponibile disponibile;
	private Boolean esterna;
	private Integer mesi;

	private Integer ordine;
	private Integer orePartTime;

	private Boolean partTime;
	private Boolean politico;
	private TipoFunzione funzione;
	private String delega;
	private ImageEntryDTO image;
	private Long idEnte = 0l;
	private Integer anno;

	private List<OrganizzazioneEasyDTO> organizzazioniAppartenenza;
	
	@Override
	public int compareTo(RisorsaUmanaDTO ris) {
		if (Disponibile.DISPONIBILE .equals( this.getDisponibile())) {
			if (Disponibile.DISPONIBILE .equals( ris.getDisponibile())) {
				return this.getCognome().compareTo(ris.getCognome());
			} else {
				return -1;
			}
		} else {
			if (Disponibile.DISPONIBILE .equals( ris.getDisponibile())) {
				return 1;
			} else {
				return ( this.getOrdine()).compareTo((
						ris.getOrdine()));
			}
		}
	}


	@JsonIgnore
	public String getNomeCognome() {
		return (nome == null ? "" : nome) + " "
				+ (cognome == null ? "" : cognome);
	}

	@JsonIgnore
	public String getCognomeNome() {
		return (getCognome() == null ? "" : getCognome()) + " "
		+ (getNome() == null ? "" : getNome())
		+ (getCodiceInterno() == null ? "" : " ["+getCodiceInterno()+"]");
	}

	@JsonIgnore
	public String getNomePolitico() {
		return (cognome == null ? "" : cognome) + " "
				+ (nome == null ? "" : nome) + " "
				+ (funzione == null ? "" : funzione.name()) + " "
				+ (delega == null ? "" : delega);
	}

	@JsonIgnore
	public String getNomeAssessore() {
		return (cognome == null ? "" : cognome) + " "
				+ (nome == null ? "" : nome)
				+ (delega == null ? "" : " - " + delega);
	}

	@JsonIgnore
	public String getNomeAssessorato() {
		return (delega == null ? "" : delega + " - ")
				+ (cognome == null ? "" : cognome) + " "
				+ (nome == null ? "" : nome)
				+ (TipoFunzione.SINDACO.equals(funzione)?" - Sindaco":"");
	}

	
}
