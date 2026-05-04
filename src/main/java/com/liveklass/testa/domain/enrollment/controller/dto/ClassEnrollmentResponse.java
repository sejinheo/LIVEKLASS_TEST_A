package com.liveklass.testa.domain.enrollment.controller.dto;

import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;

import java.time.LocalDateTime;

public record ClassEnrollmentResponse(
        Long enrollmentId,
        Long classmateId,
        String classmateName,
        EnrollmentStatus status,
        LocalDateTime enrolledAt,
        LocalDateTime confirmedAt
) {
    public static ClassEnrollmentResponse from(Enrollment enrollment) {
        return new ClassEnrollmentResponse(
                enrollment.getId(),
                enrollment.getClassmate().getId(),
                enrollment.getClassmate().getName(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt(),
                enrollment.getConfirmedAt()
        );
    }
}
