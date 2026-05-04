package com.liveklass.testa.domain.classmate.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends ClassmateException {

    public DuplicateEmailException() {
        super("DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT);
    }
}
