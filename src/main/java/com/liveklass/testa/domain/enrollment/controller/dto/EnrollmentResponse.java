package com.liveklass.testa.domain.enrollment.controller.dto;

import com.liveklass.testa.domain.enrollment.domain.Enrollment;
import com.liveklass.testa.domain.enrollment.domain.EnrollmentStatus;

import java.time.LocalDateTime;

public record EnrollmentResponse(
        Long id,
        Long classId,
        String classTitle,
        EnrollmentStatus status,
        LocalDateTime enrolledAt,
        LocalDateTime confirmedAt,
        LocalDateTime cancelledAt
) {
    public static EnrollmentResponse from(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getKlass().getId(),
                enrollment.getKlass().getTitle(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt(),
                enrollment.getConfirmedAt(),
                enrollment.getCancelledAt()
        );
    }
}
