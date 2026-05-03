package com.liveklass.testa.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends AuthException {

    public AccountNotFoundException() {
        super("ACCOUNT_NOT_FOUND", "계정을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED);
    }
}
