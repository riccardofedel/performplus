package com.finconsgroup.performplus.service.dto;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ContestoDTO extends EntityDTO {
	private String premesse;
	private String linee;
	private String analisiContesto;
	private String analisiStrategica;
	private String obiettivi;
	private String popolazione;
	private String partecipate;
	private String investimenti;
	private String tributi;
	private String fabbisogni;
	private String patrimonio;
	private String finanziamento;
	private String equilibri;
	private String risorse;
	private String nazionale;
	private String regionale;
	private String singola;
	private String territorio;

	private EntityDTO piano;
	private boolean relazione;

	public boolean isEmpty() {
		return StringUtils.isBlank(premesse) && StringUtils.isBlank(linee) && StringUtils.isBlank(premesse)
				&& StringUtils.isBlank(analisiContesto) && StringUtils.isBlank(analisiStrategica)
				&& StringUtils.isBlank(obiettivi) && StringUtils.isBlank(popolazione)
				&& StringUtils.isBlank(partecipate) && StringUtils.isBlank(investimenti)
				&& StringUtils.isBlank(fabbisogni) && StringUtils.isBlank(patrimonio)
				&& StringUtils.isBlank(finanziamento) && StringUtils.isBlank(equilibri) && StringUtils.isBlank(risorse)
				&& StringUtils.isBlank(tributi) && StringUtils.isBlank(nazionale) && StringUtils.isBlank(regionale)
				&& StringUtils.isBlank(singola)
				&& StringUtils.isBlank(territorio);

	}
}