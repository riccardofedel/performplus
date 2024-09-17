package com.finconsgroup.performplus.service.business.utils;

import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.StrutturaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.AggiornaStrutturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.OrganizzazioneDetailVM;
import com.finconsgroup.performplus.service.business.utils.Mapping;

public class MappingStrutturaHelper {
	private MappingStrutturaHelper() {
	}
	public static OrganizzazioneSmartVM mappingToSmart(Organizzazione o) {
		return Mapping.mapping(o, OrganizzazioneSmartVM.class)
				.idPadre(o.getPadre() == null ? null : o.getPadre().getId());
	}

	public static OrganizzazioneDetailVM mappingToDetail(final Organizzazione r) {
		if (r == null)
			return null;
		final OrganizzazioneDetailVM out = Mapping.mapping(r, OrganizzazioneDetailVM.class);
		return out;
	}


	public static Organizzazione mappingFromRequest(final AggiornaStrutturaRequest r) {
		final Organizzazione out = Mapping.mapping(r, Organizzazione.class);
		return out;
	}


	public static List<OrganizzazioneSmartVM> mappingItemsToSmart(List<Organizzazione> items) {
		final List<OrganizzazioneSmartVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToSmart(r)));
		return out;
	}
	public static StrutturaVM mappingToStruttura(Organizzazione o) {
		return Mapping.mapping(o, StrutturaVM.class);
	}

	public static List<StrutturaVM> mappingItemsToStruttura(List<Organizzazione> items) {
		final List<StrutturaVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToStruttura(r)));
		return out;
	}


}
