package com.liveklass.testa.domain.enrollment.exception;

import com.liveklass.testa.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class EnrollmentException extends BusinessException {

    public EnrollmentException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
