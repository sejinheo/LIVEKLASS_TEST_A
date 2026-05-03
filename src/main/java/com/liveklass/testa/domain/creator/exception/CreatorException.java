package com.liveklass.testa.domain.creator.exception;

import com.liveklass.testa.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CreatorException extends BusinessException {

    public CreatorException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
