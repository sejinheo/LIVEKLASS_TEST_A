package com.liveklass.testa.domain.enrollment.exception;

import org.springframework.http.HttpStatus;

public class ClassNotOpenException extends EnrollmentException {

    public ClassNotOpenException() {
        super("CLASS_NOT_OPEN", "모집 중인 강의에만 신청할 수 있습니다.", HttpStatus.BAD_REQUEST);
    }
}
