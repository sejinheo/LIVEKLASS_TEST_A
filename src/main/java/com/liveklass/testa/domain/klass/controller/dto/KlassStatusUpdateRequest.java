package com.liveklass.testa.domain.klass.controller.dto;

import com.liveklass.testa.domain.klass.domain.ClassStatus;
import jakarta.validation.constraints.NotNull;

public record KlassStatusUpdateRequest(
        @NotNull ClassStatus status
) {
}
