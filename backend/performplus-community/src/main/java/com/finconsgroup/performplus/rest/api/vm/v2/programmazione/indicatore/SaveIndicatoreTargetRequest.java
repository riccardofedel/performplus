package com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore;

import java.io.Serializable;
import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.v2.ValoreDetailVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class SaveIndicatoreTargetRequest implements Serializable{
	private Double peso;
	ValoreDetailVM storico;
	List<ValoreDetailVM> targets;

}
