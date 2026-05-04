package com.liveklass.testa.domain.klass.application;

import com.liveklass.testa.domain.klass.controller.dto.KlassCreateRequest;
import com.liveklass.testa.domain.klass.controller.dto.KlassStatusUpdateRequest;

public interface KlassUseCase {

    void create(Long accountId, KlassCreateRequest request);

    void updateStatus(Long accountId, Long classId, KlassStatusUpdateRequest request);
}
