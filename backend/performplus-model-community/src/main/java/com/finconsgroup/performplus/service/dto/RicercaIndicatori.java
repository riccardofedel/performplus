package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;

import lombok.Data;

@SuppressWarnings("serial")
@Data

public class RicercaIndicatori implements Serializable{
    private String testoRicerca;
    private RaggruppamentoIndicatori raggruppamento;
    private TipoFormula formula;
    private Long idEnte=0l;

    
}