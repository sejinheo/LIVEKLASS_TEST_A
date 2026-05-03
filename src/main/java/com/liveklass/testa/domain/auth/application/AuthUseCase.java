package com.liveklass.testa.domain.auth.application;

import com.liveklass.testa.domain.auth.controller.dto.LoginRequest;

public interface AuthUseCase {

    String login(LoginRequest request);
}
