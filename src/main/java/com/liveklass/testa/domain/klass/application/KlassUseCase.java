package com.liveklass.testa.domain.klass.application;

import com.liveklass.testa.domain.klass.controller.dto.KlassCreateRequest;

public interface KlassUseCase {

    void create(Long accountId, KlassCreateRequest request);
}
