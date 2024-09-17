package com.finconsgroup.performplus.service.business.pi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.CategoriaRisorsaUmana;
import com.finconsgroup.performplus.domain.Incarico;
import com.finconsgroup.performplus.domain.Regolamento;
import com.finconsgroup.performplus.pi.utils.MappingRegolamento;
import com.finconsgroup.performplus.repository.CategoriaRisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.IncaricoRepository;
import com.finconsgroup.performplus.repository.RegistrazioneRepository;
import com.finconsgroup.performplus.repository.RegolamentoRepository;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaRegolamentoRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaRegolamentoRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.RegolamentoVM;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.pi.IRegolamentoBusiness;
import com.finconsgroup.performplus.service.business.utils.Mapping;

@Service
@Transactional
public class RegolamentoBusiness implements IRegolamentoBusiness {
	@Autowired
	private RegolamentoRepository regolamentoRepository;
	@Autowired
	private CategoriaRisorsaUmanaRepository categoriaRisorsaUmanaRepository;
	@Autowired
	private IncaricoRepository incaricoRepository;
	@Autowired
	private RegistrazioneRepository registrazioneRepository;

	@Override
	@Transactional(readOnly = true)
	public List<RegolamentoVM> list(Long idEnte, Integer anno) throws BusinessException {
		List<Regolamento> x = regolamentoRepository.findByIdEnteAndAnnoOrderByIntestazione(idEnte, anno);
		return x == null ? null : x.stream().map(t -> MappingRegolamento.mapping(t)).collect(Collectors.toList());
	}

	@Override
	public void elimina(Long id) throws BusinessException {
		Regolamento regolamento = regolamentoRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Regolamento not trovata:" + id));
		long count = registrazioneRepository.countByRegolamento(regolamento);
		if (count > 0) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "regolamento utilizzato");
		}

		regolamentoRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public RegolamentoVM leggi(Long id) throws BusinessException {
		Regolamento regolamento = regolamentoRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Regolamento not trovata:" + id));
		return MappingRegolamento.mapping(regolamento);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaVM> getCategorie(Long idEnte) throws BusinessException {
		return categoriaRisorsaUmanaRepository.findByIdEnte(idEnte).stream()
				.map(t -> new DecodificaVM(t.getId(), t.getCodice(), t.getDescrizione())).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<DecodificaVM> getIncarichi(Long idEnte) {
		return incaricoRepository.findByIdEnte(idEnte).stream()
				.map(t -> new DecodificaVM(t.getId(), t.getCodice(), t.getDescrizione())).collect(Collectors.toList());
	}

	@Override
	public RegolamentoVM crea(CreaRegolamentoRequest request) throws BusinessException {
		Regolamento r = regolamentoRepository.findByIdEnteAndAnnoAndIntestazione(request.getIdEnte(), request.getAnno(),
				request.getIntestazione());
		if (r != null) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Esiste regolamento " + request.getAnno()
					+ " con uguale intestazione:" + request.getIntestazione());
		}
		r = Mapping.mapping(request, Regolamento.class);
		if (request.getCategorie() != null) {
			final List<CategoriaRisorsaUmana> items = new ArrayList<>();
			for (Long id : request.getCategorie()) {
				CategoriaRisorsaUmana c = categoriaRisorsaUmanaRepository.findById(id)
						.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Categoria non trovata"));
				items.add(c);
			}
			r.setCategorie(items);
		}
		if (request.getIncarichi() != null) {
			final List<Incarico> items = new ArrayList<>();
			for (Long id : request.getIncarichi()) {
				Incarico c = incaricoRepository.findById(id)
						.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Incarico non trovato"));
				items.add(c);
			}
			r.setIncarichi(items);

		}
		r = regolamentoRepository.save(r);
		return MappingRegolamento.mapping(r);
	}

	@Override
	public void aggiorna(Long id, AggiornaRegolamentoRequest request) throws BusinessException {
		Regolamento r = regolamentoRepository.findById(id)
				.orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Regolamento not trovata:" + id));
		Regolamento r1 = regolamentoRepository.findByIdEnteAndAnnoAndIntestazione(r.getIdEnte(), r.getAnno(),
				request.getIntestazione());
		if (r1 != null && !r1.getId().equals(r.getId())) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Esiste regolamento " + r.getAnno()
					+ " con uguale intestazione:" + request.getIntestazione());
		}
		r.setIntestazione(request.getIntestazione());		
		r.setPesoComportamentiOrganizzativi(request.getPesoComportamentiOrganizzativi());
		r.setPesoObiettiviDiStruttura(request.getPesoObiettiviDiStruttura());
		r.setPesoObiettiviDiPerformance(request.getPesoObiettiviDiPerformance());
		r.setPesoObiettiviIndividuali(request.getPesoObiettiviIndividuali());
		r.setPo(request.getPo());
		if (request.getCategorie() != null) {
			final List<CategoriaRisorsaUmana> items = new ArrayList<>();
			for (Long idc : request.getCategorie()) {
				CategoriaRisorsaUmana c = categoriaRisorsaUmanaRepository.findById(idc)
						.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Categoria non trovata"));
				items.add(c);
			}
			r.setCategorie(items);
		}else {
			r.setCategorie(null);
		}
		if (request.getIncarichi() != null) {
			final List<Incarico> items = new ArrayList<>();
			for (Long idi : request.getIncarichi()) {
				Incarico c = incaricoRepository.findById(idi)
						.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Incarico non trovato"));
				items.add(c);
			}
			r.setIncarichi(items);

		}else {
			r.setIncarichi(null);
		}
		regolamentoRepository.save(r);
		
	}

}
