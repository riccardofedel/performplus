package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class AddAllegatoRequest implements Serializable{
	@NotBlank
	private String fileName;
	private String descrizione;
	@NotBlank
	private String nome;
	@NotBlank
	private String contentType;
	@NotBlank
	private String base64;

}
