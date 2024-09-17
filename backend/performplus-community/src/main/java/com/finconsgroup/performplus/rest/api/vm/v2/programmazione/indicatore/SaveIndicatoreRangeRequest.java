package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore;

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
public class SaveIndicatoreRangeRequest implements Serializable{
	@NotNull
	List<IndicatoreRangeVM> ranges;

}
