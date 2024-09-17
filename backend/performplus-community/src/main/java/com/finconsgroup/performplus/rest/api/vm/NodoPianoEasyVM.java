package com.finconsgroup.performplus.rest.api.vm;

import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class NodoPianoEasyVM extends EntityVM {
	private Long idEnte;
	private Integer anno;
	private TipoNodo tipoNodo;

}
