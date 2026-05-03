package com.liveklass.testa.domain.creator.exception;

import com.liveklass.testa.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.liveklass.testa.domain.creator.controller")
public class CreatorExceptionHandler {

    @ExceptionHandler(CreatorException.class)
    public ResponseEntity<ErrorResponse> handleCreatorException(CreatorException e) {
        ErrorResponse response = ErrorResponse.of(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}
