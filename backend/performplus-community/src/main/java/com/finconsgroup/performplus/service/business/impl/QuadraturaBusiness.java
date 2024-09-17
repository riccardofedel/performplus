package com.finconsgroup.performplus.service.business.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.Organizzazione;
import com.finconsgroup.performplus.domain.PrioritaOperativa;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaNodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmanaOrganizzazione;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.repository.IndicatorePianoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.PrioritaOperativaRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaNodoPianoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaOrganizzazioneRepository;
import com.finconsgroup.performplus.repository.ValutazioneRepository;
import com.finconsgroup.performplus.service.business.IConfigurazioneBusiness;
import com.finconsgroup.performplus.service.business.IPianoBusiness;
import com.finconsgroup.performplus.service.business.IQuadraturaBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.DateHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MappingNodoPianoHelper;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.OrganizzazioneEasyDTO;
import com.finconsgroup.performplus.service.dto.PianoDTO;
import com.finconsgroup.performplus.service.dto.PrioritaOperativaDTO;
import com.finconsgroup.performplus.service.dto.PrioritaRisorseDTO;
import com.finconsgroup.performplus.service.dto.QuadraturaIndicatoriProg;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorseProg;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaNodoPianoDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaPianoReport;

@Service
@Transactional
public class QuadraturaBusiness implements IQuadraturaBusiness {

	@Autowired
	private IPianoBusiness pianoBusiness;

	@Autowired
	private NodoPianoBusiness nodoPianoBusiness;
	@Autowired
	private IndicatorePianoRepository indicatorePianoRepository;
	@Autowired
	private ValutazioneRepository valutazioneRepository;
	@Autowired
	private NodoPianoRepository nodoPianoRepository;
	@Autowired
	private OrganizzazioneRepository organizzazioneRepository;
	@Autowired
	private PrioritaOperativaRepository prioritaOperativaRepository;
	@Autowired
	private IConfigurazioneBusiness configBusiness;

	@Autowired
	private RisorsaUmanaNodoPianoRepository risorsaUmanaNodoPianoRepository;
	@Autowired
	private RisorsaUmanaOrganizzazioneRepository risorsaUmanaOrganizzazioneRepository;

//	@Override
//	@Transactional(readOnly = true)
//	public List<QuadraturaRisorsaUmana> quadraturaRisorse(Long idNodo, List<Long> filtroOrganizzazioni)
//			throws BusinessException {
//		NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElse(null);
//		 NodoPiano piano = nodo.getPiano();
//		int oreSettimanaliMax = configBusiness.getOreMax(nodo.getIdEnte(), nodo.getAnno());
//		List<NodoPiano> list = nodoPianoRepository
//				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(nodo.getIdEnte()
//						, nodo.getAnno(), nodo.getCodiceCompleto());
//		List<Long> filtroNodi = null;
//		if (filtroOrganizzazioni != null) {
//			filtroNodi = new ArrayList<>();
//			PianoDTO root = pianoBusiness.getRoot(nodo.getIdEnte(), nodo.getAnno(), filtroOrganizzazioni);
//			if (root != null) {
//				List<NodoPianoDTO> items = NodoPianoHelper.contiene(root);
//				for (NodoPianoDTO np : items) {
//					filtroNodi.add(np.getId());
//				}
//			}
//		}
//		List<Long> items = new ArrayList<>();
//		estraiItems(nodo,list, items, filtroNodi);
//		List<RisorsaUmanaNodoPianoDTO> risorse = Mapping.mapping(risorsaUmanaNodoPianoRepository
//				.findByNodoPianoIdInAndRisorsaUmanaAnnoAndRisorsaUmanaPolitico(items, nodo.getAnno(), false),
//				RisorsaUmanaNodoPianoDTO.class);
//		aggiungiUnitaOrganizzative(risorse);
//		return new QuadraturaRisorsaUmanaNodoHelper().elabora(risorse, 
//				Mapping.mapping(nodo, NodoPianoDTO.class)
//				, oreSettimanaliMax);
//	}

	private void estraiItems(NodoPiano nodo,List<NodoPiano> list, List<Long> items, List<Long> filtroNodi) {
		if (list == null||nodo==null)
			return;
		if(!items.contains(nodo.getId())) {
			return;
		}
		for (NodoPiano np : list) {
			if(!items.contains(np.getId())) {
				items.add(np.getId());
			}
		}
	}

	private void aggiungiUnitaOrganizzative(List<RisorsaUmanaNodoPianoDTO> risorse) {
		if (risorse == null || risorse.isEmpty())
			return;
		listaOrganizzazioniPerRisorsa(risorse);

	}

