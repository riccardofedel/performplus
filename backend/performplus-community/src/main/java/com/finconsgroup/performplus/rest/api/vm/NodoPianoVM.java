package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.v2.OrganizzazioneVM;
import com.finconsgroup.performplus.service.dto.AnnoInterface;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;


@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class NodoPianoVM extends EntityVM implements EnteInterface, AnnoInterface {

	private TipoNodo tipoNodo;
	private int ordine;
	private Integer anno;
	private String codice;
	private String denominazione;
	private String note;
	private String descrizione;
	private OrganizzazioneVM organizzazione;
	private Long idEnte = 0l;
	private String codiceCompleto;
	private NodoPianoVM padre;
	public NodoPianoVM() {
		this(0l,null);
	}

	public NodoPianoVM(Long idEnte, Integer anno) {
		this(idEnte, anno, TipoNodo.PIANO);
	}

	public NodoPianoVM(Long idEnte, Integer anno, TipoNodo tipoNodo) {
		super();
		this.idEnte = idEnte;
		this.anno = anno;
		this.tipoNodo = tipoNodo;
	}

}

