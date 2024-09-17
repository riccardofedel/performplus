package com.finconsgroup.performplus.service.business.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.RisorsaStrumentale;
import com.finconsgroup.performplus.domain.RisorsaStrumentaleOrganizzazione;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaStrumentaleOrganizzazioneRepository;
import com.finconsgroup.performplus.repository.RisorsaStrumentaleRepository;
import com.finconsgroup.performplus.service.business.IRisorsaStrumentaleBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.RisorsaStrumentaleDTO;
import com.finconsgroup.performplus.service.dto.RisorsaStrumentaleOrganizzazioneDTO;

@Service
@Transactional
public class RisorsaStrumentaleBusiness implements IRisorsaStrumentaleBusiness {
	@Autowired
	private RisorsaStrumentaleRepository risorsaStrumentaleManager;
	@Autowired
	private RisorsaStrumentaleOrganizzazioneRepository risorsaStrumentaleOrganizzazioneRepository;
	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;

	@Override

	public RisorsaStrumentaleDTO crea(RisorsaStrumentaleDTO dto) throws BusinessException {
		dto.setId(null);
		return Mapping.mapping(risorsaStrumentaleManager.save(Mapping.mapping(dto, RisorsaStrumentale.class)),
				RisorsaStrumentaleDTO.class);
	}

	@Override

	public RisorsaStrumentaleDTO aggiorna(RisorsaStrumentaleDTO dto) throws BusinessException {
		return Mapping.mapping(risorsaStrumentaleManager.save(Mapping.mapping(dto, RisorsaStrumentale.class)),
				RisorsaStrumentaleDTO.class);
	}

	@Override

	public void elimina(RisorsaStrumentaleDTO dto) throws BusinessException {
		risorsaStrumentaleManager.deleteById(dto.getId());
	}

	@Override

