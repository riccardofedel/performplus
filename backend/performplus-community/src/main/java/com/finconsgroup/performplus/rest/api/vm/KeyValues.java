package com.finconsgroup.performplus.rest.api.vm;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class KeyValues implements Serializable{
	private String key;
	private List<String> values;
	public KeyValues() {}
	public KeyValues(String key, List<String> values) {
		super();
		this.key = key;
		this.values = values;
	}
	
}
