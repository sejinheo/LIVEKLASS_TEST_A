package com.liveklass.testa.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends AuthException {

    public InvalidPasswordException() {
        super("INVALID_PASSWORD", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
    }
}
