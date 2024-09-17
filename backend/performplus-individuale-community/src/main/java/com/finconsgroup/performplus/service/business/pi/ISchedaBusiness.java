package com.finconsgroup.performplus.service.business.pi;

import java.util.List;

import com.finconsgroup.performplus.rest.api.pi.vm.NoteValutazioneVM;
import com.finconsgroup.performplus.rest.api.pi.vm.SchedaVM;
import com.finconsgroup.performplus.rest.api.pi.vm.SchedaValutatoVM;
import com.finconsgroup.performplus.rest.api.pi.vm.TotaliVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneObiettiviVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneObiettivoVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.ValutazioneQuestionarioVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface ISchedaBusiness {


	public List<SchedaVM> elenco(Long idValutatore, Long idValutato) throws BusinessException;

	public SchedaVM leggi(Long idRegistrazione) throws BusinessException;

	public void aggiornaQuestionario(ValutazioneQuestionarioRequest valutazione)throws BusinessException;

	public void forzaValutazione(ValutazioneObiettiviVM valutazione)throws BusinessException;

	public ValutazioneQuestionarioVM getQuestionario(Long idRegistrazione)throws BusinessException;

	public ValutazioneQuestionarioVM risultatoQuestionario(Long idRegistrazione)throws BusinessException;

	public List<ValutazioneObiettivoVM> getObiettivi(Long idRegistrazione)throws BusinessException;

	public TotaliVM totali( Long idRegistrazione)throws BusinessException;

	public SchedaValutatoVM schedaValutatoByRegistrazione(Long idRegistrazione)throws BusinessException;

	public SchedaValutatoVM schedaValutatoByValutato(Long idValutato)throws BusinessException;

	public void pubblicazioneScheda(Long idRegistrazione, String note)throws BusinessException;
	
	public void accettazioneScheda(Long idRegistrazione, Boolean flagAccettazione, String note)throws BusinessException;
	
	public void pubblicazioneValutazione(Long idRegistrazione, String note)throws BusinessException;
	
	public void accettazioneValutazione(Long idRegistrazione, Boolean flagAccettazione, String note)throws BusinessException;

	public NoteValutazioneVM getNote(Long idRegistrazione)throws BusinessException;

	public void approvazioneScheda(Long idRegistrazione, String note) throws BusinessException;

	public void approvazioneValutazione(Long idRegistrazione, String note) throws BusinessException;

	public List<SchedaVM> elencoAll(Long idValutato,Long idValutatore)throws BusinessException;

	public void performanceOrganizzativa(Long idRegistrazione,Long idScheda, Boolean attiva)throws BusinessException;

}
