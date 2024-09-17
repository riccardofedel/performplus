package com.finconsgroup.performplus.rest.api.vm.v2;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoListVM implements Serializable{

	private String codiceProcesso;
	private String descrizioneProcesso;
	
	private String codiceDirezione;
	private String descrizioneDirezione;
	
	private String codiceNormativa;
	private String descrizioneNormativa;
	
	private String codiceTipoNormativa;
	private String descrizioneTipoNormativa;
		
	
}
