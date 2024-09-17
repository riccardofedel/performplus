package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode
@ToString
public class TemplateDataVM implements Serializable {
	
	private String container; 
	private Map<String,Object> formlyFieldConfig;
	private List<String> fields;
	
}
