package com.liveklass.testa.domain.klass.controller.dto;

import com.liveklass.testa.domain.klass.domain.ClassStatus;
import com.liveklass.testa.domain.klass.domain.Klass;

import java.time.LocalDate;

public record KlassDetailResponse(
        Long id,
        String title,
        String description,
        int price,
        int maxCapacity,
        int currentEnrollment,
        LocalDate startDate,
        LocalDate endDate,
        ClassStatus status,
        String creatorName
) {
    public static KlassDetailResponse of(Klass klass, int currentEnrollment) {
        return new KlassDetailResponse(
                klass.getId(),
                klass.getTitle(),
                klass.getDescription(),
                klass.getPrice(),
                klass.getMaxCapacity(),
                currentEnrollment,
                klass.getStartDate(),
                klass.getEndDate(),
                klass.getStatus(),
                klass.getCreator().getName()
        );
    }
}
