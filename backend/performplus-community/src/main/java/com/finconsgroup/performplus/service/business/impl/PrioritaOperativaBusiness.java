package com.finconsgroup.performplus.service.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.PrioritaOperativa;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.PrioritaOperativaRepository;
import com.finconsgroup.performplus.service.business.IPrioritaOperativaBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.ModelHelper;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.PrioritaOperativaDTO;

@Service
@Transactional
public class PrioritaOperativaBusiness implements IPrioritaOperativaBusiness {
	@Autowired
	private PrioritaOperativaRepository prioritaOperativaManager;
	@Autowired
	private NodoPianoRepository nodoPianoRepository;

	@Override

	public PrioritaOperativaDTO crea(PrioritaOperativaDTO dto) throws BusinessException {
		PrioritaOperativa po = prioritaOperativaManager
				.findByOrganizzazioneIdAndNodoPianoId(dto.getOrganizzazione().getId(), dto.getNodoPiano().getId());
		if (po != null) {
			dto.setId(po.getId());
			return aggiorna(dto);
		}
		verifica(dto);
		dto.setId(null);
		return Mapping.mapping(prioritaOperativaManager.save(Mapping.mapping(dto, PrioritaOperativa.class)),
				PrioritaOperativaDTO.class);
	}

	@Override

	public PrioritaOperativaDTO aggiorna(PrioritaOperativaDTO dto) throws BusinessException {
		verifica(dto);
		return Mapping.mapping(prioritaOperativaManager.save(Mapping.mapping(dto, PrioritaOperativa.class)),
				PrioritaOperativaDTO.class);
	}

	private void verifica(PrioritaOperativaDTO dto) throws BusinessException {
		double a = 0d;
		PrioritaOperativa old = null;
		if (dto.getId() != null) {
			old = prioritaOperativaManager.findById(dto.getId()).orElse(null);
			a = old == null || old.getPriorita() == null ? 0d : old.getPriorita().doubleValue();
		}
		double b = dto.getPriorita() == null ? 0d : dto.getPriorita().doubleValue();
		if (a != b) {
			double pesoTotale = sommaPeso(dto.getOrganizzazione());
			int delta = ModelHelper.delta(pesoTotale - a + b, 100d);
			if (delta > 0) {
				throw new BusinessException("Priorità restante " + ModelHelper.toStringDec(100d - pesoTotale + a, 2));
			}
		}

	}

	private double sommaPeso(OrganizzazioneDTO organizzazione) throws BusinessException {
		List<PrioritaOperativa> list = prioritaOperativaManager
				.findByOrganizzazioneIdAndNodoPianoDateDeleteIsNull(organizzazione.getId());
		if (list == null || list.isEmpty())
			return 0d;
		double tot = 0d;
		for (PrioritaOperativa np : list) {
			if (np.getPriorita() != null) {
				tot += np.getPriorita().doubleValue();
			}
		}
		return tot;
	}

	@Override

	public void elimina(PrioritaOperativaDTO dto) throws BusinessException {
		prioritaOperativaManager.deleteById(dto.getId());
	}

	@Override

	public void elimina(Long id) throws BusinessException {
		prioritaOperativaManager.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PrioritaOperativaDTO leggi(Long id) throws BusinessException {
		return Mapping.mapping(prioritaOperativaManager.findById(id), PrioritaOperativaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PrioritaOperativaDTO> cerca(PrioritaOperativaDTO parametri) throws BusinessException {
		return Mapping.mapping(
				prioritaOperativaManager.findAll(Example.of(Mapping.mapping(parametri, PrioritaOperativa.class))),
				PrioritaOperativaDTO.class);

	}

	@Override
	@Transactional(readOnly = true)
	public List<PrioritaOperativaDTO> list(Long idEnte, Integer anno) throws BusinessException {
		return Mapping.mapping(prioritaOperativaManager
				.findByOrganizzazioneIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(idEnte, anno),
				PrioritaOperativaDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte, Integer anno) throws BusinessException {
		return prioritaOperativaManager.countByOrganizzazioneIdEnteAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(idEnte,
				anno);
	}

	@Override
	@Transactional(readOnly = true)
	public PrioritaOperativaDTO getPrioritaOperativa(Long idOrganizzazione, Long idNodo) throws BusinessException {
		if (idOrganizzazione == null || idNodo == null)
			throw new BusinessException("area e nodo devono essere valorizzati");
		return Mapping.mapping(prioritaOperativaManager.findByOrganizzazioneIdAndNodoPianoId(idOrganizzazione, idNodo),
				PrioritaOperativaDTO.class);
	}

	@Override
	public void clona(Long idNodo, Long idClone) throws BusinessException {
		List<PrioritaOperativa> items = prioritaOperativaManager.findByNodoPianoId(idNodo);
		if (items == null || items.isEmpty())
			return;
		NodoPiano nodoClone = nodoPianoRepository.getById(idClone);
		for (PrioritaOperativa p : items) {
			clonaPriorita(p, nodoClone);
		}

	}

	private void clonaPriorita(PrioritaOperativa priorita, NodoPiano nodoClone) throws BusinessException {
		PrioritaOperativa clone = Mapping.mapping(priorita, PrioritaOperativa.class);
		clone.setId(null);
		clone.setNodoPiano(nodoClone);
		prioritaOperativaManager.save(clone);
	}
}
