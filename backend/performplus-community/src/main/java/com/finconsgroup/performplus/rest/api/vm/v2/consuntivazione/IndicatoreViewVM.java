package com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.EntityVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatoreRangeVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatoreTargetVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class IndicatoreViewVM extends EntityVM{
	private IndicatorePianoDetailVM indicatore;
	private IndicatoreTargetVM preventivo;
	private List<IndicatoreRangeVM> ranges;
	
}
