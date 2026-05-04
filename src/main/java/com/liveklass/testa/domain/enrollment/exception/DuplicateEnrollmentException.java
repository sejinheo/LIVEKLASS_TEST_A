package com.liveklass.testa.domain.enrollment.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEnrollmentException extends EnrollmentException {

    public DuplicateEnrollmentException() {
        super("DUPLICATE_ENROLLMENT", "이미 신청한 강의입니다.", HttpStatus.CONFLICT);
    }
}
