package com.finconsgroup.performplus.service.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.service.business.utils.IndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;

import org.springframework.data.mapping.model.MappingInstantiationException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ValutazioneDTO extends ValoreDTO {
	private IndicatorePianoEasyDTO indicatorePiano;
	private TipoValutazione tipoValutazione;
	private LocalDate dataRilevazione;
	private String note;
	private Periodo periodo;

	@JsonIgnore
	public Double getValore() {
		if (getTipoFormula() == null || TipoFormula.TIPO_FORMULA_DATA.equals(getTipoFormula()))
			return null;
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(getTipoFormula()))
			return getValoreBOOL();
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(getTipoFormula()))
			return getValoreNumerico() == null ? null : getValoreNumerico().doubleValue();
		if (TipoFormula.TIPO_FORMULA_DATA.equals(getTipoFormula()))
			return (double) getValoreTemporale().getTime();
		if (getValoreNumericoA() == null || getValoreNumericoB() == null)
			return null;
		if (getValoreNumericoB().doubleValue() == 0)
			return null;
		if(getValoreNumerico()!=null)
			return getValoreNumerico().doubleValue();
		return getValoreNumericoA().doubleValue() / getValoreNumericoB().doubleValue();
	}

	@JsonIgnore 
	public TipoFormula getTipoFormula() {
		if(indicatorePiano==null|| indicatorePiano.getIndicatore()==null) {
			return null;
		}
		return indicatorePiano.getIndicatore().getTipoFormula();
	}

	@JsonIgnore
	public Double getValoreBOOL() {
		if (getTipoFormula() != null || TipoFormula.TIPO_FORMULA_BULEANO.equals(getTipoFormula())) {
			if (getValoreBooleano() == null)
				return null;
			return Boolean.TRUE.equals(getValoreBooleano()) ? 1d : 0d;
		}
		return null;
	}
	
	@JsonIgnore
	public String getValorePercStringa() {
		if (getTipoFormula() == null)
			return null;
		if (TipoFormula.TIPO_FORMULA_DATA.equals(getTipoFormula()))
			return null;
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(getTipoFormula()))
			return null;
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(getTipoFormula()))
			return null;
		if (getValore() == null)
			return null;
		return ModelHelper.toStringDec(getValore() * 100d, IndicatorePianoHelper.getDecimali(this)) + "%";
	}
	

	
	@JsonIgnore
	public String getValoreStringa() {
		if (getTipoFormula() == null)
			return null;
		if (TipoFormula.TIPO_FORMULA_DATA.equals(getTipoFormula()))
			return ModelHelper.toString(getValoreTemporale());
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(getTipoFormula())) {
			if (getValoreBooleano() == null)
				return "";
			return Boolean.TRUE.equals(getValoreBooleano()) ? "Sì" : "No";
		}
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(getTipoFormula()))
			return ModelHelper.toStringDec(getValore(), IndicatorePianoHelper.getDecimali(this));
		if (getValoreNumericoA() == null || getValoreNumericoB() == null)
			return null;
		if (getValoreNumericoB().doubleValue() == 0)
			return null;
		return ModelHelper.toStringDec(getValore(), IndicatorePianoHelper.getDecimali(this));
	}

}
