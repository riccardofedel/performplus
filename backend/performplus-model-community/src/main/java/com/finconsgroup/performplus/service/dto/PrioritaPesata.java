package com.finconsgroup.performplus.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PrioritaPesata implements Serializable{

	private static final long serialVersionUID = -2125359370043934362L;
	
	public BigDecimal pj = BigDecimal.ZERO;
	public BigDecimal rij = BigDecimal.ZERO;
	public BigDecimal req = BigDecimal.ZERO;
	public BigDecimal importo = BigDecimal.ZERO;
	
	public BigDecimal fpij(){
		return importo.multiply(pij());
	}

	public BigDecimal pij() {
		if(req.doubleValue()<=0)return BigDecimal.ZERO;
		return rij.multiply(pj).divide(req,6,RoundingMode.HALF_DOWN);
	}
	
	@Override
	public String toString(){
		return "pj:"+pj+",rij:"+rij+",req:"+req+",importo:"+importo+", pij:"+pij()+", fpij:"+fpij();
	}
}