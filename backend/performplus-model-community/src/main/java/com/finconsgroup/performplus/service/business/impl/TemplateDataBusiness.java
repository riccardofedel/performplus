package com.finconsgroup.performplus.service.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finconsgroup.performplus.domain.TemplateData;
import com.finconsgroup.performplus.repository.TemplateDataRepository;
import com.finconsgroup.performplus.rest.api.vm.TemplateDataVM;
import com.finconsgroup.performplus.service.business.ITemplateDataBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;

@Service
@Transactional
public class TemplateDataBusiness implements ITemplateDataBusiness{

	@Autowired
	private TemplateDataRepository templateDataRepository;
	
	@Override
	@Transactional(readOnly = true)
	public TemplateDataVM getContainer(Long idEnte, String container) throws BusinessException {
		TemplateDataVM out=new TemplateDataVM();
		TemplateData t=templateDataRepository.findByIdEnteAndContainerAndTypeIsNull(idEnte, container);
		if(t==null)
			return out;
		out.setContainer(container);
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			out.setFormlyFieldConfig(new ObjectMapper().readValue(t.getFormlyFieldConfig(), map.getClass()));
		} catch (Exception e) {
			System.out.println("getContainer error:"+e.getMessage());
		}
		return out;
	}

	@Override
	@Transactional(readOnly = true)
	public TemplateDataVM findByContainerAndType(Long idEnte, String container, String type) {
		TemplateDataVM out=new TemplateDataVM();
		TemplateData t=templateDataRepository.findByIdEnteAndContainerAndType(idEnte, container, type);
		if(t==null)
			return out;
		out.setContainer(container);
		List<String> list=new ArrayList<String>();
		try {
			out.setFields(new ObjectMapper().readValue(t.getFormlyFieldConfig(), list.getClass()));
		} catch (Exception e) {
			System.out.println("findByContainerAndType error:"+e.getMessage());
		}
		
		return out;
	}

}
