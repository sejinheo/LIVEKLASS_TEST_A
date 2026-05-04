package com.liveklass.testa.domain.enrollment.exception;

import org.springframework.http.HttpStatus;

public class EnrollmentAccessDeniedException extends EnrollmentException {

    public EnrollmentAccessDeniedException() {
        super("ENROLLMENT_ACCESS_DENIED", "해당 수강 신청에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
