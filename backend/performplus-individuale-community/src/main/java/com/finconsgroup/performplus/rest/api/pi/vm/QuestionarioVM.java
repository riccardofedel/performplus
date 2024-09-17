package com.finconsgroup.performplus.rest.api.pi.vm;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.service.dto.EnteInterface;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class QuestionarioVM extends EntityVM implements EnteInterface {
	private String intestazione;
	private String descrizione;
	private Long idEnte = 0l;
	private List<DecodificaVM> categorie;
	private List<DecodificaVM> incarichi;

}
