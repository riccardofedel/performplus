package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.PianoVM;
import com.finconsgroup.performplus.service.business.IBusinessEnte;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.PianoDTO;
import com.finconsgroup.performplus.service.dto.PrioritaOperativaItem;
import com.finconsgroup.performplus.service.dto.PrioritaOperativaReport;
import com.finconsgroup.performplus.service.dto.UtilizzoRisorsaItem;

public interface IPianoBusiness extends IBusinessEnte<PianoDTO> {

    public PianoDTO getPiano(Long idEnte,String codicePiano) throws BusinessException;

    public List<PianoDTO> getVersioniPrecedenti(Long idEnte,String codicePiano)
	    throws BusinessException;

    public String creaCodicePianoDuplicato(Long idEnte,String codicePianoCorrente)
	    throws BusinessException;

    public PianoDTO duplicaPiano(Long idOldPiano)
	    throws BusinessException;

    public PianoDTO getRoot(Long idEnte,int anno) throws BusinessException;
    public PianoDTO getRoot(Long idEnte,int anno,List<Long> filtro) throws BusinessException;

    public PianoVM getPiano(Long idEnte,int anno) throws BusinessException;

    public PrioritaOperativaItem getPrioritaOperativa(Long idEnte,int anno)throws BusinessException;
    
 
    public PianoDTO getAlbero(Long idEnte, Integer anno,
	    List<Long> selezionati)throws BusinessException;

	public UtilizzoRisorsaItem getUtilizzoRisorsa(Long idEnte, Integer anno)throws BusinessException;

	PianoDTO creaPianoDuplicato(Long idEnte, String codicePianoCorrente) throws BusinessException;

	public List<Integer> getAnni(Long idEnte) throws BusinessException;

	public List<Integer> getAnniAll(Long idEnte) throws BusinessException;


}
