package com.liveklass.testa.domain.classmate.application;

import com.liveklass.testa.domain.classmate.controller.dto.ClassmateSignUpRequest;

public interface ClassmateUseCase {

    void signUp(ClassmateSignUpRequest request);
}
