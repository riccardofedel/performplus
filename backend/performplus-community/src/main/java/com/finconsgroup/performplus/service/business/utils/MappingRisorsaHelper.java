package com.finconsgroup.performplus.service.business.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.finconsgroup.performplus.domain.CategoriaRisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaNodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.enumeration.Disponibile;
import com.finconsgroup.performplus.enumeration.TipoFunzione;
import com.finconsgroup.performplus.repository.CategoriaRisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.ContrattoRisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.IncaricoRepository;
import com.finconsgroup.performplus.repository.ProfiloRisorsaUmanaRepository;
import com.finconsgroup.performplus.rest.api.vm.DecodificaEnumVM;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaRisorsaUmanaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AggiornaAmministratoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AmministratoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AmministratoreListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa.RisorsaUmanaNodoPianoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.RisorsaUmanaOrganizzazioneListVM;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.RisorsaUmanaHelper;

public class MappingRisorsaHelper {
	private MappingRisorsaHelper() {
	}

	public static RisorsaUmanaDetailVM mappingToDetail(final RisorsaUmana r) {
		if (r == null)
			return null;
		final RisorsaUmanaDetailVM out = Mapping.mapping(r, RisorsaUmanaDetailVM.class);
		out.setInterno(!Boolean.TRUE.equals(r.getEsterna()));
		out.setIdCategoria(r.getCategoria() == null ? null : r.getCategoria().getId());
		out.setIdProfilo(r.getProfilo() == null ? null : r.getProfilo().getId());
		out.setIdContratto(r.getContratto() == null ? null : r.getContratto().getId());
		out.setIdIncarico(r.getIncarico()==null?null:r.getIncarico().getId());		
		out.setDataNascita(r.getDataNascita());
		out.setInizioValidita(r.getInizioValidita());
		out.setFineValidita(r.getFineValidita());
		return out;
	}

	public static RisorsaUmanaListVM mappingToList(final RisorsaUmana r) {
		final RisorsaUmanaListVM out = Mapping.mapping(r, RisorsaUmanaListVM.class);
		out.setInterno(!Boolean.TRUE.equals(r.getEsterna()));
		out.setCategoria(r.getCategoria() == null ? null : r.getCategoria().getDescrizione());
		out.setProfilo(r.getProfilo() == null ? null : r.getProfilo().getDescrizione());
		out.setContratto(r.getContratto() == null ? null : r.getContratto().getDescrizione());
		out.setIncarico(r.getIncarico() == null ? null : r.getIncarico().getDescrizione());
		out.setInizioValidita(r.getInizioValidita());
		out.setFineValidita(r.getFineValidita());
		return out;
	}

	public static RisorsaUmana mappingFromRequest(final AggiornaRisorsaUmanaRequest r,
			final CategoriaRisorsaUmanaRepository categoriaRisorsaUmanaRepository,
			final ProfiloRisorsaUmanaRepository profiloRisorsaUmanaRepository,
			final ContrattoRisorsaUmanaRepository contrattoRisorsaUmanaRepository, 
			final IncaricoRepository incaricoRepository) {
		final RisorsaUmana out = Mapping.mapping(r, RisorsaUmana.class);
		out.setEsterna(Boolean.FALSE.equals(r.getInterno()));
		out.setPolitico(false);
		out.setCategoria(r.getIdCategoria() == null ? null
				: categoriaRisorsaUmanaRepository.findById(r.getIdCategoria()).orElse(null));
		out.setProfilo(r.getIdProfilo() == null ? null
				: profiloRisorsaUmanaRepository.findById(r.getIdProfilo()).orElse(null));
		out.setContratto(r.getIdContratto() == null ? null
				: contrattoRisorsaUmanaRepository.findById(r.getIdContratto()).orElse(null));
		out.setIncarico(r.getIdIncarico() == null ? null
				: incaricoRepository.findById(r.getIdIncarico()).orElse(null));
		out.setDataNascita(r.getDataNascita());
		out.setInizioValidita(r.getInizioValidita());
		out.setFineValidita(r.getFineValidita());
		return out;
	}

	public static AmministratoreDetailVM mappingToDetailAmm(final RisorsaUmana r) {
		if (r == null)
			return null;
		final AmministratoreDetailVM out = Mapping.mapping(r, AmministratoreDetailVM.class);
		out.setFunzione(r.getFunzione()==null?null:new DecodificaEnumVM<TipoFunzione>(r.getFunzione()));
		out.setDataNascita(r.getDataNascita());
		return out;
	}

