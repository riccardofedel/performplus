package com.finconsgroup.performplus.service.business.utils;

import java.util.ArrayList;
import java.util.List;

import com.finconsgroup.performplus.domain.Utente;
import com.finconsgroup.performplus.rest.api.vm.UtenteSmartVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.service.dto.UtenteDTO;

public class MappingUtenteHelper {
	private MappingUtenteHelper() {
	}
	public static UtenteSmartVM mappingToSmart(Utente u) {
		return Mapping.mapping(u, UtenteSmartVM.class)
				.codiceFiscale(u.getCodiceInterno())
				.username(u.getUserid());
	}


	public static List<UtenteVM> mappingItemsToUtente(List<Utente> items) {
		final List<UtenteVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToUtente(r)));
		return out;
	}
	public static UtenteVM mappingToUtente(Utente u) {
		return Mapping.mapping(u, UtenteVM.class).codiceFiscale(u.getCodiceInterno())
				.username(u.getUserid());
	}
	public static UtenteDTO mappingToDTO(Utente u) {
		UtenteDTO dto=Mapping.mapping(u, UtenteDTO.class);
		dto.setCodiceFiscale(u.getCodiceInterno());
		dto.setUsername(u.getUserid());
		return dto;
	}
	public static Utente mappingFromDTO(UtenteDTO dto) {
		Utente u=Mapping.mapping(dto,Utente.class);
		u.setCodiceInterno(dto.getCodiceFiscale());
		u.setUserid(dto.getUsername());
		return u;
	}
	public static List<UtenteDTO> mappingItemsToDTO(List<Utente> items) {
		final List<UtenteDTO> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToDTO(r)));
		return out;
	}
	

}
