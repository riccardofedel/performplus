package com.finconsgroup.performplus.pi.utils;

import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.domain.Regolamento;
import com.finconsgroup.performplus.rest.api.pi.vm.RegolamentoVM;

public class MappingRegolamento {

	public static RegolamentoVM mapping(Regolamento r) {
		final RegolamentoVM out=new RegolamentoVM();
		out.setAnno(r.getAnno());
		out.setId(r.getId());
		out.setIdEnte(r.getIdEnte());
		out.setIntestazione(r.getIntestazione());
		out.setPesoComportamentiOrganizzativi(r.getPesoComportamentiOrganizzativi());
		out.setPesoObiettiviDiStruttura(r.getPesoObiettiviDiStruttura());
		out.setPesoObiettiviDiPerformance(r.getPesoObiettiviDiPerformance());
		out.setPesoObiettiviIndividuali(r.getPesoObiettiviIndividuali());
		out.setPo(r.getPo());
		final List<Long> catIds=new ArrayList<>();
		final List<Long> incIds=new ArrayList<>();
		final List<String> desc=new ArrayList<>();
		if(r.getCategorie()!=null) {
			r.getCategorie().stream().forEach(c->{
				catIds.add(c.getId());
				desc.add(c.getCodice());
			});
		}
		out.setCategorie(catIds);
		out.setDescCategorie(String.join(", ", desc));
		
		desc.clear();
		if(r.getIncarichi()!=null) {
			r.getIncarichi().stream().forEach(c->{
				incIds.add(c.getId());
				desc.add(c.getCodice());
			});
		}
		out.setIncarichi(incIds);
		out.setDescIncarichi(String.join(",", desc));
		return out;
	}


}
