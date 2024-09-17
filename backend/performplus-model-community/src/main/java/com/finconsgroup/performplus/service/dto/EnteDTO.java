package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class EnteDTO extends EntityDTO {


	private String nome;
	private String descrizione;
	private ImageEntryDTO imageEntry;

	

}
