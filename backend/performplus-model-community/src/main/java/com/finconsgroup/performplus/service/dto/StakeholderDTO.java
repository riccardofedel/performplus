package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class StakeholderDTO extends EntityDTO implements EnteInterface {
	private String descrizione;
	private String codice;
	private CategoriaStakeholderDTO categoria;
	private Long idEnte = 0l;

}