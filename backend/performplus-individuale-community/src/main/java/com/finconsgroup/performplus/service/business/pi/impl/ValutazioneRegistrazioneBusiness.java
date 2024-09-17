package com.finconsgroup.performplus.service.business.pi.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.Registrazione;
import com.finconsgroup.performplus.domain.ValoreAmbito;
import com.finconsgroup.performplus.domain.ValutazioneRegistrazione;
import com.finconsgroup.performplus.repository.RegistrazioneRepository;
import com.finconsgroup.performplus.repository.ValoreAmbitoRepository;
import com.finconsgroup.performplus.repository.ValutazioneRegistrazioneRepository;
import com.finconsgroup.performplus.rest.api.pi.vm.StatoValutazioneVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.pi.IValutazioneRegistrazioneBusiness;

@Service
@Transactional
public class ValutazioneRegistrazioneBusiness implements
		IValutazioneRegistrazioneBusiness {
	@Autowired
	private ValutazioneRegistrazioneRepository valutazioneRegistrazioneRepository;

	@Autowired
	private RegistrazioneRepository registrazioneRepository;
	
	@Autowired
	private ValoreAmbitoRepository valoreAmbitoRepository;

	@Override
	public void eliminaValutazioniRegistrazione(long idRegistrazione) throws BusinessException, Exception {
		Registrazione r=registrazioneRepository.findById(idRegistrazione).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"Valutazione non trovata:"+idRegistrazione));
		valutazioneRegistrazioneRepository.deleteByRegistrazioneId(idRegistrazione);
	}

	@Override
	public void salvaNuovaValutazione(List<Long> idsValoreAmbito, Long idRegistrazione) throws Exception {
		Registrazione r=registrazioneRepository.findById(idRegistrazione).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"Valutazione non trovata:"+idRegistrazione));
		List<ValutazioneRegistrazione> items=valutazioneRegistrazioneRepository.findByRegistrazioneOrderByValoreAmbitoAmbitoValutazioneCodiceCompletoAsc(r);
		if(idsValoreAmbito==null||idsValoreAmbito.isEmpty() && items!=null && !items.isEmpty()) {
			valutazioneRegistrazioneRepository.deleteAll(items);
			return;
		}
		if(idsValoreAmbito==null||idsValoreAmbito.isEmpty()) {
			return;
		}
		final List<ValoreAmbito> valori=new ArrayList<>();
		final List<ValutazioneRegistrazione> elimina=new ArrayList<>();
		for (Long id : idsValoreAmbito) {
			ValoreAmbito v=valoreAmbitoRepository.findById(id).orElseThrow(()->new BusinessException(HttpStatus.BAD_REQUEST,"Valore ambito non trovato"));
			valori.add(v);
		}
		for (ValutazioneRegistrazione vr : items) {
			boolean trovato=false;
			for (ValoreAmbito v : valori) {
				if(vr.getValoreAmbito().getId().equals(v.getId())) {
					trovato=true;
					break;
				}
			}
			if(!trovato) {
				elimina.add(vr);
			}else {
				valori.removeIf(t->t.getId().equals(vr.getValoreAmbito().getId()));
			}
		}
		if(!elimina.isEmpty()) {
			valutazioneRegistrazioneRepository.deleteAll(elimina);
		}

		if(!valori.isEmpty()) {
			for (ValoreAmbito v : valori) {
				valutazioneRegistrazioneRepository.save(new ValutazioneRegistrazione(v,r));
			}
		}
			
	}

	@Override
	public StatoValutazioneVM statoValutazione(Long idRegistrazione) throws Exception {
		Registrazione r=registrazioneRepository.findById(idRegistrazione).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"Valutazione non trovata:"+idRegistrazione));
		List<ValutazioneRegistrazione> items=valutazioneRegistrazioneRepository.findByRegistrazioneOrderByValoreAmbitoAmbitoValutazioneCodiceCompletoAsc(r);
		return null;
	}

	
}