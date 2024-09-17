package com.finconsgroup.performplus.rest.api.pi.vm;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
@NoArgsConstructor
public class AggiornaQuestionarioRequest implements Serializable{
	@NotBlank
	private String intestazione;
	private String descrizione;
	@NotNull
	private Long idEnte = 0l;
	private List<Long> categorie;
	private List<Long> incarichi;

}
