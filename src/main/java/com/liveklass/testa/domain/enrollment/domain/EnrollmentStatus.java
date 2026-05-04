package com.liveklass.testa.domain.enrollment.domain;

import java.util.Set;

public enum EnrollmentStatus {
    WAITLISTED(Set.of("PENDING", "CANCELLED")),
    PENDING(Set.of("CONFIRMED", "CANCELLED")),
    CONFIRMED(Set.of("CANCELLED")),
    CANCELLED(Set.of());

    private final Set<String> allowedTransitions;

    EnrollmentStatus(Set<String> allowedTransitions) {
        this.allowedTransitions = allowedTransitions;
    }

    public boolean canTransitionTo(EnrollmentStatus target) {
        return allowedTransitions.contains(target.name());
    }
}
