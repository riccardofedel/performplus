package com.finconsgroup.performplus.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoConsuntivazione;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.TipoValutazione;
import com.finconsgroup.performplus.service.business.utils.ConsuntivoHelper;
import com.finconsgroup.performplus.service.business.utils.IndicatorePianoHelper;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class IndicatorePianoDTO extends IndicatorePianoEasyDTO {

	private int nroValutazioni;
	private List<ValutazioneDTO> consuntivi = new ArrayList<>();
	private List<ValutazioneDTO> preventivi = new ArrayList<>();
	private List<ValutazioneDTO> storici = new ArrayList<>();

	private List<RangeIndicatoreDTO> ranges = new ArrayList<>();

	
	private RaggruppamentoIndicatori raggruppamento = RaggruppamentoIndicatori.SPECIFICO;

	private ConfigurazioneDTO configurazione;

	public IndicatorePianoDTO() {
	}

	public IndicatorePianoDTO(ConfigurazioneDTO configurazione) {
		this.configurazione = configurazione;
	}



	public ValutazioneDTO getPrevisione1() {
		return getPrevisione(1);
	}

	public ValutazioneDTO getPrevisione2() {
		return getPrevisione(2);
	}

	public ValutazioneDTO getPrevisione3() {
		return getPrevisione(3);
	}

	public ValutazioneDTO getPrevisione4() {
		return getPrevisione(4);
	}

	public ValutazioneDTO getPrevisione5() {
		return getPrevisione(5);
	}

	protected ValutazioneDTO getPrevisione(int i) {
		return getPreventivo(i - 1);
	}

	public ValutazioneDTO getConsuntivo1() {
		return getConsuntivo(1, configurazione.getTipoConsuntivazione());
	}

	public ValutazioneDTO getConsuntivo2() {
		return getConsuntivo(2, configurazione.getTipoConsuntivazione());
	}

	public ValutazioneDTO getConsuntivo3() {
		return getConsuntivo(3, configurazione.getTipoConsuntivazione());
	}

	public ValutazioneDTO getConsuntivo4() {
		return getConsuntivo(4, configurazione.getTipoConsuntivazione());
	}

	public ValutazioneDTO getConsuntivo5() {
		return getConsuntivo(5, configurazione.getTipoConsuntivazione());
	}


	public String getTargetStoricoStringa() {
		if (getIndicatore() == null)
			return null;
		if (getStorico() == null)
			return null;
		Boolean perc = getPercentuale();
		if (perc == null)
			perc = getIndicatore().getPercentuale() != null && getIndicatore().getPercentuale();
		if (perc)
			return getStorico().getValorePercStringa();
		else
			return getStorico().getValoreStringa();

	}

	public String getTargetStringa() {
		if (getIndicatore() == null)
			return null;
		if (getPrevisione1() == null)
			return null;
		Boolean perc = getPercentuale();
		if (perc == null)
			perc = getIndicatore().getPercentuale() != null && getIndicatore().getPercentuale();
		if (perc)
			return getPrevisione1().getValorePercStringa();
		else
			return getPrevisione1().getValoreStringa();
	}

	public String getVariazionePercStringa() {
		if (getVariazione() == null)
			return null;
		if (getStorico() == null || getStorico().getValore() == null || getStorico().getValore() == 0)
			return "100%";
		return ((int) (100d * getVariazione() / getStorico().getValore())) + "%";
	}

	public void addPreventivi(List<ValutazioneDTO> listPreventivi) {
		if (listPreventivi == null)
			return;
		for (ValutazioneDTO v : listPreventivi) {
			addPreventivo(v);
		}
	}

	public void addStorici(List<ValutazioneDTO> listStorici) {
		if (listStorici == null)
			return;
		for (ValutazioneDTO v : listStorici) {
			addStorico(v);
		}
	}

	public void addConsuntivi(List<ValutazioneDTO> listConsuntivi) {
		if (listConsuntivi == null)
			return;
		for (ValutazioneDTO v : listConsuntivi) {
			addConsuntivo(v);
		}
	}

	public void add(List<ValutazioneDTO> items, ValutazioneDTO v) {
		if (v == null)
			return;
		for (ValutazioneDTO val : items) {
			if (v.getId() != null && val.getId() != null && v.getId().equals(val.getId()))
				return;
		}
		items.add(v);
	}

	public void addValutazione(ValutazioneDTO valutazione) {
		if (valutazione == null || valutazione.getTipoValutazione() == null)
			return;
		switch (valutazione.getTipoValutazione()) {
		case PREVENTIVO:
			addPreventivo(valutazione);
			break;
		case CONSUNTIVO:
			addConsuntivo(valutazione);
			break;
		case STORICO:
			addStorico(valutazione);
			break;
		}
	}



	public ValutazioneDTO getConsuntivo() {
		return getConsuntivoMax();
	}

	public ValutazioneDTO getConsuntivoMax() {
		Integer anno = getAnno();
		if (anno == null)
			return null;
		return getConsuntivoAnnoMax(anno);
	}

	public ValutazioneDTO getConsuntivoMax(int i) {
		Integer anno = getAnno();
		if (anno == null)
			return null;
		anno += i;
		return getConsuntivoAnnoMax(anno);
	}

	public ValutazioneDTO getConsuntivoAnnoMax(int anno) {
		if (consuntivi.isEmpty())
			return null;
		ValutazioneDTO val = null;
		for (ValutazioneDTO rel : consuntivi) {
			if (rel.getDataRilevazione() == null || ConsuntivoHelper.getAnno(rel.getDataRilevazione()) != anno
					|| rel.getValore() == null)
				continue;
			if (val == null || rel.getDataRilevazione().isAfter(val.getDataRilevazione()))
				val = rel;
		}
		return val;
	}

	public void setConsuntivo(ValutazioneDTO consuntivo) {
		addConsuntivo(consuntivo);
	}

	public void addConsuntivo(ValutazioneDTO consuntivo) {
		if (manca(consuntivi, consuntivo))
			consuntivi.add(consuntivo);
	}

	private boolean manca(List<ValutazioneDTO> items, ValutazioneDTO v) {
		List<Long> togli = new ArrayList<>();
		boolean manca = true;
		for (ValutazioneDTO c : items) {
			if (v.getId() != null && v.getId().equals(c.getId())) {
				manca = false;
			} else if (TipoValutazione.PREVENTIVO.equals(v.getTipoValutazione()) && v.getInizio() != null
					&& c.getInizio() != null && v.getInizio().equals(c.getInizio())) {
				if (v.getDateInsert().isAfter(c.getDateInsert())) {
					togli.add(c.getId());
				} else {
					manca = false;
				}
			} else if (TipoValutazione.STORICO.equals(v.getTipoValutazione()) && v.getInizio() != null
					&& c.getInizio() != null
					&& ConsuntivoHelper.getAnno(c.getInizio()) == ConsuntivoHelper.getAnno(v.getInizio())) {
				if (v.getInizio().isAfter(c.getInizio())) {
					togli.add(c.getId());
				} else {
					manca = false;
				}
			} 
		}
		if (!togli.isEmpty())
			items.removeIf(c -> togli.contains(c.getId()));
		return manca;
	}

	public ValutazioneDTO getStorico() {
		Integer anno = getAnno();
		if (anno == null)
			return null;
		return getStoricoAnno(anno);
	}
	public void setStorico(ValutazioneDTO storico) {
		addStorico(storico);
	}

	public void addStorico(ValutazioneDTO storico) {
		if (manca(storici, storico))
			storici.add(storico);
	}

	public void setPreventivo(ValutazioneDTO preventivo) {
		addPreventivo(preventivo);
	}

	public void addPreventivo(ValutazioneDTO preventivo) {
		if (manca(preventivi, preventivo))
			preventivi.add(preventivo);
	}

	public ValutazioneDTO getConsuntivo(int trimestre, TipoConsuntivazione tipoConsuntivazione) {
		Integer anno = getAnno();
		if (anno == null)
			return null;
		return getConsuntivo(trimestre, anno, tipoConsuntivazione);
	}

	public ValutazioneDTO getPreventivo() {
		Integer anno = getAnno();
		if (anno == null)
			return null;
		return getPreventivoAnno(anno);
	}


	public ValutazioneDTO getConsuntivo(int trimestre, int anno, TipoConsuntivazione tipoConsuntivazione) {
		if (consuntivi.isEmpty())
			return null;
		Periodo tp = ConsuntivoHelper.tipoPianoTrimestrale(trimestre);
		LocalDate min = ConsuntivoHelper.inizioPeriodo(anno, tp, tipoConsuntivazione);
		LocalDate max = ConsuntivoHelper.finePeriodo(anno, tp, tipoConsuntivazione);
		ValutazioneDTO val = null;
		for (ValutazioneDTO rel : consuntivi) {
			if (rel.getDataRilevazione() != null) {
				if (!rel.getDataRilevazione().isBefore(min) && !rel.getDataRilevazione().isAfter(max))
					val = rel;
			} else {
				if (rel.getInizio() != null && !rel.getInizio().isBefore(min) && !rel.getInizio().isAfter(max)) {
					val = rel;
				}
			}
		}
		return val;
	}

	public ValutazioneDTO getPreventivoAnno(int anno) {
		if (preventivi.isEmpty())
			return null;
		for (ValutazioneDTO rel : preventivi) {
			if (rel.getInizio() == null)
				continue;
			if (ConsuntivoHelper.getAnno(rel.getInizio()) == anno) {
				return rel;
			}
		}
		return null;
	}

	public ValutazioneDTO getStoricoAnno(int anno) {
		if (storici.isEmpty())
			return null;
		for (ValutazioneDTO rel : storici) {
			if (rel.getInizio() == null && rel.getDateInsert() == null)
				continue;
			if (rel.getInizio() == null)
				rel.setInizio(rel.getDateInsert()==null?LocalDate.of(anno,01,01):rel.getDateInsert().toLocalDate());
			if (ConsuntivoHelper.getAnno(rel.getInizio()) == anno) {
				return rel;
			}
		}
		return null;
	}

	public ValutazioneDTO getPreventivo(int i) {
		Integer anno = getAnno();
		if (anno == null)
			return null;
		anno += i;
		return getPreventivoAnno(anno);
	}

	public ValutazioneDTO getStorico(int i) {
		Integer anno = getAnno();
		if (anno == null)
			return null;
		anno += i;
		return getStoricoAnno(anno);
	}

	public List<ValutazioneDTO> getPreventivi() {
		return preventivi;
	}

	public List<ValutazioneDTO> getConsuntivi() {
		return consuntivi;
	}

	public List<ValutazioneDTO> getStorici() {
		return storici;
	}

	public ValutazioneDTO getPreventivoMin() {
		if (preventivi.isEmpty())
			return null;
		ValutazioneDTO val = null;
		for (ValutazioneDTO rel : preventivi) {
			if (rel.getInizio() == null)
				continue;
			if (val == null || ConsuntivoHelper.getAnno(rel.getInizio()) < ConsuntivoHelper.getAnno(val.getInizio()))
				val = rel;
		}
		return val;
	}

	public ValutazioneDTO getPreventivoMax() {
		if (preventivi.isEmpty())
			return null;
		ValutazioneDTO val = null;
		for (ValutazioneDTO rel : preventivi) {
			if (rel.getInizio() == null)
				continue;
			if (val == null || ConsuntivoHelper.getAnno(rel.getInizio()) > ConsuntivoHelper.getAnno(val.getInizio()))
				val = rel;
		}
		return val;
	}

	public String getVariazioneStringa() {
		if (getVariazione() == null)
			return null;
		if (getIndicatore() == null)
			return null;
		boolean perc = getIndicatore().getPercentuale() != null && getIndicatore().getPercentuale();
		if (perc)
			return ModelHelper.toStringDec(getVariazione() * 100d, IndicatorePianoHelper.getDecimali(this,this.getIndicatore())) + "%";
		else
			return ModelHelper.toStringDec(getVariazione(), IndicatorePianoHelper.getDecimali(this,this.getIndicatore()));
	}


	public Double getVariazione() {
		double prev = getPreventivoNodo() == null ? 0d : getPreventivoNodo();
		return getConsuntivo() == null || getConsuntivo().getValore() == null ? 0d : getConsuntivo().getValore() - prev;
	}

	public Double getPreventivoNodo() {
		if (getNodoPiano() == null)
			return null;
//		if (getNodoPiano().getTipoNodo() == TipoNodo.MISSIONE) {
//			if (getPreventivoMax() == null)
//				return null;
//			if (getPreventivoMax().getValore() == null)
//				return null;
//			return getPreventivoMax().getValore();
//		} else {
			if (getPreventivo() == null)
				return null;
			if (getPreventivo().getValore() == null)
				return null;
			return getPreventivo().getValore();
//		}
	}


//	public void addRanges(List<RangeIndicatoreDTO> ranges) {
//		if (ranges == null || ranges.isEmpty())
//			return;
//		getRanges().addAll(ranges);
//	}

	public ConfigurazioneDTO getConfigurazione() {
		return configurazione;
	}

	public void setConfigurazione(ConfigurazioneDTO configurazione) {
		this.configurazione = configurazione;
	}

}
