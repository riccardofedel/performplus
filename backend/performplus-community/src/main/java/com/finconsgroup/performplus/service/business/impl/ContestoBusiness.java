package com.finconsgroup.performplus.service.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.ContenutoContesto;
import com.finconsgroup.performplus.domain.NodoPiano;
import com.finconsgroup.performplus.enumeration.StatoPiano;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.repository.ContenutoContestoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.rest.api.vm.KeyValues;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaIntroduzioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaIntroduzioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.IntroduzioneContenutoResponse;
import com.finconsgroup.performplus.rest.api.vm.v2.IntroduzioneVM;
import com.finconsgroup.performplus.service.business.IContestoBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.business.utils.MappingContestoHelper;
import com.finconsgroup.performplus.service.dto.ContestoDTO;

@Service
@Transactional
public class ContestoBusiness implements IContestoBusiness {

	@Autowired
	private ContenutoContestoRepository contestoRepository;

	@Autowired
	private NodoPianoRepository nodoPianoRepository;


	@Value("#{${programmazione.introduzione}}")
	private Map<String,List<String>> attributiIntroduzione;
	
	@Override
	@Transactional(readOnly = true)
	public ContestoDTO getContesto(Long idEnte, int anno, boolean isRelazione) throws BusinessException {
		NodoPiano piano = nodoPianoRepository.getPiano(idEnte, anno);
		if (piano == null)
			return null;
		return MappingContestoHelper.mappingContenuto(contestoRepository.findByPianoId(piano.getId()),piano);
	}

//	@Override
//	public void clona(Long idPiano, Long idPianoClone, boolean isRelazione) throws BusinessException {
//		List<ContenutoContesto> contesto = contestoRepository.findByPianoIdAndRelazione(idPiano, isRelazione);
//		if (contesto != null)
//			clonaContesto(contesto, idPianoClone);
//	}
//
//	private Contesto clonaContesto(Contesto contesto, Long idPianoClone) throws BusinessException {
//		Contesto clone = Mapping.mapping(contesto, Contesto.class);
//		clone.setId(null);
//		clone.setPiano(nodoPianoRepository.findById(idPianoClone).orElse(null));
//		clone = contestoRepository.save(clone);
//		return clone;
//	}

	@Override
	@Transactional(readOnly = true)
	public IntroduzioneVM readIntroduzione(Long id) throws BusinessException {
		List<ContenutoContesto> items = contestoRepository.findByPianoId(id);
		return MappingContestoHelper.mappingIntroduzione(items, id,attributiIntroduzione).idPiano(id);
	}


	@Override
	public IntroduzioneVM creaIntroduzione(CreaIntroduzioneRequest request) throws BusinessException {
		NodoPiano piano = nodoPianoRepository.findTopByIdEnteAndAnnoAndTipoNodoAndStatoPianoAndDateDeleteIsNull(request.getIdEnte(),
				request.getAnno(), TipoNodo.PIANO, StatoPiano.ATTIVO);
		if (piano == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND,
					"Non trovato PRS valido per idEnte:" + request.getIdEnte() + " e anno:" + request.getAnno());
		}
		List<ContenutoContesto> items = contestoRepository.findByPiano(piano);
		items = MappingContestoHelper.mappingChanged(request, items, piano);
		List<ContenutoContesto> out = new ArrayList<>();
		for (ContenutoContesto c : items) {
			out.add(contestoRepository.save(c));
		}
		return MappingContestoHelper.mappingIntroduzione(out, piano.getId(),attributiIntroduzione);
	}

	@Override
	public void aggiornaIntroduzione(Long idPiano, AggiornaIntroduzioneRequest request) throws BusinessException {
		NodoPiano piano = nodoPianoRepository.findById(idPiano).orElse(null);
		if (piano == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND, "Non trovato PRS valido per id:" + idPiano);
		}
		List<ContenutoContesto> items = contestoRepository.findByPiano(piano);
		items = MappingContestoHelper.mappingChanged(request, items, piano);
		for (ContenutoContesto c : items) {
			contestoRepository.save(c);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public IntroduzioneVM leggiIntroduzione(Long idEnte, Integer anno) throws BusinessException {
		NodoPiano piano = nodoPianoRepository.findTopByIdEnteAndAnnoAndTipoNodoAndStatoPianoAndDateDeleteIsNull(idEnte, anno, TipoNodo.PIANO,
				StatoPiano.ATTIVO);
		if (piano == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND,
					"Non trovato DUP valido per idEnte:" + idEnte + " e anno:" + anno);
		}
		List<ContenutoContesto> items = contestoRepository.findByPianoIdEnteAndPianoAnno(idEnte, anno);
		return MappingContestoHelper.mappingIntroduzione(items, piano.getId(),attributiIntroduzione);

	}

	@Override
	@Transactional(readOnly = true)
	public ContestoDTO leggiPerPiano(Long idPiano, boolean isRelazione) throws BusinessException {
		NodoPiano piano = nodoPianoRepository.findById(idPiano).orElse(null);
		if (piano == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND, "Non trovato DUP valido per id:" + idPiano);
		}
		List<ContenutoContesto> items = contestoRepository.findByPiano(piano);
		return MappingContestoHelper.mappingContenuto(items, piano);
	}

	@Override
	public void clona(Long idPiano, Long idPianoClone, boolean isRelazione) throws BusinessException {
		List<ContenutoContesto> items = contestoRepository.findByPianoId(idPiano);
		NodoPiano pc = nodoPianoRepository.findById(idPianoClone).orElse(null);
		if (pc == null)
			return;
		if (items == null || items.isEmpty()) {
			return;
		}
		for (ContenutoContesto c : items) {
			ContenutoContesto clone = contestoRepository.findByPianoIdAndNome(pc.getId(), c.getNome());
			if (clone == null) {
				clone = Mapping.mapping(c, ContenutoContesto.class);
				clone.setId(null);
				clone.setPiano(pc);
				clone=contestoRepository.save(clone);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<KeyValues> getIntroduzioneElementi() {
		List<KeyValues> out=new ArrayList<>();
		for (String  key : attributiIntroduzione.keySet()) {
			out.add(new KeyValues(key,attributiIntroduzione.get(key)));
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public IntroduzioneContenutoResponse leggiIntroduzioneContenuto(Long idEnte, Integer anno, String gruppo,
			String nome) throws BusinessException {
		NodoPiano piano = nodoPianoRepository.findTopByIdEnteAndAnnoAndTipoNodoAndStatoPianoAndDateDeleteIsNull(idEnte, anno, TipoNodo.PIANO,
				StatoPiano.ATTIVO);
		if (piano == null) {
			throw new BusinessException(HttpStatus.NOT_FOUND,
					"Non trovato DUP valido per idEnte:" + idEnte + " e anno:" + anno);
		}
		ContenutoContesto c = contestoRepository.findByPianoAndGruppoAndNome(piano,gruppo,nome);
		if(c==null) {
			c=new ContenutoContesto(nome, gruppo, "");
		}
		return MappingContestoHelper.mappingContenuto(c, piano);
	}



	
}
