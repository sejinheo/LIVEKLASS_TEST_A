package com.liveklass.testa.domain.klass.exception;

import com.liveklass.testa.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class KlassException extends BusinessException {

    public KlassException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
