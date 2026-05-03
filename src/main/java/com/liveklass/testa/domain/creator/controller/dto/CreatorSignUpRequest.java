package com.liveklass.testa.domain.creator.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreatorSignUpRequest(
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String name
) {
}
