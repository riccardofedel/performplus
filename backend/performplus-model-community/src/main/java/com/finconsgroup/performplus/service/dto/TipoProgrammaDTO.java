package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class TipoProgrammaDTO extends EntityDTO{
    private String codice;
    private String descrizione;
    private TipoMissioneDTO tipoMissione;

  
    
 }
