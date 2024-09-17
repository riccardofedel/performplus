package com.finconsgroup.performplus.service.dto;

import java.math.BigDecimal;

import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.StatoConservazione;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class RisorsaStrumentaleDTO extends EntityDTO implements EnteInterface {
	private String codice;
	private String descrizione;
	private Disponibile disponibile;
	private String note;
	private Integer quantita;
	private StatoConservazione statoConservazione;
	private TipologiaRisorsaStrumentaleDTO tipologia;
	private BigDecimal valore;
	private Long idEnte = 0l;
	private String modello;
	private String targa;

	public BigDecimal getValoreTotale() {
		if (valore == null || quantita == null)
			return null;
		return valore.multiply(new BigDecimal(quantita));

	}

	public Long getIdEnte() {
		return idEnte;
	}

	public void setIdEnte(Long idEnte) {
		this.idEnte = idEnte;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getTarga() {
		return targa;
	}

	public void setTarga(String targa) {
		this.targa = targa;
	}

}
