package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.enumeration.CalcoloConsuntivazione;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class IndicatoreVM extends EntityVM implements EnteInterface{

    private Integer decimali;
    private Integer decimaliA;
    private Integer decimaliB;
    private String denominazione;
    private String descrizione;
    private String nomeValoreA;
    private String nomeValoreB;
    private TipoFormula tipoFormula;
    private Boolean percentuale;
    private Boolean decrescente;
    private Long idEnte=0l;
	private RaggruppamentoIndicatori raggruppamento=RaggruppamentoIndicatori.SPECIFICO;
	private CalcoloConsuntivazione calcoloConsuntivazione; 
 
}
