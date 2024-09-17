package com.finconsgroup.performplus.exceptions;

@SuppressWarnings("serial")
public class EmailAlreadyUsedException extends RuntimeException {

    public EmailAlreadyUsedException() {
        super("Email is already in use!");
    }

}
