package com.finconsgroup.performplus.rest.api.vm.v2.authentication;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@ToString
public class AuthorizationsResponse implements Serializable{
        List<AuthorizationsElement> elements;
        
}
