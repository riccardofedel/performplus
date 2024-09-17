package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.service.business.IBusinessEnteAnno;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.RisorsaStrumentaleDTO;
import com.finconsgroup.performplus.service.dto.RisorsaStrumentaleOrganizzazioneDTO;

public interface IRisorsaStrumentaleBusiness extends
	IBusinessEnteAnno<RisorsaStrumentaleDTO> {

    public List<RisorsaStrumentaleDTO> cercoPerOrganizzazione(
	    Long idOrganizzazione) throws BusinessException;

    public double getTotalePerOrganizzazione(Long idOrganizzazione)
	    throws BusinessException;

    public List<RisorsaStrumentaleDTO> getRisorseStrumentaliDiAlberoUo(
    		Long idOrganizzazione) throws BusinessException;

    public void elimina(Long idOrganizzazione, String codice
	    ) throws BusinessException;

    public RisorsaStrumentaleDTO leggiPerCodice(Long idOrganizzazione,
	    String codice) throws BusinessException;

    public String maxCodice(Long idEnte,Integer anno)throws BusinessException;

    public List<RisorsaStrumentaleDTO> cercaNonAssociate(
    		Long idOrganizzazione)throws BusinessException;

    public List<RisorsaStrumentaleDTO> cercaNonAssociate(
    		Long idOrganizzazione, String testo)throws BusinessException;
    
    public List<RisorsaStrumentaleOrganizzazioneDTO> getRisorsaStrumentaleOrganizzazione(
	    Long idOrganizzazione) throws BusinessException;

    public List<RisorsaStrumentaleDTO> cerca(Long idEnte,Integer anno, String testo)throws BusinessException;

}
