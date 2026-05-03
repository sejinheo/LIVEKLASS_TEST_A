package com.liveklass.testa.domain.classmate.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClassmateSignUpRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String name
) {
}
