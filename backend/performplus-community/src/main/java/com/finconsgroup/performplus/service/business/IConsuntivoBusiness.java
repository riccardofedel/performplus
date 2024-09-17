package com.finconsgroup.performplus.service.business;

import java.time.LocalDate;
import java.util.List;

import com.finconsgroup.performplus.enumeration.Periodicita;
import com.finconsgroup.performplus.enumeration.Periodo;
import com.finconsgroup.performplus.enumeration.TipoNodo;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AddAllegatoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaConsuntivoNodoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaConsuntivoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AggiornaForzaturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AllegatoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AllegatoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AvanzamentoFineAnnoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.AvanzamentoFineAnnoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ConsuntivoIndicatoreVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ConsuntivoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ForzaturaNodoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ForzaturaVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.IndicatoreViewVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.RichiestaForzaturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.StatoAvanzamentoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.StatoAvanzamentoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.consuntivazione.ValutazioneVM;

import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.AzioneDTO;
import com.finconsgroup.performplus.service.dto.ConsuntivoDTO;
import com.finconsgroup.performplus.service.dto.ConsuntivoIndicatoreDTO;
import com.finconsgroup.performplus.service.dto.IndicatorePianoDTO;
import com.finconsgroup.performplus.service.dto.LineaStrategicaDTO;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.ObiettivoStrategicoDTO;
import com.finconsgroup.performplus.service.dto.ValutazioneDTO;

import jakarta.validation.Valid;

public interface IConsuntivoBusiness {

//	public RelazioneIndicatoreItem getRelazioneIndicatori(Long idEnte, int anno, Periodo periodo,
//			List<Long> selezionati) throws BusinessException;

	public void aggiorna(ValutazioneDTO valutazione) throws BusinessException;

	public void aggiornaNote(IndicatorePianoDTO indicatorePiano) throws BusinessException;

	public List<ConsuntivoDTO> getRelazioneIndicatoriStampa(Long idEnte, int anno) throws BusinessException;

	
	public List<ConsuntivoIndicatoreDTO> getConsuntivoIndicatori(Long idNodo, Long idEnte, Integer anno,
			Periodo periodo)throws BusinessException;

	public Double raggiungimentoFigli(Long idNodo, Integer anno, Periodo periodo)throws BusinessException;

	List<NodoPianoDTO> contiene(Long idNodo)throws BusinessException;

	void aggiorna(LineaStrategicaDTO obiettivoGestionale) throws BusinessException;

	void aggiorna(AzioneDTO obiettivoOperativo) throws BusinessException;

	void aggiorna(ObiettivoStrategicoDTO obiettivoStrategico) throws BusinessException;

	void aggiornaForzatura(IndicatorePianoDTO indicatorePiano) throws BusinessException;

	void aggiornaNonValutabile(IndicatorePianoDTO indicatorePiano) throws BusinessException;

	public ConsuntivoVM read(Long idNodoPiano) throws BusinessException;

	public List<ConsuntivoIndicatoreVM> detailConsuntivoIndicatori(Long idNodoPiano) throws BusinessException;

	public IndicatoreViewVM view(Long idIndicatore) throws BusinessException;

	public void consuntiva(Long idIndicatore, AggiornaConsuntivoRequest request) throws BusinessException;

	public ForzaturaVM getForzatura(Long idIndicatore) throws BusinessException;

	public void aggiornaForzatura(Long idIndicatore, AggiornaForzaturaRequest request) throws BusinessException;

	public List<AllegatoListVM> getAllegati(Long idIndicatore) throws BusinessException;

	public AllegatoVM getAllegato(Long idAllegato) throws BusinessException;

	public void addAllegato(Long idIndicatore,AddAllegatoRequest request) throws BusinessException;


	public void aggiorna(Long idNodoPiano, AggiornaConsuntivoNodoRequest request) throws BusinessException;

	public void richiestaForzatura( Long idIndicatore,  RichiestaForzaturaRequest request)throws BusinessException;

	public ValutazioneVM getValutazione( Long idIndicatore,  Long idValutazione,
			LocalDate dataRilevazione)throws BusinessException;

	public void deleteAllegato( Long idAllegato)throws BusinessException;


	public void statoAvanzamento( StatoAvanzamentoRequest request)throws BusinessException;

	public StatoAvanzamentoVM getStatoAvanzamento(Long idNodoPiano)throws BusinessException;


	public Periodicita[] getPeridicitaFase(TipoNodo tipoNodo) throws BusinessException;
	
	public boolean enabledConsuntivo(Long idNodo)throws BusinessException;

	void aggiornaPercentualiForzatura(Long idNodoPiano, Double percentualeForzatura, Double percentualeForzaturaResp)
			throws BusinessException;

	public ForzaturaNodoVM getForzatureNodo(@Valid Long idNodoPiano)throws BusinessException;

	public void forzatureNodo(@Valid ForzaturaNodoVM request)throws BusinessException;


}
