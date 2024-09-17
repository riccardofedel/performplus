package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode

public class CategoriaStakeholderDTO extends EntityDTO implements EnteInterface {
	
	private static final long serialVersionUID = -8795890367794561943L;
	
	private String codice;
	private String descrizione;
	private ImageEntryDTO imageEntry;
	private Long idEnte = 0l;

	
}
