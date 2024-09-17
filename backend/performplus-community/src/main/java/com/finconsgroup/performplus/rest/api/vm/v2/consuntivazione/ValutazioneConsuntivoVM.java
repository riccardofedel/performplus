package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.time.LocalDate;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class ValutazioneConsuntivoVM extends EntityVM{
	private Integer anno;
	private Integer period;
	private String periodo;
	private String valore;
	private LocalDate dataRilevazione;
	private boolean enabled;
	private boolean enabledRipristina;
	public ValutazioneConsuntivoVM() {}
	public ValutazioneConsuntivoVM(Long id,String periodo, String valore, LocalDate dataRilevazione) {
		super(id);
		this.periodo = periodo;
		this.valore = valore;
		this.dataRilevazione=dataRilevazione;
	}
	
	public ValutazioneConsuntivoVM enabled(boolean enabled) {
		this.enabled=enabled;
		return this;
	}
	public ValutazioneConsuntivoVM period(Integer period) {
		this.period=period;
		return this;
	}
	public ValutazioneConsuntivoVM anno(Integer anno) {
		this.anno=anno;
		return this;
	}

}
