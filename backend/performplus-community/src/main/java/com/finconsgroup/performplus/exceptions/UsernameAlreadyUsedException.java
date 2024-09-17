package com.finconsgroup.performplus.exceptions;

@SuppressWarnings("serial")
public class UsernameAlreadyUsedException extends RuntimeException {

    public UsernameAlreadyUsedException() {
        super("Login name already used!");
    }

}
