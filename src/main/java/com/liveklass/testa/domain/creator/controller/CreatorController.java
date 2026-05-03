package com.liveklass.testa.domain.creator.controller;

import com.liveklass.testa.domain.creator.application.CreatorUseCase;
import com.liveklass.testa.domain.creator.controller.dto.CreatorSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creators")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorUseCase creatorUseCase;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody CreatorSignUpRequest request) {
        creatorUseCase.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
