package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;

import com.finconsgroup.performplus.enumeration.Disponibile;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class RicercaRisorseUmane implements Serializable{
    private String testoRicerca;
    private Boolean esterna;
    private Long idCategoria;
    private Long idContratto;
    private Long idProfilo;
    private Integer anno;
    private Long idEnte=0l;
    private Disponibile disponibile;
    private Boolean partTime;
    private String codiceInterno;
    private Boolean politico;
    private Boolean soloAttiveAnno=true;

}