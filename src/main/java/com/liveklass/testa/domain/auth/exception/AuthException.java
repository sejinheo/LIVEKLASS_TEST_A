package com.liveklass.testa.domain.auth.exception;

import com.liveklass.testa.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AuthException extends BusinessException {

    public AuthException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
