package com.finconsgroup.performplus.rest.api.vm;
import com.finconsgroup.performplus.enumeration.Ruolo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class UtenteFlatVM extends EntityVM{

	private String username;
	private String nome;
	private Ruolo ruolo;
	private Long idEnte=0l;
	private Long struttura;
	private Long risorsa;
	private String codiceFiscale;
	
	private String categoria;
	private String cognomeNome;
	private String matricola;

	private Ruolo ruoloAggiunto;
	private Long strutturaAggiunta;

	public UtenteFlatVM codiceFiscale(String codiceFiscale) {
		this.codiceFiscale=codiceFiscale;
		return this;
	}
	public UtenteFlatVM username(String username) {
		this.username=username;
		return this;
	}
}