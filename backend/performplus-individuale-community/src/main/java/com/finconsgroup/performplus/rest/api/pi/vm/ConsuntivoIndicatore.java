package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.TipoFormula;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class ConsuntivoIndicatore implements Serializable {
	private TipoFormula tipoFormula ;
	private boolean desc;
	private Double target;
	private Double cons;
	private Double raggiungimento;
	private Float peso;
	private boolean stategico;
	private boolean sostenibile;
	private Double percentualeEsogena;
	private Double percentualeEsogenaProposta;
	private boolean nonValutabile;
	private boolean percentuale;
	private Integer anno;
	private Long idEnte;
	private Double raggiungimentoEffettivo;
	private Double raggiungimentoPesato;
	private Double raggiungimentoPesatoEffettivo;
	private Double incrementoEsogeno;
	

}

