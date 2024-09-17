package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ConfigurazioneVM extends EntityVM {
    
	private Long idEnte=0l;
	private Integer anno=2022;
	private Double maxPercentualePesoOperativi=75d;
	private TipoConsuntivazione tipoConsuntivazione=TipoConsuntivazione.TRIMESTRE;
	private Livello maxLivello=Livello.INFERIORE;
	private Integer maxOre=8;
	private Boolean abilitaObiettivoGestionale;
	public ConfigurazioneVM() {}
	public ConfigurazioneVM(Long idEnte, Integer anno) {
		super();
		this.idEnte = idEnte;
		this.anno = anno;
	}

  

}
