package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

import com.finconsgroup.performplus.enumeration.StatoIndicatore;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.service.business.utils.IndicatorePianoHelper;

import lombok.Data;

@SuppressWarnings("serial")
@Data

public class QuadraturaIndicatore implements Serializable,
		Comparable<QuadraturaIndicatore> {
	private Long idNodoPiano;
	private Long idIndicatorePiano;
	private Long idIndicatore;
	private String codiceNodoPiano;
	private String descNodoPiano;
	private String descIndicatore;
	private Double preventivo;
	private Boolean preventivoBoolean;
	private Double consuntivo;
	private Boolean consuntivoBoolean;
	private LocalDate dataPreventivo;
	private LocalDate dataConsuntivo;
	private int ordine;
	private String avvertimento;
	private StatoIndicatore status;
	private TipoFormula tipoFormula;
	private int decimali;

	public QuadraturaIndicatore() {
		super();
	}

	public QuadraturaIndicatore(ValutazioneDTO preventivo,
			ValutazioneDTO consuntivo, IndicatorePianoDTO risorsa) {
		super();
		idNodoPiano = risorsa.getNodoPiano().getId();
		idIndicatorePiano = risorsa.getId();
		idIndicatore = risorsa.getIndicatore().getId();
		codiceNodoPiano = risorsa.getNodoPiano().getCodiceCompleto();
		descNodoPiano = risorsa.getNodoPiano().getDenominazione();
		descIndicatore = risorsa.getSpecifica() == null ? risorsa
				.getIndicatore().getDenominazione() : risorsa.getSpecifica();
		this.preventivo = preventivo == null ? null : preventivo.getValore();
		this.consuntivo = consuntivo == null ? null : consuntivo.getValore();
		tipoFormula = risorsa.getIndicatore().getTipoFormula();
		dataPreventivo = preventivo == null ? null : preventivo.getInizio();
		dataConsuntivo = consuntivo == null ? null : consuntivo.getInizio();
		if (TipoFormula.TIPO_FORMULA_BULEANO.equals(tipoFormula)) {
			preventivoBoolean = preventivo == null ? null : preventivo
					.getValoreBooleano();
			consuntivoBoolean = consuntivo == null ? null : consuntivo
					.getValoreBooleano();
		}
		decimali = IndicatorePianoHelper.getDecimali(risorsa,risorsa.getIndicatore());
	}


	@Override
	public int compareTo(QuadraturaIndicatore o) {
		if (o == null)
			return -1;
		String a = getCodiceNodoPiano();
		String b = o.getCodiceNodoPiano();
		int k = a.lastIndexOf('.');
		if (k >= 0)
			a = a.substring(0, k);
		else
			a = "";
		k = b.lastIndexOf('.');
		if (k >= 0)
			b = b.substring(0, k);
		else
			b = "";
		int comp = (a == null ? "" : a).compareTo(b == null ? "" : b);
		if (comp != 0)
			return comp;
		comp = dataPreventivo == null ? 0 : dataPreventivo.compareTo(o
				.getDataPreventivo());
		if (comp != 0)
			return comp;
		return getOrdine() - o.getOrdine();
	}

	
	public Integer getScostamento() {
		Integer scostamento = null;
		if (null == getPreventivo() || null == getConsuntivo())
			return scostamento;
		scostamento = Math.round((float) (getConsuntivo() - getPreventivo()));
		return scostamento;
	}

	public Integer getScostamentoPercentuale() {
		Integer percentuale = null;
		if (null == getPreventivo() || null == getConsuntivo())
			return percentuale;
		if (getPreventivo().doubleValue() == 0)
			return null;
		double perc = 100d * getConsuntivo() / getPreventivo();
		percentuale = Math.round((float) perc);
		if (percentuale < 0)
			percentuale = 0;
		if (percentuale > 100)
			percentuale = 100;
		return percentuale;
	}


}
