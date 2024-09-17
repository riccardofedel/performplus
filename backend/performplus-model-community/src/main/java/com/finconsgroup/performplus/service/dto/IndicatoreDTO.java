package com.finconsgroup.performplus.service.dto;

import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class IndicatoreDTO extends EntityDTO implements EnteInterface{

    private Integer decimali;
    private Integer decimaliA;
    private Integer decimaliB;
    private String denominazione;
    private String descrizione;
    private String nomeValoreA;
    private String nomeValoreB;
    private TipoFormula tipoFormula;
    private Boolean percentuale;
    private Long idEnte=0l;
	private RaggruppamentoIndicatori raggruppamento=RaggruppamentoIndicatori.SPECIFICO;
	   
 

}
