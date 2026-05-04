package com.liveklass.testa.domain.klass.exception;

import org.springframework.http.HttpStatus;

public class KlassAccessDeniedException extends KlassException {

    public KlassAccessDeniedException() {
        super("CLASS_ACCESS_DENIED", "해당 강의에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
