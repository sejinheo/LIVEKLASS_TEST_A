package com.liveklass.testa.domain.klass.application;

import com.liveklass.testa.domain.klass.controller.dto.KlassCreateRequest;
import com.liveklass.testa.domain.klass.controller.dto.KlassResponse;
import com.liveklass.testa.domain.klass.controller.dto.KlassStatusUpdateRequest;
import com.liveklass.testa.domain.klass.domain.ClassStatus;

import java.util.List;

public interface KlassUseCase {

    void create(Long accountId, KlassCreateRequest request);

    void updateStatus(Long accountId, Long classId, KlassStatusUpdateRequest request);

    List<KlassResponse> findAll(ClassStatus status);
}
