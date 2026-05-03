package com.liveklass.testa.domain.creator.application;

import com.liveklass.testa.domain.creator.controller.dto.CreatorSignUpRequest;

public interface CreatorUseCase {

    void signUp(CreatorSignUpRequest request);
}
