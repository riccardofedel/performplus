package com.finconsgroup.performplus.pi.utils;

import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.rest.api.pi.vm.RegistrazioneVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.OrganizzazioneHelper;

public class MappingRegistrazione {

	public static RegistrazioneVM mapping(Registrazione r) {
		RegistrazioneVM out= Mapping.mapping(r, RegistrazioneVM.class);
		out.setQuestionario(r.getQuestionario()==null?null: new DecodificaVM(r.getQuestionario().getId()
				,r.getQuestionario().getId().toString()
				,r.getQuestionario().getIntestazione()));
		out.setOrganizzazione(r.getOrganizzazione()==null?null: new DecodificaVM(r.getOrganizzazione().getId()
				,OrganizzazioneHelper.ridotto(r.getOrganizzazione().getCodiceCompleto())
				,r.getOrganizzazione().getIntestazione()));
		out.setValutato(Mapping.mapping(r.getValutato(), RisorsaUmanaSmartVM.class));
		out.setValutatore(Mapping.mapping(r.getValutatore(), RisorsaUmanaSmartVM.class));
		out.setRegolamento(r.getRegolamento()==null?null: new DecodificaVM(r.getRegolamento().getId()
				,r.getRegolamento().getId().toString()
				,r.getRegolamento().getIntestazione()));
		out.setRegistrazioneSeparata(r.getForzaSchedaSeparata());
		out.setNonValutare(r.getInattiva());
		out.setDataPresaVisioneScheda(r.getDataAccettazioneScheda());
		out.setDataPresaVisioneValutazione(r.getDataAccettazioneValutazione());
		out.setNotePresaVisioneScheda(r.getNoteAccettazioneScheda());
		out.setNotePresaVisioneValutazione(r.getNoteAccettazioneValutazione());

		return out;
	}


}
