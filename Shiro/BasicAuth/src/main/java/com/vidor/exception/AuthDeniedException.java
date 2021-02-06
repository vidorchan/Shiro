package com.vidor.exception;

import javax.servlet.ServletException;

public class AuthDeniedException extends ServletException {

    public AuthDeniedException(String message) {
        super(message);
    }
}
