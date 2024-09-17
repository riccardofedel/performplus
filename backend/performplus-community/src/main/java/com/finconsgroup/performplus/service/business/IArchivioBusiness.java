package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.rest.api.vm.PianoVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.CambiaStato;

public interface IArchivioBusiness {
    public PianoVM archiviazione(Long idPiano)
	    throws BusinessException;

}
