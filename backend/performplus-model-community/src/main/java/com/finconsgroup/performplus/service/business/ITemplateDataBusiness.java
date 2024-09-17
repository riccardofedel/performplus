package com.finconsgroup.performplus.service.business;

import com.finconsgroup.performplus.rest.api.vm.TemplateDataVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;



public interface ITemplateDataBusiness {

	public TemplateDataVM getContainer(Long idEnte, String container) throws BusinessException;

	public TemplateDataVM findByContainerAndType(Long idEnte, String container, String type);


}
