package com.finconsgroup.performplus.rest.api.vm.v2;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.rest.api.vm.EntityVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
public class ValoreDetailVM extends EntityVM {
	private Double valoreNumerico;
	private Double valoreNumericoA;
	private Double valoreNumericoB;
	private Boolean valoreBooleano;
	private LocalDate valoreTemporale;
	private Integer anno;
	private Integer periodo;
	private Boolean enabled=true;
	private Boolean percentuale;
	@JsonIgnore
	public boolean isValid() {
		return anno!=null && periodo!=null && periodo>=1 && periodo<=4
				&& (valoreBooleano!=null||valoreNumerico!=null||valoreNumericoA!=null||valoreNumericoB!=null||valoreTemporale!=null) ;
	}

	
}
