package com.liveklass.testa.domain.klass.controller;

import com.liveklass.testa.domain.klass.application.KlassUseCase;
import com.liveklass.testa.domain.klass.controller.dto.KlassCreateRequest;
import com.liveklass.testa.domain.klass.controller.dto.KlassStatusUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class KlassController {

    private final KlassUseCase klassUseCase;

    @PreAuthorize("hasRole('CREATOR')")
    @PostMapping
    public ResponseEntity<Void> create(@AuthenticationPrincipal Long accountId,
                                       @Valid @RequestBody KlassCreateRequest request) {
        klassUseCase.create(accountId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasRole('CREATOR')")
    @PatchMapping("/{classId}/status")
    public ResponseEntity<Void> updateStatus(@AuthenticationPrincipal Long accountId,
                                             @PathVariable Long classId,
                                             @Valid @RequestBody KlassStatusUpdateRequest request) {
        klassUseCase.updateStatus(accountId, classId, request);
        return ResponseEntity.ok().build();
    }
}
