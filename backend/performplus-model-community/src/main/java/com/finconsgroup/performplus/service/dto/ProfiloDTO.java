package com.finconsgroup.performplus.service.dto;
import com.finconsgroup.performplus.enumeration.Ruolo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ProfiloDTO extends EntityDTO implements EnteInterface, AnnoInterface{
    private OrganizzazioneDTO organizzazione;
    private Ruolo ruolo;
    private UtenteEasyDTO utente;
    private Long idEnte=0l;
    private Integer anno;
	private RisorsaUmanaDTO risorsaUmana;
	private String risorsaUmanaString;
	private Boolean aggiunto=false;

}