	private void listaOrganizzazioniPerRisorsa(List<RisorsaUmanaNodoPianoDTO> risorse) {
		final List<String> doppie = new ArrayList<>();
		final List<Long> ids = new ArrayList<>();
		int anno = risorse.get(0).getNodoPiano().getAnno();
		for (RisorsaUmanaNodoPianoDTO item : risorse) {
			if (ids.size() == 256) {
				List<RisorsaUmanaOrganizzazione> list = risorsaUmanaOrganizzazioneRepository.listaPerRisorse(ids,
						DateHelper.inizioAnno(anno), DateHelper.fineAnno(anno));
				carica(ids, risorse, list);
				ids.clear();
			}
			if (doppie.contains(item.getRisorsaUmana().getId().toString()))
				continue;
			doppie.add(item.getRisorsaUmana().getId().toString());
			ids.add(item.getRisorsaUmana().getId());
		}
		if (!ids.isEmpty()) {
			List<RisorsaUmanaOrganizzazione> list = risorsaUmanaOrganizzazioneRepository.listaPerRisorse(ids,
					DateHelper.inizioAnno(anno), DateHelper.fineAnno(anno));
			carica(ids, risorse, list);
		}
	}

	private void carica(List<Long> ids, List<RisorsaUmanaNodoPianoDTO> risorse, List<RisorsaUmanaOrganizzazione> list) {
		List<OrganizzazioneEasyDTO> out = null;
		final List<String> doppi = new ArrayList<>();
		if (list == null || list.isEmpty())
			return;

		for (Long id : ids) {
			doppi.clear();
			out = new ArrayList<>();
			for (RisorsaUmanaOrganizzazione r : list) {
				if (!id.equals(r.getRisorsaUmana().getId()))
					continue;
				if (doppi.contains(r.getOrganizzazione().getId().toString()))
					continue;
				doppi.add(r.getOrganizzazione().getId().toString());
				out.add(Mapping.mapping(r.getOrganizzazione(), OrganizzazioneEasyDTO.class));
			}
			if (out.isEmpty())
				continue;
			for (RisorsaUmanaNodoPianoDTO rp : risorse) {
				if (rp.getRisorsaUmana().getId().equals(id))
					rp.getRisorsaUmana().setOrganizzazioniAppartenenza(out);
			}
		}
	}

