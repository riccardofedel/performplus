package com.finconsgroup.performplus.service.business;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.DipendenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ModificaDisponibilitaRisorsaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.AggiornaStrutturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.CreaStrutturaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.OrganizzazioneDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.AssociaRisorsaOrganizzazioneRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.struttura.risorsa.RisorsaUmanaOrganizzazioneListVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.OrganizzazioneDTO;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorsaStrumentale;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorsaUmana;

public interface IOrganizzazioneBusiness {
    public boolean isCodiceDuplicato(Long idOrganizzazione, String codice)
	    throws BusinessException;

    public void rimuoviRisorsaUmana(Long idRisorsaUmanaOrganizzazione
	    ) throws BusinessException;

    public void associaRisorse(Long idOrganizzazione,
	    List<Long> selezionate)
	    throws BusinessException;

    public List<QuadraturaRisorsaUmana> quadraturaRisorseUmane(
    		Long idOrganizzazione) throws BusinessException;

    public List<QuadraturaRisorsaStrumentale> quadraturaRisorseStrumentali(
    		Long idOrganizzazione) throws BusinessException;

 
    public String maxCodice(Long idPadre) throws BusinessException;
    public void rimuoviRisorsaStrumentale(
	    Long idRisorsaStrumentaleOrganizzazione
	    ) throws BusinessException;
    public void associaRisorseStrumentali(Long idOrganizzazione,
	    List<Long> selezionate
	    ) throws BusinessException;
    public List<QuadraturaRisorsaUmana> quadraturaRisorseUmane(
    		Long idOrganizzazione, String testo) throws BusinessException;
    public List<QuadraturaRisorsaStrumentale> quadraturaRisorseStrumentali(
    		Long idOrganizzazione, String testo) throws BusinessException;

    public List<QuadraturaRisorsaUmana> quadraturaRisorseUmaneStampe(Long idEnte,Integer anno)
	    throws BusinessException;
    List<QuadraturaRisorsaUmana> quadraturaRisorseUmane(
    		Long idOrganizzazione, Integer anno, 
    		LocalDate inizioValidita, LocalDate fineValidita,
    		String testo)
	    throws BusinessException;

	List<QuadraturaRisorsaStrumentale> quadraturaRisorseStrumentaliStampe(Long idEnte, Integer anno)
			throws BusinessException;
	
	public List<OrganizzazioneSmartVM> search( Long idEnte, Integer anno,  String testo)throws BusinessException;
	public List<OrganizzazioneSmartVM> direzioni( Long idEnte, Integer anno,  String testo)throws BusinessException;

	public List<OrganizzazioneSmartVM> children(Long idOrganizzazione)
	    	    throws BusinessException;

	OrganizzazioneSmartVM getRootOnly(Long idEnte, Integer anno) throws BusinessException;

	List<OrganizzazioneSmartVM> ramo(Long idOrganizzazione) throws BusinessException;

	public OrganizzazioneDetailVM read(Long id)throws BusinessException;

	public CreaStrutturaRequest prepareDescendant(Long idPadre)throws BusinessException;

	public void modificaDisponibilitaRisorsa(ModificaDisponibilitaRisorsaRequest request)throws BusinessException;

	public void associaRisorsa(AssociaRisorsaOrganizzazioneRequest request)throws BusinessException;

	public void crea(CreaStrutturaRequest request)throws BusinessException;

	public void aggiorna(AggiornaStrutturaRequest request)throws BusinessException;

	public List<RisorsaUmanaOrganizzazioneListVM> getRisorseAssociate( Long idOrganizzazione)throws BusinessException;

	public List<RisorsaUmanaListVM> getRisorseAssociabili( Long idOrganizzazione, String testo)throws BusinessException;

	public List<DipendenteVM> quadraturaRisorseUmaneRamo(Long idOrganizzazione)throws BusinessException;

	public OrganizzazioneSmartVM readSmart(Long idOrganizzazione)throws BusinessException;

	void elimina(Long id) throws BusinessException;

	List<OrganizzazioneDTO> ramoStampa(Long idOrganizzazione) throws BusinessException;

	public List<OrganizzazioneSmartVM> LivelloSuperiore( Long idEnte, Integer anno)throws BusinessException;

	public List<DecodificaVM> getResponsabiliAzione(Long idOrganizzazione, String testo)throws BusinessException;

	public List<RisorsaUmanaOrganizzazioneListVM> getRisorseAssociateAttuali( Long idOrganizzazione)throws BusinessException;

	
	public Page<DipendenteVM> getDipendenti(Long idStruttura,String cognome,
			Pageable pageable)throws BusinessException;

	public List<Long> ramoId(Long id)throws BusinessException;

	public Page<DipendenteVM> getDipendenti(@Valid Long id, String cognome, List<Long> filtroStrutture,
			Pageable pageable)throws BusinessException;

	List<String> codiciCompleti(List<Long> ids) throws BusinessException;

	public List<Long> ramoCompletoId(Long id)throws BusinessException;

}
