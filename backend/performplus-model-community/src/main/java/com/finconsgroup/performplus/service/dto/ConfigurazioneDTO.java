package com.finconsgroup.performplus.service.dto;

import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.Livello;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ConfigurazioneDTO extends EntityDTO {
    
	private Long idEnte=0l;
	private Integer anno=2021;
	private Double maxPercentualePesoOperativi=75d;
	private TipoConsuntivazione tipoConsuntivazione=TipoConsuntivazione.SEMESTRE;
	private Livello maxLivello=Livello.INFERIORE;
	private Integer maxOre=8;
	private Boolean abilitaObiettivoGestionale;
	public ConfigurazioneDTO() {}
	public ConfigurazioneDTO(Long idEnte, Integer anno) {
		super();
		this.idEnte = idEnte;
		this.anno = anno;
	}

  

}
