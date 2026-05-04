package com.liveklass.testa.domain.klass.controller;

import com.liveklass.testa.domain.klass.application.KlassUseCase;
import com.liveklass.testa.domain.klass.controller.dto.KlassCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
