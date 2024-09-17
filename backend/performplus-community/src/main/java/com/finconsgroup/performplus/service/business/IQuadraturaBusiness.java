package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.PrioritaRisorseDTO;
import com.finconsgroup.performplus.service.dto.QuadraturaIndicatoriProg;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorseProg;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaNodoPianoDTO;
import com.finconsgroup.performplus.service.dto.RisorsaUmanaPianoReport;

public interface IQuadraturaBusiness {

    public List<QuadraturaIndicatoriProg> quadraturaProgrammazione(
	    Long idNodo, List<Long> filtroOrganizzazioni) throws BusinessException;


    List<QuadraturaIndicatoriProg> quadraturaProgrammazioneStampa(
    		Long idNodo, int oreSettimanaliMax)
	    throws BusinessException;

    List<RisorsaUmanaPianoReport> quadraturaRisorseStampaPerPiano(Long idPiano,
	    List<Long> filtroOrganizzazioni,int oreSettimanaliMax) throws BusinessException;

	public List<PrioritaRisorseDTO> getPrioritaRisorseNodi(
			Long idOrganizzazione)throws BusinessException;

	public List<RisorsaUmanaNodoPianoDTO> getPrioritaRisorseSettore(
			Long idSettore)throws BusinessException;

	List<QuadraturaRisorseProg> quadraturaRisorseStampaPerNodo(Long idNodo, List<Long> filtroOrganizzazioni,
			int oreSettimanaliMax) throws BusinessException;

}
