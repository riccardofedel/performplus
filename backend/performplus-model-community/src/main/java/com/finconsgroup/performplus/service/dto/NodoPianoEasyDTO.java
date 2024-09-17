package com.finconsgroup.performplus.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.TipoNodo;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode

public class NodoPianoEasyDTO extends EntityDTO implements Comparable<NodoPianoEasyDTO>, EnteInterface, AnnoInterface {

	private static final long serialVersionUID = -4833956628278775898L;

	private Long idEnte;
	private Integer anno;
	private TipoNodo tipoNodo;
	private String codice;
	private String codiceCompleto;
	private Integer ordine;
	private String denominazione;
	private NodoPianoEasyDTO padre;
	
	public NodoPianoEasyDTO() {

	}

	public NodoPianoEasyDTO(Long idEnte, Integer anno) {
		this(idEnte, anno, null);
	}

	public NodoPianoEasyDTO(Long idEnte, Integer anno, TipoNodo tipoNodo) {
		super();
		this.idEnte = idEnte;
		this.anno = anno;
		this.tipoNodo = tipoNodo;
	}
 
	@Override
	public int compareTo(NodoPianoEasyDTO o) {
		if (o == null)
			return -1;
		String a = getCodiceCompleto();
		String b = o.getCodiceCompleto();
		int comp = (a == null ? "" : a).compareTo(b == null ? "" : b);
		if (comp == 0)
			return getOrdine() - o.getOrdine();
		else
			return comp;
	}
	@JsonIgnore
	public String getCodiceRidotto() {
		String code = getCodiceCompleto();
		int k = code.indexOf('.');
		if (k >= 0) {
			code = code.substring(k + 1);
		} else {
			code = "";
		}
		return code;
	}


	@JsonIgnore
	public String getNomeCompleto() {
		StringBuilder sb = new StringBuilder();
		String tipo = getTipoNodo().name();
		sb.append(getCodiceRidotto());
		if (getDenominazione() == null || !getDenominazione().toLowerCase().startsWith(tipo.toLowerCase() + " ")) {
			sb.append(" ").append(tipo);
		}
		if (getDenominazione() != null) {
			sb.append(" ").append(getDenominazione());
		}
		return sb.toString();
	}
}
