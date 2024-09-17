package com.finconsgroup.performplus.service.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class PrioritaOperativaDTO extends EntityDTO {
    private NodoPianoDTO nodoPiano;
    private OrganizzazioneDTO organizzazione;
    private BigDecimal priorita;
    
}
