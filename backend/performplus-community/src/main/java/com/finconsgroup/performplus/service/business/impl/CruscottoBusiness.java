package com.finconsgroup.performplus.service.business.impl;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.Cruscotto;
import com.finconsgroup.performplus.repository.CruscottoRepository;
import com.finconsgroup.performplus.repository.NodoPianoRepository;
import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.CruscottoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.cruscotto.StatisticheCruscottoVM;
import com.finconsgroup.performplus.service.business.ICruscottoBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.Mapping;

@Service
@Transactional
public class CruscottoBusiness implements ICruscottoBusiness {

	@Autowired
	private CruscottoRepository cruscottoRepository;
	@Autowired
	private NodoPianoRepository nodoPianoRepository;

	private static final Logger logger = LoggerFactory.getLogger(CruscottoBusiness.class);

	@Override
	@Transactional(readOnly = true)
	public CruscottoVM getCruscotto(Long idEnte, Integer anno) throws BusinessException {
		return mapping(cruscottoRepository.findByIdEnteAndAnno(idEnte, anno).orElse(new Cruscotto())).idEnte(idEnte)
				.anno(anno);
	}

	@Override
	public CruscottoVM crea(CruscottoVM dto) throws BusinessException {
		try {
			Cruscotto c = cruscottoRepository.findByIdEnteAndAnno(dto.getIdEnte(), dto.getAnno()).orElse(null);
			if (c == null) {
				c = new Cruscotto();
			}
			mapping(dto, c);
			c = cruscottoRepository.save(c);
			return Mapping.mapping(c, CruscottoVM.class);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	@Override
	public CruscottoVM aggiorna(CruscottoVM dto) throws BusinessException {
		try {
			Cruscotto c = cruscottoRepository.findByIdEnteAndAnno(dto.getIdEnte(), dto.getAnno()).orElse(null);
			mapping(dto, c);
			c = cruscottoRepository.save(c);
			return Mapping.mapping(c, CruscottoVM.class);
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	private void mapping(CruscottoVM from, Cruscotto to) {
		Mapping.mapping(from, to);

	}

	private CruscottoVM mapping(Cruscotto from) {
		return Mapping.mapping(from, CruscottoVM.class);
	}

	@Override
	public void elimina(Long id) throws BusinessException {
		cruscottoRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public CruscottoVM leggi(Long id) throws BusinessException {
		return Mapping.mapping(cruscottoRepository.findById(id), CruscottoVM.class);
	}

	@Override
	@Transactional(readOnly = true)
	public StatisticheCruscottoVM statistiche(Long idEnte, Integer anno) throws BusinessException {
		final StatisticheCruscottoVM out = new StatisticheCruscottoVM();
		Cruscotto cruscotto = cruscottoRepository.findByIdEnteAndAnno(idEnte, anno).orElse(new Cruscotto());

		int nodi = nodoPianoRepository.countByIdEnteAndAnnoAndDateDeleteIsNull(cruscotto.getIdEnte(),
				cruscotto.getAnno()) - 1;
		// count obiettivi sennza target
		int noTarget = nodoPianoRepository.countWithoutTarget(cruscotto.getIdEnte(), cruscotto.getAnno());
		float resto = nodi - noTarget;
		if (nodi > 0) {
			out.setStatoAvanzamentoPiano(resto * 100f / (float) nodi);
		} else {
			out.setStatoAvanzamentoPiano(0f);
		}
//		if (!Boolean.TRUE.equals(cruscotto.getFlagConsuntivazione01())
//				&& !Boolean.TRUE.equals(cruscotto.getFlagConsuntivazione02())) {
//			return out;
//		}
		Integer periodo = null;
		LocalDate now=LocalDate.now();
		if (cruscotto.getDataConsuntivazioneS1Da() != null && !cruscotto.getDataConsuntivazioneS1Da().isAfter(now)
				&& Boolean.TRUE.equals(cruscotto.getFlagConsuntivazioneS1())) {
			periodo = 2;
		}
		if (cruscotto.getDataConsuntivazioneS2Da() != null && !cruscotto.getDataConsuntivazioneS2Da().isAfter(now)
				&& Boolean.TRUE.equals(cruscotto.getFlagConsuntivazioneS2())) {
			periodo = 4;
		}

		if (periodo == null) {
			return out;
		}
		int noConsuntivi = 0;
		// count obiettivi senza consuntivo periodo attivo
		noConsuntivi = nodoPianoRepository.countWithoutConsuntivazione(cruscotto.getIdEnte(), cruscotto.getAnno(),
				periodo);
		resto = nodi - noConsuntivi;
		if (nodi > 0) {
			out.setStatoAvanzamentoConsuntivazione(resto * 100f / (float) nodi);
		} else {
			out.setStatoAvanzamentoConsuntivazione(0f);
		}

		return out;
	}

}
