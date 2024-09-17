package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class AuthorizationsElement implements Serializable{
	private String name;
	private Boolean enabled;
	private Boolean visible;
	List<AuthorizationsElement> children;

}
