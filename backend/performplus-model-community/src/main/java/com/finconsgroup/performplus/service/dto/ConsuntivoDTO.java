package com.finconsgroup.performplus.service.dto;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.service.business.utils.ConsuntivoHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ConsuntivoDTO extends ConsuntivoIndicatoreDTO {

	private boolean primo;

	public ConsuntivoDTO(RelazioneIndicatoreDTO info, ConfigurazioneDTO configurazione) {
		super(info, configurazione);
	}
	public ConsuntivoDTO(ConsuntivoIndicatoreDTO c) {
		this(c.getRelazioneIndicatore(),c.getConfigurazione());
	}
	public IndicatorePianoDTO getIndicatorePiano() {
		return primo?null:getRelazione();
	}
	public RelazioneIndicatoreDTO getRelazione() {
		return getRelazioneIndicatore();
	}

	public IndicatoreDTO getIndicatore() {
		if (getRelazione() != null)
			return getRelazione().getIndicatore();
		return null;
	}
	public NodoPianoEasyDTO getNodo() {
		return primo?getRelazione().getNodoPiano():null;
	}
	public NodoPianoEasyDTO getNodoPiano() {
		return primo?getRelazione().getNodoPiano():null;
	}



	public String getNome() {
		String denom = null;
		if (getIndicatorePiano() != null) {
			denom = getIndicatorePiano().getDenominazione();
		} else if (getNodoPiano() != null) {
			denom = ModelHelper.normalize(getTipoNodo())+" "+getNodoPiano().getDenominazione();
			if (!TipoNodo.PIANO.equals(getNodoPiano().getTipoNodo() ) )
				denom = getNodoPiano().getCodice() + " " + denom;
		} 
		return denom;
	}





	public Double getRaggiungimento() {
		
		return ConsuntivoHelper.raggiungimentoRange(this);
	}


	public String getRaggiungimentoString() {
		if (getRaggiungimento() == null)
			return null;
		return Math.round(getRaggiungimento()) + "%";
	}

	public boolean getRaggiunto33() {
		if (getRaggiungimento() == null)
			return false;
		if (getRaggiungimento() >= 66)
			return false;
		return getRaggiungimento() >= 33;
	}

	public boolean getRaggiunto66() {
		if (getRaggiungimento() == null)
			return false;
		return getRaggiungimento() >= 66;
	}

	public boolean getRaggiunto00() {
		if (getRaggiungimento() == null)
			return true;
		return getRaggiungimento() < 33;
	}

	public ConsuntivoDTO primo(boolean primo) {
		this.primo=primo;
		return this;
	}

	public boolean getInizioChanged() {
		if (getDataInizio() == null || getDataInizioEff() == null)
			return false;
		return getDataInizio().isBefore(getDataInizioEff());
	}

	public boolean getFineChanged() {
		if (getDataFine() == null || getDataFineEff() == null)
			return false;
		return getDataFine().isBefore(getDataFineEff());

	}
}
