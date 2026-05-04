package com.liveklass.testa.domain.klass.exception;

import com.liveklass.testa.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.liveklass.testa.domain.klass.controller")
public class KlassExceptionHandler {

    @ExceptionHandler(KlassException.class)
    public ResponseEntity<ErrorResponse> handleKlassException(KlassException e) {
        ErrorResponse response = ErrorResponse.of(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}
