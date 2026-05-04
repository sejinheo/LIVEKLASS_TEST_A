package com.liveklass.testa.domain.enrollment.exception;

import org.springframework.http.HttpStatus;

public class CancellationPeriodExpiredException extends EnrollmentException {

    public CancellationPeriodExpiredException() {
        super("CANCELLATION_PERIOD_EXPIRED", "취소 가능 기간(결제 확정 후 7일)이 지났습니다.", HttpStatus.BAD_REQUEST);
    }
}
