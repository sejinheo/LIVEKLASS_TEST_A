package com.liveklass.testa.domain.enrollment.application;

import com.liveklass.testa.domain.enrollment.controller.dto.ClassEnrollmentResponse;
import com.liveklass.testa.domain.enrollment.controller.dto.EnrollmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnrollmentUseCase {

    void enroll(Long accountId, Long classId);

    void confirm(Long accountId, Long enrollmentId);

    void cancel(Long accountId, Long enrollmentId);

    Page<EnrollmentResponse> findMyEnrollments(Long accountId, Pageable pageable);

    Page<ClassEnrollmentResponse> findClassEnrollments(Long accountId, Long classId, Pageable pageable);
}
