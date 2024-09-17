package com.finconsgroup.performplus.rest.api.vm.v2.view;

import java.io.Serializable;

import jakarta.persistence.Entity;

import org.springframework.data.annotation.Immutable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString

public class ViewAnagrafiche implements Serializable{
	private String anagrafica;
	private String codice;
	private String obiettivo;
	private String descrizione;
	private String albero;
	private String tipo;
	private String padre_obiettivo;
	private String padre;
	private Integer np_ordine;
	private String np_codice_completo;
	private String area;

}
