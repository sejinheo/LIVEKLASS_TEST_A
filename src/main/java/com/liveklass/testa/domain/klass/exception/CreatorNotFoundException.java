package com.liveklass.testa.domain.klass.exception;

import org.springframework.http.HttpStatus;

public class CreatorNotFoundException extends KlassException {

    public CreatorNotFoundException() {
        super("CREATOR_NOT_FOUND", "크리에이터 정보를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
