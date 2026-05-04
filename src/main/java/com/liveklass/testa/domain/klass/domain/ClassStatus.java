package com.liveklass.testa.domain.klass.domain;

import java.util.Set;

public enum ClassStatus {
    DRAFT(Set.of("OPEN")),
    OPEN(Set.of("CLOSED")),
    CLOSED(Set.of());

    private final Set<String> allowedTransitions;

    ClassStatus(Set<String> allowedTransitions) {
        this.allowedTransitions = allowedTransitions;
    }

    public boolean canTransitionTo(ClassStatus target) {
        return allowedTransitions.contains(target.name());
    }
}
