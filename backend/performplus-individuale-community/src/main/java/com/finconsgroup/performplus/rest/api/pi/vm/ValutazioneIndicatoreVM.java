package com.finconsgroup.performplus.rest.api.pi.vm;

import java.time.LocalDate;

import com.finconsgroup.performplus.domain.SchedaValutazioneIndicatore;
import com.finconsgroup.performplus.service.business.utils.DateHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ValutazioneIndicatoreVM extends ValutazioneObiettivoVM{
	Long idSchedaValutazioneIndicatore;
	Long idIndicatore;
	String indicatore;
	String tipoIndicatore;
	String target;
	Float raggiungimentoIndicatore;
	Float forzaturaRaggiungimentoIndicatore;
	String noteForzaturaRaggiungimentoIndicatore;
	Float pesoIndicatore;
	Float pesoIndicatoreForzato;
	Float valutazioneIndicatore;
	Float forzaturaIndicatore;
	String noteForzaturaIndicatore;
	String noteIndicatore;
	LocalDate scadenzaIndicatore;
	String scadenzaIndicatoreSt;
	
	public ValutazioneIndicatoreVM(SchedaValutazioneIndicatore svi) {
		this.idSchedaValutazioneIndicatore=svi.getId();
		this.idIndicatore=svi.getIndicatorePiano().getId();
		this.indicatore=svi.getIndicatorePiano().getSpecifica()==null?
				svi.getIndicatorePiano().getIndicatore().getDenominazione()
				:svi.getIndicatorePiano().getSpecifica();
		this.tipoIndicatore=svi.getTipo();
		this.raggiungimentoIndicatore=svi.getRaggiungimento();
		this.forzaturaRaggiungimentoIndicatore=svi.getForzaturaRaggiungimento();
		this.noteForzaturaRaggiungimentoIndicatore=svi.getNoteForzaturaRaggiungimento();
		this.pesoIndicatore=svi.getPeso();
		this.pesoIndicatoreForzato=svi.getPesoForzato();
		this.valutazioneIndicatore=svi.getValutazione();
		this.forzaturaIndicatore=svi.getForzatura();
		this.noteForzaturaIndicatore=svi.getNoteForzatura();
		this.noteIndicatore=svi.getIndicatorePiano().getNote();
		this.scadenzaIndicatore=svi.getIndicatorePiano().getScadenzaIndicatore();
		this.setScadenzaIndicatoreSt(DateHelper.toString(this.getScadenzaIndicatore()));				

	}
	public ValutazioneIndicatoreVM target(String target) {
		this.target=target;
		return this;
	}

}
