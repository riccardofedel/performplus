package com.finconsgroup.performplus.service.business;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.RisorsaUmanaSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.AggiornaRisorsaUmanaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.CreaRisorsaUmanaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AggiornaAmministratoreRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AmministratoreDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.AmministratoreListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.amministratore.CreaAmministratoreRequest;
import com.finconsgroup.performplus.service.business.IBusinessEnteAnno;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaOrganizzazioneDTO;

public interface IRisorsaUmanaBusiness extends IBusinessEnteAnno<RisorsaUmanaDTO> {

	public RisorsaUmanaDTO getRisorsaUmana(Long idEnte, Integer anno, String codice)
			throws BusinessException;

	public List<RisorsaUmanaDTO> getRisorseUmane(Long idEnte, Integer anno)
			throws BusinessException;

	public List<RisorsaUmanaDTO> getRisorsePolitiche(Long idEnte, Integer anno)
			throws BusinessException;


	public List<RisorsaUmanaDTO> cercoPerOrganizzazione(Long idOrganizzazione,
			Boolean esterne) throws BusinessException;

	public List<RisorsaUmanaDTO> getRisorseUmaneDiAlberoUo(
			Long idOrganizzazione) throws BusinessException;

	public List<RisorsaUmanaDTO> getRisorseUmane(Long idEnte, Integer anno, boolean esterno)
			throws BusinessException;

	public List<RisorsaUmanaDTO> cercaNonAssociate(
			Long idOrganizzazione, Boolean esterne)
			throws BusinessException;

	public List<RisorsaUmanaDTO> cercaNonAssociate(
			Long idOrganizzazione, Boolean esterne, String testo)
			throws BusinessException;

	public List<RisorsaUmanaDTO> getRisorseUmane(Long idEnte, Integer anno, Boolean esterne,
			String testo) throws BusinessException;

	public List<RisorsaUmanaDTO> getRisorsePolitiche(Long idEnte, Integer anno, String testo)
			throws BusinessException;

	public RisorsaUmanaDTO getSindaco(Long idEnte, Integer anno) throws BusinessException;

	public List<RisorsaUmanaOrganizzazioneDTO> getRisorseUmaneOrganizzazione(
			Long idEnte,Integer anno) throws BusinessException;

	public List<RisorsaUmanaDTO> cercaPerCognome(Long idEnte, Integer anno, String cognome)
			throws BusinessException;

	public List<RisorsaUmanaOrganizzazioneDTO> getRisorseUmaneRamo(
			Long idOrganizzazione) throws BusinessException;

	public RisorsaUmanaDTO cercaPerCodiceInterno(Long idEnte, Integer anno, String codiceinterno) throws BusinessException;

	public List<RisorsaUmanaSmartVM> search( Long idEnte, Integer year, boolean politico,  String testo) throws BusinessException;

	public RisorsaUmanaDetailVM creaRisorsaUmana(CreaRisorsaUmanaRequest request)throws BusinessException;

	public void aggiornaRisorsaUmana(Long id, AggiornaRisorsaUmanaRequest request)throws BusinessException;

	public RisorsaUmanaDetailVM leggiRisorsaUmana(Long id);

	public Page<RisorsaUmanaListVM> search(Long idEnte, Integer anno, Boolean interno, String cognome,
			Boolean soloAttive,
			Pageable pageable)throws BusinessException;

	public AmministratoreDetailVM leggiAmministratore( Long id)throws BusinessException;

	public Page<AmministratoreListVM> searchAmministratore(Long idEnte, Integer anno, String cognome,
			Pageable pageable)throws BusinessException;
	public List<AmministratoreListVM> searchAmministratore(Long idEnte, Integer anno, String cognome)throws BusinessException;

	public AmministratoreDetailVM creaAmministratore(CreaAmministratoreRequest request)throws BusinessException;

	public void aggiornaAmministratore(@Valid Long id, AggiornaAmministratoreRequest request)throws BusinessException;

	public List<OrganizzazioneSmartVM> getOrganizzazioniResponsabile(Long id)throws BusinessException;

	public List<OrganizzazioneSmartVM> getOrganizzazioni(Long id)throws BusinessException;

	public List<DecodificaVM> searchResponsabili( Long idEnte,  Integer anno,  String cognome)throws BusinessException;

	public List<DecodificaVM> searchRisorse( Long idEnte,  Integer anno,  String cognome)throws BusinessException;

	public RisorsaUmanaSmartVM leggiSmart(Long idRisorsa)throws BusinessException;

	public List<DecodificaVM> incarichi( Long idEnte)throws BusinessException;
	public List<DecodificaVM> categorie( Long idEnte)throws BusinessException;
	public List<DecodificaVM> contratti( Long idEnte)throws BusinessException;
	public List<DecodificaVM> profili( Long idEnte)throws BusinessException;

}