	private void aggiungiUnitaOrganizzative(RisorsaUmanaNodoPianoDTO r) {
		int anno = r.getNodoPiano().getAnno();
		List<RisorsaUmanaOrganizzazione> items = risorsaUmanaOrganizzazioneRepository
				.listaPerRisorsa(r.getRisorsaUmana().getId(), DateHelper.inizioAnno(anno), DateHelper.fineAnno(anno));
		List<OrganizzazioneEasyDTO> organizzazioni = new ArrayList<>();
		List<Long> doppi = new ArrayList<>();
		for (RisorsaUmanaOrganizzazione ro : items) {
			if (doppi.contains(ro.getOrganizzazione().getId()))
				continue;
			doppi.add(ro.getOrganizzazione().getId());
			organizzazioni.add(Mapping.mapping(ro.getOrganizzazione(),OrganizzazioneEasyDTO.class));
		}

		r.setOrganizzazioniAppartenenza(organizzazioni);
		r.getRisorsaUmana().setOrganizzazioniAppartenenza(organizzazioni);

	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaIndicatoriProg> quadraturaProgrammazione(Long idNodo, List<Long> filtroOrganizzazioni)
			throws BusinessException {
		NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElse(null);
		
		List<Long> filtroNodi = null;
		if (filtroOrganizzazioni != null) {
			filtroNodi = new ArrayList<>();
			PianoDTO root = pianoBusiness.getRoot(nodo.getIdEnte(), nodo.getPiano().getAnno(), filtroOrganizzazioni);
			if (root != null) {
				List<NodoPianoDTO> items = NodoPianoHelper.contiene(root);
				for (NodoPianoDTO np : items) {
					filtroNodi.add(np.getId());
				}
			}
		}
		return QuadraturaIndicatoriHelper.elabora(MappingNodoPianoHelper.toDto(nodo), indicatorePianoRepository, 
				valutazioneRepository,
				filtroNodi);
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaIndicatoriProg> quadraturaProgrammazioneStampa(Long  idNodo, int oreSettimanaliMax)
			throws BusinessException {
		NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElse(null);
		return QuadraturaIndicatoriHelper.elaboraStampa(MappingNodoPianoHelper.toDto(nodo), indicatorePianoRepository, valutazioneRepository);
	}



	@Override
	@Transactional(readOnly = true)
	public List<QuadraturaRisorseProg> quadraturaRisorseStampaPerNodo(Long idNodo,
			List<Long> filtroOrganizzazioni, int oreSettimanaliMax) throws BusinessException {
		NodoPiano nodo = nodoPianoRepository.findById(idNodo).orElse(null);
		NodoPiano piano = nodo.getPiano();
		List<NodoPiano> list = nodoPianoRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithAndDateDeleteIsNullOrderByCodiceCompleto(nodo.getIdEnte()
						, nodo.getAnno(), nodo.getCodiceCompleto());
		List<Long> filtroNodi = null;
		if (filtroOrganizzazioni != null) {
			filtroNodi = new ArrayList<>();
			PianoDTO root = pianoBusiness.getRoot(piano.getIdEnte(), piano.getAnno(), filtroOrganizzazioni);
			if (root != null) {
				List<NodoPianoDTO> items = NodoPianoHelper.contiene(root);
				for (NodoPianoDTO np : items) {
					filtroNodi.add(np.getId());
				}
			}
		}
//		List<Long> items = new ArrayList<>();
//		estraiItems(nodo, list,items, filtroNodi);
//		List<RisorsaUmanaNodoPianoDTO> risorse = Mapping.mapping(risorsaUmanaNodoPianoRepository
//				.findByNodoPianoIdIn(items),RisorsaUmanaNodoPianoDTO.class);
//		aggiungiUnitaOrganizzative(risorse);
		return QuadraturaRisorsaUmanaNodoHelper.elabora(piano, nodoPianoRepository, risorsaUmanaNodoPianoRepository, risorsaUmanaOrganizzazioneRepository, filtroNodi, oreSettimanaliMax);
	}



	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaPianoReport> quadraturaRisorseStampaPerPiano(Long idPiano,
			List<Long> filtroOrganizzazioni, int oreSettimanaliMax) throws BusinessException {
		ArrayList<RisorsaUmanaPianoReport> out = new ArrayList<>();
		NodoPiano piano = nodoPianoRepository.findById(idPiano).orElse(null);
		List<NodoPiano> list = nodoPianoRepository
				.findByIdEnteAndAnnoAndCodiceCompletoStartsWithAndDateDeleteIsNullOrderByCodiceCompleto(piano.getIdEnte()
						, piano.getAnno(), piano.getCodiceCompleto());
		List<Long> filtroNodi = null;
		if (filtroOrganizzazioni != null) {
			filtroNodi = new ArrayList<>();
			PianoDTO root = pianoBusiness.getRoot(piano.getIdEnte(), piano.getAnno(), filtroOrganizzazioni);
			if (root != null) {
				List<NodoPianoDTO> items = NodoPianoHelper.contiene(root);
				for (NodoPianoDTO np : items) {
					filtroNodi.add(np.getId());
				}
			}
		}

		List<Long> items = new ArrayList<>();
		estraiItems(piano, list, items, filtroNodi);
		for (NodoPiano np : list) {
			if (!NodoPianoHelper.contenuto(piano, items))
				continue;

			if (TipoNodo.PIANO.equals(np.getTipoNodo()))
				continue;
			List<RisorsaUmanaNodoPianoDTO> risorse = null;
			if (np.getTipoNodo().ordinal() >= TipoNodo.OBIETTIVO.ordinal()) {
				risorse = Mapping.mapping(risorsaUmanaNodoPianoRepository.findByNodoPianoId(np.getId()),RisorsaUmanaNodoPianoDTO.class);
			}
			List<String> doppi = new ArrayList<>();

			if (risorse != null && !risorse.isEmpty()) {
				for (RisorsaUmanaNodoPianoDTO r : risorse) {
					if (doppi.contains(r.getRisorsaUmana().getId().toString()))
						continue;
					doppi.add(r.getRisorsaUmana().getId().toString());
					aggiungiUnitaOrganizzative(r);
					out.add(new RisorsaUmanaPianoReport(MappingNodoPianoHelper.toDto(np), r));
				}

			} else {
				out.add(new RisorsaUmanaPianoReport(MappingNodoPianoHelper.toDto(np)));
			}
		}

		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PrioritaRisorseDTO> getPrioritaRisorseNodi(Long idOrganizzazione)
			throws BusinessException {
		Organizzazione o = organizzazioneRepository.findById(idOrganizzazione).orElseThrow(()->{throw new BusinessException("organizzazione non trovasta:"+idOrganizzazione);});
		// leggo risorse per organizzazione e programmi associati a risorse
		List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(o.getIdEnte(), o.getAnno(), o.getCodiceCompleto());
		final List<Long> ids=new ArrayList<>();
		ids.add(o.getId());
		items.stream().forEach(c->ids.add(c.getId()));
		List<RisorsaUmanaOrganizzazione> risorse = risorsaUmanaOrganizzazioneRepository.findByOrganizzazioneIdInAndRisorsaUmanaPoliticoIsFalse(ids);
		List<PrioritaOperativa> priorities = prioritaOperativaRepository.findByOrganizzazioneIdInAndNodoPianoDateDeleteIsNull(ids);
		List<String> doppi = new ArrayList<>();
		List<Long> ris = new ArrayList<>();
		List<RisorsaUmana> risorseUmane = new ArrayList<>();
		if (risorse != null) {
			for (RisorsaUmanaOrganizzazione ruo : risorse) {
				if (doppi.contains(ruo.getRisorsaUmana().getId().toString()))
					continue;
				ris.add(ruo.getRisorsaUmana().getId());
				risorseUmane.add(ruo.getRisorsaUmana());
				doppi.add(ruo.getRisorsaUmana().getId().toString());
			}
		}
		Map<Long, PrioritaRisorseDTO> map = new HashMap<>();
		List<RisorsaUmanaNodoPiano> nodi = risorsaUmanaNodoPianoRepository.findByRisorsaUmanaIdInAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(ris, o.getAnno());
		List<PrioritaRisorseDTO> out = new ArrayList<>();
		PrioritaRisorseDTO pr = null;
		doppi.clear();
		if (nodi != null) {
			for (RisorsaUmanaNodoPiano rn : nodi) {
				String key = rn.getNodoPiano().getId() + "/" + rn.getRisorsaUmana().getId();
				if (doppi.contains(key))
					continue;
				pr = map.get(rn.getNodoPiano().getId());
				if (pr == null) {
					pr = new PrioritaRisorseDTO(Mapping.mapping(risorseUmane,RisorsaUmanaDTO.class));
					pr.setNodoPiano(MappingNodoPianoHelper.toDto(rn.getNodoPiano()));
					pr.setRisorse(new ArrayList<>());
					pr.setPrioritaOperativa(Mapping.mapping(trova(priorities, pr.getNodoPiano(), Mapping.mapping(o, OrganizzazioneDTO.class)),PrioritaOperativaDTO.class));
					map.put(rn.getNodoPiano().getId(), pr);
					out.add(pr);
				}
				pr.addRisorsa(Mapping.mapping(rn,RisorsaUmanaNodoPianoDTO.class));
				doppi.add(key);
			}

		}
		out.sort(Comparator.naturalOrder());
		return out;
	}

	private PrioritaOperativa trova(List<PrioritaOperativa> priorities, NodoPianoDTO np, OrganizzazioneDTO org) {
		if (priorities == null || priorities.isEmpty())
			return null;
		for (PrioritaOperativa po : priorities) {
			if (po.getOrganizzazione().getCodiceCompleto().startsWith(org.getCodiceCompleto())
					&& po.getNodoPiano().getId().equals(np.getId()))
				return po;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<RisorsaUmanaNodoPianoDTO> getPrioritaRisorseSettore(Long idSettore)
			throws BusinessException {
		Organizzazione settore = organizzazioneRepository.findById(idSettore).orElseThrow(()->{throw new BusinessException("organizzazione non trovasta:"+idSettore);});
		List<Organizzazione> items = organizzazioneRepository.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(settore.getIdEnte(), settore.getAnno(), settore.getCodiceCompleto());
		final List<Long> ids=new ArrayList<>();
		ids.add(settore.getId());
		items.stream().forEach(c->ids.add(c.getId()));
		List<RisorsaUmanaOrganizzazione> risorse = risorsaUmanaOrganizzazioneRepository.findByOrganizzazioneIdInAndRisorsaUmanaPoliticoIsFalse(ids);
		List<Long> doppi = new ArrayList<>();
		List<Long> ris = new ArrayList<>();
		if (risorse != null) {
			for (RisorsaUmanaOrganizzazione ruo : risorse) {
				if (doppi.contains(ruo.getRisorsaUmana().getId()))
					continue;
				ris.add(ruo.getRisorsaUmana().getId());
				doppi.add(ruo.getRisorsaUmana().getId());
			}
		}
		List<RisorsaUmanaNodoPianoDTO> list = Mapping.mapping(risorsaUmanaNodoPianoRepository.findByRisorsaUmanaIdInAndNodoPianoAnnoAndNodoPianoDateDeleteIsNull(ris, settore.getAnno()),RisorsaUmanaNodoPianoDTO.class);
		if (list != null)
			list.sort(Comparator.naturalOrder());
		return list;
	}

}
