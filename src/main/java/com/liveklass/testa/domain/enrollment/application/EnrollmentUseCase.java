package com.liveklass.testa.domain.enrollment.application;

import com.liveklass.testa.domain.enrollment.controller.dto.ClassEnrollmentResponse;
import com.liveklass.testa.domain.enrollment.controller.dto.EnrollmentResponse;

import java.util.List;

public interface EnrollmentUseCase {

    void enroll(Long accountId, Long classId);

    void confirm(Long accountId, Long enrollmentId);

    void cancel(Long accountId, Long enrollmentId);

    List<EnrollmentResponse> findMyEnrollments(Long accountId);

    List<ClassEnrollmentResponse> findClassEnrollments(Long accountId, Long classId);
}
