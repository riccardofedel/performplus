package com.finconsgroup.performplus.service.business.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.domain.RisorsaUmana;
import com.finconsgroup.performplus.domain.RisorsaUmanaNodoPiano;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.enumeration.Livello;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.repository.OrganizzazioneRepository;
import com.finconsgroup.performplus.repository.PrioritaOperativaRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaNodoPianoRepository;
import com.finconsgroup.performplus.repository.RisorsaUmanaRepository;
import com.finconsgroup.performplus.rest.api.vm.PianoVM;
import com.finconsgroup.performplus.service.business.IPianoBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.NodoPianoHelper;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.PianoDTO;
import com.finconsgroup.performplus.service.dto.PianoEasyDTO;
import com.finconsgroup.performplus.service.dto.PrioritaOperativaDTO;
import com.finconsgroup.performplus.service.dto.PrioritaOperativaItem;
import com.finconsgroup.performplus.service.dto.PrioritaOperativaReport;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaNodoPianoDTO;
import com.finconsgroup.performplus.service.dto.UtilizzoRisorsaItem;

@Service
@Transactional
public class PianoBusiness implements IPianoBusiness {
	@Autowired
	private NodoPianoRepository nodoPianoManager;
	@Autowired
	private OrganizzazioneRepository organizzazioneManager;
	@Autowired
	private PrioritaOperativaRepository prioritaOperativaManager;
	@Autowired
	private RisorsaUmanaRepository risorsaUmanaManager;

	@Autowired
	private RisorsaUmanaNodoPianoRepository risorsaUmanaNodoPianoManager;

	public static final String SUFFIX_PRS_VARIATO = "_var";

