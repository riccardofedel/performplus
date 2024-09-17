package com.finconsgroup.performplus.exceptions;

@SuppressWarnings("serial")
public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException() {
        super("Incorrect password");
    }

}
