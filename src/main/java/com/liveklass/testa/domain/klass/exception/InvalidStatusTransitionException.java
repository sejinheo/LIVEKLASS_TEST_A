package com.liveklass.testa.domain.klass.exception;

import org.springframework.http.HttpStatus;

public class InvalidStatusTransitionException extends KlassException {

    public InvalidStatusTransitionException(String message) {
        super("INVALID_STATUS_TRANSITION", message, HttpStatus.BAD_REQUEST);
    }
}
