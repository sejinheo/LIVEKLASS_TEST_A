package com.liveklass.testa.domain.enrollment.exception;

import org.springframework.http.HttpStatus;

public class ClassCapacityExceededException extends EnrollmentException {

    public ClassCapacityExceededException() {
        super("CLASS_CAPACITY_EXCEEDED", "수강 정원이 초과되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