	public static AmministratoreListVM mappingToListAmm(final RisorsaUmana r) {
		return Mapping.mapping(r, AmministratoreListVM.class);
	}

	public static RisorsaUmana mappingFromRequest(final AggiornaAmministratoreRequest r) {
		final RisorsaUmana out = Mapping.mapping(r, RisorsaUmana.class);
		out.setPolitico(true);
		out.setDataNascita(r.getDataNascita());
		return out;
	}

	public static List<RisorsaUmanaListVM> mappingItemsToList(List<RisorsaUmana> items) {
		final List<RisorsaUmanaListVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToList(r)));
		return out;
	}

	public static List<AmministratoreListVM> mappingItemsToListAmm(List<RisorsaUmana> items) {
		final List<AmministratoreListVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(r -> out.add(mappingToListAmm(r)));
		return out;
	}

	public static List<RisorsaUmanaOrganizzazioneListVM> mappingItemsRisorsaUmanaOrganizzazioneToList(
			List<RisorsaUmanaOrganizzazione> items) {
		final List<RisorsaUmanaOrganizzazioneListVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(ro -> out.add(mappingUmanaOrganizzazioneToList(ro)));
		return out;
	}

	public static RisorsaUmanaOrganizzazioneListVM mappingUmanaOrganizzazioneToList(RisorsaUmanaOrganizzazione ro) {
		final RisorsaUmana r = ro.getRisorsaUmana();
		final RisorsaUmanaOrganizzazioneListVM out = Mapping.mapping(r, RisorsaUmanaOrganizzazioneListVM.class);
		out.setInterno(!Boolean.TRUE.equals(r.getEsterna()));
		out.setCategoria(r.getCategoria() == null ? null : r.getCategoria().getDescrizione());
		out.setProfilo(r.getProfilo() == null ? null : r.getProfilo().getDescrizione());
		out.setContratto(r.getContratto() == null ? null : r.getContratto().getDescrizione());
		out.setIncarico(r.getIncarico() == null ? null : r.getIncarico().getDescrizione());
		out.setId(ro.getId());
		out.setIdRisorsa(r.getId());
		out.setIdOrganizzazione(ro.getOrganizzazione().getId());
		out.setDisponibilita(ro.getDisponibilita());
		out.setInizioValidita(ro.getInizioValidita());	
		out.setFineValidita(ro.getFineValidita());		
		return out;

	}

	public static String getCognomeNome(RisorsaUmana r) {
		if (r == null)
			return "";
		return RisorsaUmanaHelper.getCognomeNomeMatricola(r);
	}

	public static List<RisorsaUmanaNodoPianoListVM> mappingItemsRisorsaUmanaNodoPianoToList(
			List<RisorsaUmanaNodoPiano> items) {
		final List<RisorsaUmanaNodoPianoListVM> out = new ArrayList<>();
		if (items == null)
			return out;
		items.forEach(ro -> out.add(mappingUmanaNodoPianoToList(ro)));
		return out;
	}

	public static RisorsaUmanaNodoPianoListVM mappingUmanaNodoPianoToList(RisorsaUmanaNodoPiano rnp) {
		final RisorsaUmana r = rnp.getRisorsaUmana();
		final RisorsaUmanaNodoPianoListVM out = Mapping.mapping(r, RisorsaUmanaNodoPianoListVM.class);
		out.setInterno(!Boolean.TRUE.equals(r.getEsterna()));
		out.setCategoria(r.getCategoria() == null ? null : r.getCategoria().getDescrizione());
		out.setProfilo(r.getProfilo() == null ? null : r.getProfilo().getDescrizione());
		out.setContratto(r.getContratto() == null ? null : r.getContratto().getDescrizione());
		out.setIncarico(r.getIncarico()== null ? null : r.getIncarico().getDescrizione());
		out.setId(rnp.getId());
		out.setIdRisorsa(r.getId());
		out.setIdNodoPiano(rnp.getNodoPiano().getId());
		out.setDisponibilita(rnp.getDisponibilita()==null?null:rnp.getDisponibilita().intValue());
		return out;

	}
}
