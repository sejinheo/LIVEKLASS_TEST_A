package com.liveklass.testa.domain.klass.exception;

import org.springframework.http.HttpStatus;

public class KlassNotFoundException extends KlassException {

    public KlassNotFoundException() {
        super("CLASS_NOT_FOUND", "강의를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
