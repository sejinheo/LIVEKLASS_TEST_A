package com.liveklass.testa.domain.enrollment.controller;

import com.liveklass.testa.domain.enrollment.application.EnrollmentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentUseCase enrollmentUseCase;

    @PreAuthorize("hasRole('CLASSMATE')")
    @PostMapping("/classes/{classId}/enrollments")
    public ResponseEntity<Void> enroll(@AuthenticationPrincipal Long accountId,
                                       @PathVariable Long classId) {
        enrollmentUseCase.enroll(accountId, classId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('CLASSMATE')")
    @PatchMapping("/enrollments/{enrollmentId}/confirm")
    public ResponseEntity<Void> confirm(@AuthenticationPrincipal Long accountId,
                                        @PathVariable Long enrollmentId) {
        enrollmentUseCase.confirm(accountId, enrollmentId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CLASSMATE')")
    @PatchMapping("/enrollments/{enrollmentId}/cancel")
    public ResponseEntity<Void> cancel(@AuthenticationPrincipal Long accountId,
                                       @PathVariable Long enrollmentId) {
        enrollmentUseCase.cancel(accountId, enrollmentId);
        return ResponseEntity.ok().build();
    }
}
