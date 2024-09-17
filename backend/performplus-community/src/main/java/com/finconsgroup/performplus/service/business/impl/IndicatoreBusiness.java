package com.finconsgroup.performplus.service.business.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.Indicatore;
import com.finconsgroup.performplus.enumeration.RaggruppamentoIndicatori;
import com.finconsgroup.performplus.enumeration.TipoFormula;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.IndicatoreRepository;
import com.finconsgroup.performplus.rest.api.vm.v2.IndicatoreTemplateVM;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.AggiornaIndicatoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.CreaIndicatoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.IndicatoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.indicatore.IndicatoreListVM;
import com.finconsgroup.performplus.service.business.IIndicatoreBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;
import com.finconsgroup.performplus.service.dto.IndicatoreDTO;
import com.finconsgroup.performplus.service.dto.RicercaIndicatori;

@Service
@Transactional
public class IndicatoreBusiness implements IIndicatoreBusiness {
	@Autowired
	private IndicatoreRepository indicatoreRepository;
	@Autowired
	private IndicatorePianoRepository indicatorePianoRepository;

	@Override
	
	public IndicatoreDTO crea(final IndicatoreDTO dto
			 ) throws BusinessException {
		dto.setId(null);
		return Mapping.mapping(indicatoreRepository.save(Mapping.mapping(dto,Indicatore.class)),IndicatoreDTO.class);
	}

	@Override
	
	public IndicatoreDTO aggiorna(final IndicatoreDTO dto
			 ) throws BusinessException {
		return Mapping.mapping(indicatoreRepository.save(Mapping.mapping(dto,Indicatore.class)),IndicatoreDTO.class);
	}

	

	@Override
	
	public void elimina(final IndicatoreDTO dto
			) throws BusinessException {
		 indicatoreRepository.deleteById(dto.getId());
	}

	@Override
	public void elimina(final Long id)
			throws BusinessException {
		 if(indicatorePianoRepository.existsByIndicatoreIdAndNodoPianoDateDeleteIsNull(id)) {
			 throw new BusinessException(HttpStatus.CONFLICT, "Indicatore in uso");
		 }
		 indicatoreRepository.deleteById(id);
	}


