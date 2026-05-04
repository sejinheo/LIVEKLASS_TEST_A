package com.liveklass.testa.domain.enrollment.controller;

import com.liveklass.testa.domain.enrollment.application.EnrollmentUseCase;
import com.liveklass.testa.domain.enrollment.controller.dto.ClassEnrollmentResponse;
import com.liveklass.testa.domain.enrollment.controller.dto.EnrollmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasRole('CLASSMATE')")
    @GetMapping("/enrollments/me")
    public ResponseEntity<Page<EnrollmentResponse>> findMyEnrollments(
            @AuthenticationPrincipal Long accountId,
            @PageableDefault(size = 20, sort = "enrolledAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(enrollmentUseCase.findMyEnrollments(accountId, pageable));
    }

    @PreAuthorize("hasRole('CREATOR')")
    @GetMapping("/classes/{classId}/enrollments")
    public ResponseEntity<Page<ClassEnrollmentResponse>> findClassEnrollments(
            @AuthenticationPrincipal Long accountId,
            @PathVariable Long classId,
            @PageableDefault(size = 20, sort = "enrolledAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(enrollmentUseCase.findClassEnrollments(accountId, classId, pageable));
    }
}
