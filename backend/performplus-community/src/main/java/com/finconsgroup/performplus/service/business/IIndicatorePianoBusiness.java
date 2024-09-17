package com.finconsgroup.performplus.service.business;

import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.ForzatureAdminRequest;
import com.finconsgroup.performplus.rest.api.vm.ForzatureRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.AggiornaIndicatorePianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.CreaIndicatorePianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatorePianoListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatoreRangeVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.IndicatoreTargetVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.SaveIndicatoreRangeRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.indicatore.SaveIndicatoreTargetRequest;
import com.finconsgroup.performplus.service.business.IBusinessEnteAnno;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.IndicatorePianoDTO;

public interface IIndicatorePianoBusiness extends IBusinessEnteAnno<IndicatorePianoDTO> {
    public List<IndicatorePianoDTO> cercaPerNodo(Long idNodo)
	    throws BusinessException;

    public Double peso(Long idNodo) throws BusinessException;
    
	public IndicatorePianoDTO modificaForzatureAdmin(ForzatureAdminRequest request);
	
	public IndicatorePianoDTO modificaForzature(ForzatureRequest request);
	void aggiornaPeso(IndicatorePianoDTO dto) throws BusinessException;

	public IndicatorePianoDetailVM read( Long idIndicatorePiano)throws BusinessException;

	public Long create( Long idNodoPiano,CreaIndicatorePianoRequest request)throws BusinessException;

	public void update(Long idIndicatorePiano, AggiornaIndicatorePianoRequest request)throws BusinessException;

	public IndicatoreTargetVM readTarget(Long idIndicatorePiano)throws BusinessException;

	public List<IndicatoreRangeVM> readRange(Long idIndicatorePiano)throws BusinessException;

	public void saveTarget(Long idIndicatorePiano,SaveIndicatoreTargetRequest request)throws BusinessException;

	public void saveRange(Long idIndicatorePiano, SaveIndicatoreRangeRequest request)throws BusinessException;

	public List<IndicatorePianoListVM> indicatori(Long idNodoPiano)throws BusinessException;

}
