package com.finconsgroup.performplus.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class RangeIndicatoreDTO extends EntityDTO {

	private static final long serialVersionUID = 6425138191557764711L;

	private Long id;
	private Double minimo;
	private Double massimo;
	private Double valore;
	private Boolean proporzionale;
	private EntityDTO indicatorePiano;

}
