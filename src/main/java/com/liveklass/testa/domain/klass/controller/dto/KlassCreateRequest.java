package com.liveklass.testa.domain.klass.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record KlassCreateRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull Integer price,
        @NotNull Integer maxCapacity,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
