package com.finconsgroup.performplus.rest.api.vm.v2.programmazione;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class NoteAssessoriRequest implements Serializable {
	@NotNull
	private Long idNodoPiano;
	private String noteAssessori;

}