	@Override
	@Transactional(readOnly = true)
	public IndicatoreDTO leggi(final Long id )
			throws BusinessException {
		return Mapping.mapping(indicatoreRepository.findById(id),IndicatoreDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndicatoreDTO> cerca(final IndicatoreDTO parametri ) throws BusinessException {
		return Mapping.mapping(indicatoreRepository.findAll(Example.of(Mapping.mapping(parametri,Indicatore.class))),IndicatoreDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndicatoreDTO> list(final Long idEnte) throws BusinessException {
		return Mapping.mapping(indicatoreRepository.findByIdEnte(0l),IndicatoreDTO.class);

	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte)
			throws BusinessException {
		return indicatoreRepository.countByIdEnte(0l);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndicatoreDTO> cercaPerRaggruppamento(Long idEnte,
			RaggruppamentoIndicatori raggruppamento) throws BusinessException {
		return Mapping.mapping(indicatoreRepository.findByIdEnteAndRaggruppamentoOrderByDenominazione(0l,
				raggruppamento),IndicatoreDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IndicatoreDTO> cerca(RicercaIndicatori parametri)
			throws BusinessException {
		return Mapping.mapping(indicatoreRepository.cerca(parametri),IndicatoreDTO.class);
	}
	@Override
	@Transactional(readOnly = true)
	public IndicatoreDTO leggiPerNome(final Long idEnte, final String nome)
			throws BusinessException {
		return Mapping.mapping(indicatoreRepository.findByIdEnteAndDenominazione(0l,nome),IndicatoreDTO.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<IndicatoreDTO> cercaStrategico() throws BusinessException {
		return Mapping.mapping(indicatoreRepository.findByIdEnteAndRaggruppamentoInOrderByDenominazione(0l,Arrays.asList(new RaggruppamentoIndicatori[] {RaggruppamentoIndicatori.STRATEGICO,RaggruppamentoIndicatori.STRATEGICO_SOSTENIBILE})),IndicatoreDTO.class);
	}

	@Transactional(readOnly = true)
	@Override
	public List<IndicatoreDTO> cercaSviluppoSostenibile() throws BusinessException {
		return Mapping.mapping(indicatoreRepository.findByIdEnteAndRaggruppamentoInOrderByDenominazione(0l,Arrays.asList(new RaggruppamentoIndicatori[] {RaggruppamentoIndicatori.SVILUPPO_SOSTENIBILE,RaggruppamentoIndicatori.STRATEGICO_SOSTENIBILE})),IndicatoreDTO.class);
	}
	@Transactional(readOnly = true)
	@Override
	public List<IndicatoreDTO> cercaPersonalizzabili() throws BusinessException {
		return Mapping.mapping(indicatoreRepository.findByIdEnteAndRaggruppamentoOrderByDenominazione(0l,RaggruppamentoIndicatori.SPECIFICO),IndicatoreDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<IndicatoreListVM> searchIndicatore(Long idEnte, String denominazione, TipoFormula formula,
			RaggruppamentoIndicatori raggruppamento, Pageable pageable) throws BusinessException {
		RicercaIndicatori pars=new RicercaIndicatori();
		pars.setFormula(formula);
		pars.setRaggruppamento(raggruppamento);
		pars.setIdEnte(idEnte);
		pars.setTestoRicerca(denominazione);
		Page<Indicatore> page = indicatoreRepository.search(pars, pageable);
		Page<IndicatoreListVM> out = page.map(new Function<Indicatore, IndicatoreListVM>() {
			@Override
			public IndicatoreListVM apply(Indicatore r) {
				return mappingToList(r);
			}


		});
		return out;
	}



	@Override
	public IndicatoreDetailVM creaIndicatore(CreaIndicatoreRequest request) throws BusinessException {
		Indicatore r=Mapping.mapping(request, Indicatore.class);
		r.setTipoFormula(request.getFormula());
		r=indicatoreRepository.save(r);
		return mappingToDetail(r);
	}

	@Override
	public void aggiornaIndicatore(Long id, AggiornaIndicatoreRequest request) throws BusinessException {
		final Indicatore r=indicatoreRepository.findById(id).orElseThrow(()->
		new BusinessException(HttpStatus.NOT_FOUND,"Indicatore non trovato:"+id));
		Mapping.mapping(request, r);
		r.setTipoFormula(request.getFormula());
		indicatoreRepository.save(r);
	}

	@Override
	@Transactional(readOnly = true)
	public IndicatoreDetailVM leggiIndicatore(Long id) throws BusinessException {
		return mappingToDetail(indicatoreRepository.findById(id).orElse(null));
	}
	
	private IndicatoreListVM mappingToList(final Indicatore r) {
		final IndicatoreListVM out=new IndicatoreListVM();
		out.setId(r.getId());
		out.setDenominazione(r.getDenominazione());
		out.setPercentuale(r.getPercentuale());
		out.setRaggruppamento(ModelHelper.normalize(r.getRaggruppamento()));
		out.setFormula(ModelHelper.normalize(r.getTipoFormula()));
		out.setTipoCalcoloConsuntivazione(r.getCalcoloConsuntivazione());	
		out.setTipoFormula(r.getTipoFormula());
		out.setCalcoloConsuntivazione(ModelHelper.normalize(r.getCalcoloConsuntivazione()));
		return out;
	}
	private IndicatoreDetailVM mappingToDetail(final Indicatore r) {
		if(r==null)
			return null;
		final IndicatoreDetailVM out=Mapping.mapping(r,IndicatoreDetailVM.class);
		out.setFormula(r.getTipoFormula());
		return out;
	}

	@Override
	public List<IndicatoreTemplateVM> templates(Long idEnte) throws BusinessException {
		final List<IndicatoreTemplateVM> out=new ArrayList<>();
		List<Indicatore> items=indicatoreRepository.findByIdEnteOrderByDenominazione(idEnte);
		if(items!=null) {
			for (Indicatore ind : items) {
				out.add(new IndicatoreTemplateVM(ind.getId(),ind.getDenominazione(),
						ind.getTipoFormula()));
			}
		}
		return out;
	}

}
