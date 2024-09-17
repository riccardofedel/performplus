package com.finconsgroup.performplus.rest.api.pi.vm;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class PesoIndicatoreRegistrazioneVM extends EntityVM{
      private Long idIndicatorePiano;
      private Long idRegistrazione;
      private String denominazione;
      private Float peso;
	public PesoIndicatoreRegistrazioneVM(Long idIndicatorePiano, Long idRegistrazione, String denominazione) {
		super();
		this.idIndicatorePiano = idIndicatorePiano;
		this.idRegistrazione=idRegistrazione;
		this.denominazione = denominazione;
	}
      
}
