package com.liveklass.testa.domain.enrollment.application;

public interface EnrollmentUseCase {

    void enroll(Long accountId, Long classId);

    void confirm(Long accountId, Long enrollmentId);

    void cancel(Long accountId, Long enrollmentId);
}
