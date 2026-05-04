package com.liveklass.testa.domain.enrollment.exception;

import org.springframework.http.HttpStatus;

public class EnrollmentNotFoundException extends EnrollmentException {

    public EnrollmentNotFoundException() {
        super("ENROLLMENT_NOT_FOUND", "수강 신청 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
