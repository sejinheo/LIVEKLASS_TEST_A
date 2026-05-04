package com.liveklass.testa.domain.klass.controller.dto;

import com.liveklass.testa.domain.klass.domain.ClassStatus;
import com.liveklass.testa.domain.klass.domain.Klass;

import java.time.LocalDate;

public record KlassResponse(
        Long id,
        String title,
        String description,
        int price,
        int maxCapacity,
        LocalDate startDate,
        LocalDate endDate,
        ClassStatus status,
        String creatorName
) {
    public static KlassResponse from(Klass klass) {
        return new KlassResponse(
                klass.getId(),
                klass.getTitle(),
                klass.getDescription(),
                klass.getPrice(),
                klass.getMaxCapacity(),
                klass.getStartDate(),
                klass.getEndDate(),
                klass.getStatus(),
                klass.getCreator().getName()
        );
    }
}
