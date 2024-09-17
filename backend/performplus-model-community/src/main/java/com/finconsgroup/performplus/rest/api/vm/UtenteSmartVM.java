package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.enumeration.Ruolo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class UtenteSmartVM extends EntityVM {

	private String username;
	private String nome;
	private boolean admin;
	private Ruolo ruolo;
	private String ruoloLabel;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private Long idEnte;
	private String codiceFiscale;
	private String struttura;

	private Ruolo ruoloAggiunto;
	private String ruoloAggiuntoLabel;
	private String strutturaAggiunta;
	
	private String cognomeNome;
	private String matricola;

	public UtenteSmartVM codiceFiscale(String codiceFiscale) {
		this.codiceFiscale=codiceFiscale;
		return this;
	}
	
	public UtenteSmartVM username(String username) {
		this.username=username;
		return this;
	}
}