package com.liveklass.testa.domain.enrollment.exception;

import org.springframework.http.HttpStatus;

public class ClassmateNotFoundException extends EnrollmentException {

    public ClassmateNotFoundException() {
        super("CLASSMATE_NOT_FOUND", "수강생 정보를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
