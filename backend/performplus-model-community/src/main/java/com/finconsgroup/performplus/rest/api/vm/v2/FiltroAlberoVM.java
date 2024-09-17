package com.finconsgroup.performplus.rest.api.vm.v2;

import java.io.Serializable;
import java.util.List;

import com.finconsgroup.performplus.enumeration.TipoNodo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class FiltroAlberoVM implements Serializable {
	@NotNull
	Long idEnte;
	@NotNull
	Integer anno;
	String testo;
	Long idResponsabile;
	Long idRisorsa;
	Long idDirezione;
//	Long idProgramma;
	List<TipoNodo> tipiNodo;
	String codiceCompleto;
	String codiceInterno;
	List<Long> strutture;
	String codice;
}