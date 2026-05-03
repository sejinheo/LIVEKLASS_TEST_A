package com.liveklass.testa.domain.classmate.controller;

import com.liveklass.testa.domain.classmate.application.ClassmateUseCase;
import com.liveklass.testa.domain.classmate.controller.dto.ClassmateSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classmates")
@RequiredArgsConstructor
public class ClassmateController {

    private final ClassmateUseCase classmateUseCase;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody ClassmateSignUpRequest request) {
        classmateUseCase.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
