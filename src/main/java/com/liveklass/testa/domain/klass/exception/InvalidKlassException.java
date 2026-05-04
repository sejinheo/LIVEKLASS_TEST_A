package com.liveklass.testa.domain.klass.exception;

import org.springframework.http.HttpStatus;

public class InvalidKlassException extends KlassException {

    public InvalidKlassException(String message) {
        super("INVALID_CLASS", message, HttpStatus.BAD_REQUEST);
    }
}