	public void elimina(Long id) throws BusinessException {
		risorsaStrumentaleManager.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public RisorsaStrumentaleDTO leggi(Long id) throws BusinessException {
		return Mapping.mapping(risorsaStrumentaleManager.findById(id), RisorsaStrumentaleDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaStrumentaleDTO> cerca(RisorsaStrumentaleDTO parametri) throws BusinessException {
		return Mapping.mapping(
				risorsaStrumentaleManager.findAll(Example.of(Mapping.mapping(parametri, RisorsaStrumentale.class))),
				RisorsaStrumentaleDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaStrumentaleDTO> list(Long idEnte, Integer anno) throws BusinessException {
		return Mapping.mapping(risorsaStrumentaleManager.findByIdEnte(idEnte), RisorsaStrumentaleDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte, Integer anno) throws BusinessException {
		return risorsaStrumentaleManager.countByIdEnte(idEnte);
	}

	@Override
	@Transactional(readOnly = true)
	public String maxCodice(Long idEnte, Integer anno) throws BusinessException {
		return risorsaStrumentaleManager.maxCodice(idEnte, anno);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaStrumentaleDTO> cercoPerOrganizzazione(Long idOrganizzazione) {
		List<RisorsaStrumentale> list = getByOrganizzazioneId(idOrganizzazione);
		return Mapping.mapping(list, RisorsaStrumentaleDTO.class);
	}

	private List<RisorsaStrumentale> getByOrganizzazioneId(Long idOrganizzazione) {
		return getByRisorsaStrumentaleOrganizzazione(risorsaStrumentaleOrganizzazioneRepository
				.findByOrganizzazioneIdOrderByRisorsaStrumentaleCodice(idOrganizzazione));
	}
	private List<RisorsaStrumentale> getByRisorsaStrumentaleOrganizzazione(List<RisorsaStrumentaleOrganizzazione> items) {
		final List<RisorsaStrumentale> list = new ArrayList<>();
		if (items != null) {
			items.stream().forEach(r -> {
				if (!list.contains(r.getRisorsaStrumentale()))
					list.add(r.getRisorsaStrumentale());
			});
		}
		return list;
	}
	@Override
	@Transactional(readOnly = true)
	public double getTotalePerOrganizzazione(Long idOrganizzazione) throws BusinessException {
		BigDecimal totale = BigDecimal.ZERO;
		List<RisorsaStrumentaleDTO> list = cercoPerOrganizzazione(idOrganizzazione);
		if (list != null) {
			for (RisorsaStrumentaleDTO ris : list) {
				BigDecimal mult = ris.getValore();
				if (mult == null)
					mult = BigDecimal.ZERO;
				else
					mult = ris.getValore().multiply(new BigDecimal(ris.getQuantita()));
				totale = totale.add(mult);
			}
		}
		return totale.doubleValue();
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaStrumentaleDTO> getRisorseStrumentaliDiAlberoUo(Long idOrganizzazione)
			throws BusinessException {
		final List<Long> ids=new ArrayList<>();
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(()->{throw new BusinessException("Organizzazione non trovata:"+idOrganizzazione);});
		ids.add(o.getId());
		List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte()
				,o.getAnno()
				,o.getCodiceCompleto()+".");
		items.forEach(c->ids.add(c.getId()));
		return Mapping.mapping(getByRisorsaStrumentaleOrganizzazione(risorsaStrumentaleOrganizzazioneRepository
				.findByOrganizzazioneIdIn(ids)), RisorsaStrumentaleDTO.class);
	}

	@Override

	public void elimina(Long idOrganizzazione, String codice) throws BusinessException {
		RisorsaStrumentaleDTO dto = leggiPerCodice(idOrganizzazione, codice);
		if (dto == null)
			return;
		elimina(dto);
	}

	@Override
	@Transactional(readOnly = true)
	public RisorsaStrumentaleDTO leggiPerCodice(Long idOrganizzazione, String codice) throws BusinessException {
		RisorsaStrumentaleOrganizzazione rso = risorsaStrumentaleOrganizzazioneRepository
				.findByOrganizzazioneIdAndRisorsaStrumentaleCodice(idOrganizzazione, codice);
		return rso == null ? null : Mapping.mapping(rso.getRisorsaStrumentale(), RisorsaStrumentaleDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaStrumentaleDTO> cercaNonAssociate(Long idOrganizzazione) throws BusinessException {
		return cercaNonAssociate(idOrganizzazione, null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaStrumentaleDTO> cercaNonAssociate(Long idOrganizzazione, String testo)
			throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(()->{throw new BusinessException("Organizzazione non trovata:"+idOrganizzazione);});
		List<RisorsaStrumentale> items = risorsaStrumentaleManager.cercaNonAssociate(o.getIdEnte(),o.getAnno(),
				o.getId());
		if (StringUtils.isNotBlank(testo)) {
			items.removeIf(
					r -> !(r.getCodice() + " " + r.getDescrizione()).toLowerCase().contains(testo.toLowerCase()));
		}
		return Mapping.mapping(items, RisorsaStrumentaleDTO.class);

	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaStrumentaleOrganizzazioneDTO> getRisorsaStrumentaleOrganizzazione(Long idOrganizzazione)
			throws BusinessException {

		List<RisorsaStrumentaleOrganizzazione> list = risorsaStrumentaleOrganizzazioneRepository
				.findByOrganizzazioneId(idOrganizzazione);
		return Mapping.mapping(list, RisorsaStrumentaleOrganizzazioneDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaStrumentaleDTO> cerca(Long idEnte, Integer anno, String testo) throws BusinessException {
		if (StringUtils.isBlank(testo)) {
			return Mapping.mapping(risorsaStrumentaleManager.findByIdEnteAndAnno(idEnte, anno),
					RisorsaStrumentaleDTO.class);
		} else {
			return Mapping.mapping(
					risorsaStrumentaleManager.findByIdEnteAndAnnoAndDescrizioneContainsIgnoreCase(idEnte, anno, testo),
					RisorsaStrumentaleDTO.class);
		}
	}
}
