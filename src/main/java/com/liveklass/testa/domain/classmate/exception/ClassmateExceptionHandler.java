package com.liveklass.testa.domain.classmate.exception;

import com.liveklass.testa.global.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.liveklass.testa.domain.classmate.controller")
public class ClassmateExceptionHandler {

    @ExceptionHandler(ClassmateException.class)
    public ResponseEntity<ErrorResponse> handleClassmateException(ClassmateException e) {
        ErrorResponse response = ErrorResponse.of(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}
