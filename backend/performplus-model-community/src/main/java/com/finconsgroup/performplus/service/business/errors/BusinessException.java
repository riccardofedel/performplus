package com.finconsgroup.performplus.service.business.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@SuppressWarnings("serial")
@ResponseStatus 
public class BusinessException extends ResponseStatusException {
    private final String code;
    
    public BusinessException(String code,String message, Throwable cause) {
		this(HttpStatus.INTERNAL_SERVER_ERROR,code,message, cause);
	}

	public BusinessException(HttpStatus status,String code,String message, Throwable cause) {
		super(status, message, cause);
		this.code=code;
	}

	public BusinessException(Throwable cause) {
		this("system",cause.getMessage(),cause);
	}
	public BusinessException(HttpStatus status,Throwable cause) {
		this(status,"system",cause.getMessage(),cause);
	}

	public BusinessException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR,"Business Exception");
        this.code="system";
    }
	public BusinessException(String message) {
        this("system",message);
    }

    public BusinessException(HttpStatus status, String message) {
    	super(status,message);
    	this.code="system";
     }
 
    public BusinessException(String code, String message) {
    	super(HttpStatus.INTERNAL_SERVER_ERROR,message);
    	this.code=code;
    }
	public BusinessException(HttpStatus status, String code, String message) {
    	super(status,message);
    	this.code=code;
	}

	public String getCode() {
        return code;
    }


}