	@Override
	@Transactional(readOnly = true)
	public PianoDTO getPiano(Long idEnte, String codice) throws BusinessException {
		return Mapping.mapping(nodoPianoManager.findTopByIdEnteAndCodiceAndTipoNodoAndDateDeleteIsNull(idEnte, codice, TipoNodo.PIANO),
				PianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public PianoVM getPiano(Long idEnte, int anno) throws BusinessException {
		try {
		    NodoPiano np = nodoPianoManager.getPiano(idEnte, anno);
		   PianoEasyDTO tmp = Mapping.mapping(np, PianoEasyDTO.class);
			return Mapping.mapping(tmp, PianoVM.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
	}

	@Override
	public PianoDTO crea(PianoDTO dto) throws BusinessException {
		dto.setId(null);
		return Mapping.mapping(nodoPianoManager.save(Mapping.mapping(dto, NodoPiano.class)), PianoDTO.class);
	}

	@Override

	public PianoDTO aggiorna(PianoDTO dto) throws BusinessException {
		return Mapping.mapping(nodoPianoManager.save(Mapping.mapping(dto, NodoPiano.class)), PianoDTO.class);
	}

	@Override

	public void elimina(PianoDTO dto) throws BusinessException {
		nodoPianoManager.deleteById(dto.getId());
	}

	@Override

	public void elimina(Long id) throws BusinessException {
		nodoPianoManager.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public PianoDTO leggi(Long id) throws BusinessException {
		return Mapping.mapping(nodoPianoManager.findById(id), PianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PianoDTO> cerca(PianoDTO parametri) throws BusinessException {
		return Mapping.mapping(nodoPianoManager.findAll(Example.of(Mapping.mapping(parametri, NodoPiano.class))),
				PianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PianoDTO> list(Long idEnte) throws BusinessException {
		return Mapping.mapping(nodoPianoManager.findByIdEnteAndTipoNodo(idEnte, TipoNodo.PIANO), PianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte) throws BusinessException {
		return nodoPianoManager.countByIdEnteAndTipoNodoAndDateDeleteIsNull(idEnte, TipoNodo.PIANO);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PianoDTO> getVersioniPrecedenti(Long idEnte, String codicePiano) throws BusinessException {
		return Mapping.mapping(nodoPianoManager.findByIdEnteAndCodiceLessThanAndDateDeleteIsNull(idEnte, codicePiano), PianoDTO.class);
	}

	@Override
	public PianoDTO creaPianoDuplicato(Long idEnte, String codicePianoCorrente) throws BusinessException {
		PianoDTO nuovoPiano = null;
		try {
			PianoDTO vecchioPiano = getPiano(idEnte, codicePianoCorrente);
			nuovoPiano = duplicaPiano(vecchioPiano);
			vecchioPiano.setStato(StatoPiano.ARCHIVIO);
			aggiorna(vecchioPiano);
		} catch (Throwable t) {
			throw new BusinessException("creaPianoDuplicato");
		}
		return nuovoPiano;
	}

	public PianoDTO duplicaPiano(PianoDTO oldPiano) throws BusinessException {
		NodoPiano newPiano = new NodoPiano(oldPiano.getIdEnte(), oldPiano.getAnno() + 1);
		newPiano.setCodice(creaCodicePianoDuplicato(oldPiano.getIdEnte(), oldPiano.getCodice()));
		newPiano.setDescrizione(oldPiano.getDescrizione());
		newPiano.setStatoPiano(StatoPiano.ATTIVO);
		newPiano.setAnno(oldPiano.getAnno());
		if (this.getPiano(oldPiano.getIdEnte(), newPiano.getCodice()) != null)
			throw new BusinessException("Piano già esistente");
		try {
			newPiano = nodoPianoManager.save(newPiano);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
		return Mapping.mapping(newPiano, PianoDTO.class);
	}

	public String creaCodicePianoDuplicato(Long idEnte, String codicePianoCorrente) throws BusinessException {
		int max = 0;
		String suff = null;
		String pref = codicePianoCorrente + SUFFIX_PRS_VARIATO;
		int pos = codicePianoCorrente.indexOf(SUFFIX_PRS_VARIATO);
		if (pos >= 0) {
			pref = codicePianoCorrente.substring(0, pos + SUFFIX_PRS_VARIATO.length());
			suff = codicePianoCorrente.substring(pos + SUFFIX_PRS_VARIATO.length() + 1);
			max = Integer.parseInt(suff);
		}
		max++;
		String progr = new DecimalFormat("0000").format(max);
		return pref + progr;
	}

	@Override
	@Transactional(readOnly = true)
	public PianoDTO getRoot(Long idEnte, int anno) throws BusinessException {
		PianoDTO root = Mapping.mapping(nodoPianoManager.getPiano(idEnte, anno), PianoDTO.class);
		List<NodoPiano> items = nodoPianoManager.findByPianoIdAndDateDeleteIsNullOrderByCodiceCompleto(root.getId());
		riempi(root, items);
		return root;
	}

	private void riempi(final NodoPianoDTO root, final List<NodoPiano> items) {
		final Map<Long, NodoPianoDTO> map = new HashMap<>();
		map.put(root.getId(), root);
		if (items == null) {
			return;
		}
		items.stream().filter(np -> np.getId().equals(root.getId()) || np.getPadre() == null).forEach(np

		-> aggiungiNp(np, map));
	}

	private void aggiungiNp(final NodoPiano np, final Map<Long, NodoPianoDTO> map) {

		NodoPianoDTO p = map.get(np.getPadre().getId());
		if (p == null) {
			p = Mapping.mapping(np.getPadre(), NodoPianoDTO.class);
			map.put(np.getPadre().getId(), p);
		}
		NodoPianoDTO d = map.get(np.getId());
		if (d == null) {
			d = Mapping.mapping(np.getPadre(), NodoPianoDTO.class);
			map.put(np.getId(), d);
			p.add(d);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public PianoDTO getRoot(Long idEnte, int anno, List<Long> filtro) throws BusinessException {
		PianoDTO root = getRoot(idEnte, anno);
		if (filtro == null || filtro.isEmpty())
			return root;
		Map<Long, List<OrganizzazioneDTO>> map = new HashMap<>();
		List<NodoPianoDTO> rimasti = rimasti(root, filtro, map);
		rimuovi(root, rimasti);
		return root;
	}

	private boolean rimuovi(final NodoPianoDTO nodo, final List<NodoPianoDTO> rimasti) {
		final List<NodoPianoDTO> figli = nodo.getFigli();
		if (figli == null || figli.isEmpty())
			return true;
		figli.removeIf(f -> {
			boolean trovato = false;
			for (NodoPianoDTO np : rimasti) {
				if (np.getId().equals(f.getId())) {
					trovato = true;
					break;
				}
			}
			boolean vuoto = rimuovi(f, rimasti);
			return !trovato && vuoto;
		});
		return figli.isEmpty();
	}

	private List<NodoPianoDTO> rimasti(NodoPianoDTO nodo, List<Long> filtro, Map<Long, List<OrganizzazioneDTO>> map)
			throws BusinessException {
		List<NodoPianoDTO> elenco = NodoPianoHelper.contiene(nodo);
		List<NodoPianoDTO> rimasti = new ArrayList<>();
		for (NodoPianoDTO np : elenco) {
			if (contiene(np, filtro, map)) {
				rimasti.add(np);
			}
		}
		return rimasti;
	}

	private boolean contiene(NodoPianoDTO nodo, List<Long> filtro, Map<Long, List<OrganizzazioneDTO>> map) {

		List<OrganizzazioneDTO> organizzazioni = new ArrayList<>();
		if (nodo.getOrganizzazione() != null) {
			organizzazioni.add(nodo.getOrganizzazione());
		}
		if (nodo.getOrganizzazioni() != null) {
			organizzazioni.addAll(nodo.getOrganizzazioni());
		}
		for (OrganizzazioneDTO org : organizzazioni) {
			if (compatibili(org, filtro, map))
				return true;
		}

		return false;
	}

	private boolean compatibili(OrganizzazioneDTO org, List<Long> filtro, Map<Long, List<OrganizzazioneDTO>> map) {
		List<OrganizzazioneDTO> ramo = null;
		ramo = map.get(org.getId());
		if (ramo == null) {
			ramo = Mapping
					.mapping(organizzazioneManager.findByIdEnteAndAnnoAndCodiceCompletoStartsWithOrderByCodiceCompleto(
							org.getIdEnte(), org.getAnno(), org.getCodiceCompleto()), OrganizzazioneDTO.class);
			ramo.add(0, org);
			map.put(org.getId(), ramo);
		}
		for (OrganizzazioneDTO o : ramo) {
			for (Long l : filtro) {
				if (l.equals(o.getId()))
					return true;
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public PrioritaOperativaItem getPrioritaOperativa(Long idEnte, int anno) throws BusinessException {
		PrioritaOperativaItem nodeEnte = null;
		PrioritaOperativaItem node = null;
		PrioritaOperativaItem nodeP = null;
		List<OrganizzazioneDTO> ramo = Mapping.mapping(
				organizzazioneManager.findByIdEnteAndAnnoOrderByCodiceCompleto(idEnte, anno), OrganizzazioneDTO.class);
		PianoDTO piano = Mapping.mapping(getPiano(idEnte, anno),PianoDTO.class);
		Map<Long, PrioritaOperativaItem> map = new HashMap<>();
		for (OrganizzazioneDTO o : ramo) {
			node = map.get(o.getId());
			if (node == null) {
				node = new PrioritaOperativaItem(o);
				map.put(o.getId(), node);
			}
			if (o.getLivello() == Livello.ENTE) {
				nodeEnte = node;
			} else {
				nodeP = map.get(o.getPadre().getId());
				nodeP.add(node);
			}

			addCollegati(o, node, piano);

		}
		return nodeEnte;
	}



	private void addCollegati(OrganizzazioneDTO o, PrioritaOperativaItem node, PianoDTO piano) {
		PrioritaOperativaItem nodeOb = null;
		List<NodoPiano> items = nodoPianoManager.findByOrganizzazioneIdAndPianoIdAndDateDeleteIsNullOrderByCodiceCompleto(o.getId(),
				piano.getId());
//		final List<NodoPiano> items1 = nodoPianoManager.findByOrganizzazioniIdAndPianoIdOrderByCodiceCompleto(o.getId(),
//				piano.getId());
//		if (items == null) {
//			items = items1;
//		} else {
//			if (items1 != null) {
//				items.addAll(items1);
//			}
//		}
		final List<Long> elaborati = new ArrayList<>();
		if (items != null) {
			for (NodoPiano np : items) {
				if (elaborati.contains(np.getId())) {
					continue;
				}
				elaborati.add(np.getId());
				PrioritaOperativaDTO po = Mapping.mapping(
						prioritaOperativaManager.findByOrganizzazioneIdAndNodoPianoId(o.getId(), np.getId()),
						PrioritaOperativaDTO.class);
				if (po == null) {
					po = new PrioritaOperativaDTO();
					po.setNodoPiano(Mapping.mapping(np, NodoPianoDTO.class));
					po.setOrganizzazione(o);
					po.setPriorita(BigDecimal.ZERO);
				}
				nodeOb = new PrioritaOperativaItem(po);
				node.add(nodeOb);
				node.setConObiettivi(true);
				if (po.getPriorita() != null)
					node.setPrioritaTotale(node.getPrioritaTotale().add(po.getPriorita()));
			}
		}
	}

	private void addCollegatiStampe(OrganizzazioneDTO o, PrioritaOperativaReport node, PianoDTO piano) {
		PrioritaOperativaReport nodeOb = null;
		List<NodoPiano> items = nodoPianoManager.findByOrganizzazioneIdAndPianoIdAndDateDeleteIsNullOrderByCodiceCompleto(o.getId(),
				piano.getId());
		final List<NodoPiano> items1 = nodoPianoManager.findByOrganizzazioneIdAndPianoIdAndDateDeleteIsNullOrderByCodiceCompleto(o.getId(),
				piano.getId());
		if (items == null) {
			items = items1;
		} else {
			if (items1 != null) {
				items.addAll(items1);
			}
		}
		final List<Long> elaborati = new ArrayList<>();
		if (items != null) {
			for (NodoPiano np : items) {
				if (elaborati.contains(np.getId())) {
					continue;
				}
				elaborati.add(np.getId());
				PrioritaOperativaDTO po = Mapping.mapping(
						prioritaOperativaManager.findByOrganizzazioneIdAndNodoPianoId(o.getId(), np.getId()),
						PrioritaOperativaDTO.class);
				if (po == null) {
					po = new PrioritaOperativaDTO();
					po.setNodoPiano(Mapping.mapping(np, NodoPianoDTO.class));
					po.setOrganizzazione(o);
					po.setPriorita(BigDecimal.ZERO);
				}
				nodeOb = new PrioritaOperativaReport(po);
				node.add(nodeOb);
				node.setConObiettivi(true);
				if (po.getPriorita() != null)
					node.setPrioritaTotale(node.getPrioritaTotale().add(po.getPriorita()));
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Integer> getAnni(Long idEnte) throws BusinessException {
		return nodoPianoManager.getAnni(idEnte);
	}

	@Override
	@Transactional(readOnly = true)
	public PianoDTO getAlbero(Long idEnte, Integer anno, List<Long> filtro) throws BusinessException {
		return getRoot(idEnte, anno, filtro);
	}

	@Override
	@Transactional(readOnly = true)
	public UtilizzoRisorsaItem getUtilizzoRisorsa(Long idEnte, Integer anno) throws BusinessException {
		UtilizzoRisorsaItem nodeEnte = null;
		UtilizzoRisorsaItem node = null;
		List<RisorsaUmanaNodoPiano> associate = risorsaUmanaNodoPianoManager
				.findByRisorsaUmanaIdEnteAndRisorsaUmanaAnnoAndNodoPianoDateDeleteIsNull(idEnte, anno);
		List<RisorsaUmana> risorse = risorsaUmanaManager.findByIdEnteAndAnno(idEnte, anno);
		Map<String, UtilizzoRisorsaItem> map = new HashMap<>();
		nodeEnte = new UtilizzoRisorsaItem();
		for (RisorsaUmana r : risorse) {
			node = map.get("R/" + r.getId());
			if (node == null) {
				node = new UtilizzoRisorsaItem(Mapping.mapping(r, RisorsaUmanaDTO.class));
				map.put("R/" + r.getId(), node);
				nodeEnte.add(node);
			}

			addCollegati(node.getRisorsaUmana(), node, associate, map);

		}
		return nodeEnte;
	}

	private void addCollegati(RisorsaUmanaDTO o, UtilizzoRisorsaItem node, List<RisorsaUmanaNodoPiano> associate,
			Map<String, UtilizzoRisorsaItem> map) {
		for (RisorsaUmanaNodoPiano runp : associate) {
			if (runp.getRisorsaUmana().getId().equals(o.getId())) {
				NodoPiano nodo = runp.getNodoPiano();
				TipoNodo tipo = runp.getNodoPiano().getTipoNodo();
				String keyR = "R/" + o.getId();
				String keyT = "T/" + o.getId() + "/" + tipo;
				String keyP = "P/" + o.getId() + "/" + tipo + "/" + nodo.getId();
				UtilizzoRisorsaItem nodeR = map.get(keyR);
				UtilizzoRisorsaItem nodeT = map.get(keyT);
				UtilizzoRisorsaItem nodeP = map.get(keyP);
				nodeR.setConObiettivi(true);
				if (nodeT == null) {
					nodeT = new UtilizzoRisorsaItem(tipo);
					nodeR.add(nodeT);
					map.put(keyT, nodeT);
				}
				if (nodeP == null) {
					nodeP = new UtilizzoRisorsaItem(Mapping.mapping(runp, RisorsaUmanaNodoPianoDTO.class));
					nodeT.add(nodeP);
					map.put(keyP, nodeP);
				}
			}
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<Integer> getAnniAll(Long idEnte) throws BusinessException {
		return nodoPianoManager.getAnni(idEnte);
	}

	@Override
	public PianoDTO duplicaPiano(Long idOldPiano) throws BusinessException {
		return duplicaPiano(leggi(idOldPiano));
	}

}
