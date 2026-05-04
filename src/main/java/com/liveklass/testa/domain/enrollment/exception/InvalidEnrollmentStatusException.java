package com.liveklass.testa.domain.enrollment.exception;

import org.springframework.http.HttpStatus;

public class InvalidEnrollmentStatusException extends EnrollmentException {

    public InvalidEnrollmentStatusException(String message) {
        super("INVALID_ENROLLMENT_STATUS", message, HttpStatus.BAD_REQUEST);
    }
}
