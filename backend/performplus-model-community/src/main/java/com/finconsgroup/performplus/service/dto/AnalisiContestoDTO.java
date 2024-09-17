package com.finconsgroup.performplus.service.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class AnalisiContestoDTO extends EntityDTO {
    private EntityDTO obiettivoStrategico;
    private String forza;
    private String debolezza;
    private boolean relazione;
    private List<CategoriaStakeholderDTO> categorie;
  
   
}
