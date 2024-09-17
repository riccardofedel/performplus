package com.finconsgroup.performplus.rest.api.pi.vm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class PreparaNodoQuestionarioRequest extends CreaNodoQuestionarioRequest{
	private String codiceCompleto;
}
