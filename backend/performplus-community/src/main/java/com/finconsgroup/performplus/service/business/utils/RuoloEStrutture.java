package com.finconsgroup.performplus.service.business.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode()
@ToString
public class RuoloEStrutture implements Serializable{
	private Long idRisorsa;
	private Ruolo ruolo;
	private Ruolo ruoloAggiunto;
	private List<OrganizzazioneSmartVM> struttureResp=new ArrayList<>();
	private List<OrganizzazioneSmartVM> struttureDip=new ArrayList<>();
	private OrganizzazioneSmartVM strutturaProf;
	private OrganizzazioneSmartVM strutturaProfAggiunta;
	
}