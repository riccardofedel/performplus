package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class ParametriRiceraQuestionario implements Serializable{

	@NotNull
	Long idEnte=0l;
	String testo;
	List<Long> incarichi;
	List<Long> categorie;
}
