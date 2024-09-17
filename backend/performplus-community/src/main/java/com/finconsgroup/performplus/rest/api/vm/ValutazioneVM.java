package com.finconsgroup.performplus.rest.api.vm;

import java.math.RoundingMode;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.rest.api.vm.v2.IndicatorePianoVM;
import com.finconsgroup.performplus.service.business.utils.MappingIndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;
import com.finconsgroup.performplus.service.dto.ValoreDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ValutazioneVM extends ValoreDTO {
	private IndicatorePianoVM indicatorePiano;
	private TipoValutazione tipoValutazione;
	private LocalDate dataRilevazione;
	private String note;
	private Periodo periodo;
	private Integer anno;

	@JsonIgnore
	public Double getValore() {
		if (getTipoFormula() == null || TipoFormula.TIPO_FORMULA_DATA.equals(getTipoFormula()))
			return null;
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(getTipoFormula()))
			return getValoreBOOL();
		if (TipoFormula.TIPO_FORMULA_NUMERO.equals(getTipoFormula()))
			return getValoreNumerico() == null ? null : getValoreNumerico().doubleValue();
		if (TipoFormula.TIPO_FORMULA_DATA.equals(getTipoFormula()))
			return (double)getValoreTemporale().getTime();
		if (getValoreNumericoA() == null || getValoreNumericoB() == null)
			return null;
		if (getValoreNumericoB().doubleValue() == 0)
			return null;
		if(getValoreNumerico()!=null)
			return getValoreNumerico().doubleValue();
		return getValoreNumericoA().divide(getValoreNumericoB(),MappingIndicatorePianoHelper.getDecimali(this),RoundingMode.HALF_DOWN).doubleValue();
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
	public TipoFormula getTipoFormula() {
		if (getIndicatorePiano() == null || getIndicatorePiano().getIndicatore() == null)
			return null;
		return getIndicatorePiano().getIndicatore().getTipoFormula();
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
		return ModelHelper.toStringDec(getValore() * 100d, MappingIndicatorePianoHelper.getDecimali(this)) + "%";
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
			return ModelHelper.toStringDec(getValore(), MappingIndicatorePianoHelper.getDecimali(this));
		if (getValoreNumerico()==null || getValoreNumericoA() == null || getValoreNumericoB() == null)
			return null;
		if (getValoreNumericoB().doubleValue() == 0)
			return null;
		return ModelHelper.toStringDec(getValore(), MappingIndicatorePianoHelper.getDecimali(this));
	}
	

}
