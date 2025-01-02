package com.codeflix.admin.catalogo.infrastructure.api.controllers;

import com.codeflix.admin.catalogo.domain.exceptions.DomainException;
import com.codeflix.admin.catalogo.domain.exceptions.NotFoundException;
import com.codeflix.admin.catalogo.domain.validation.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<?> handleDomainException(final DomainException exception) {
        return ResponseEntity.unprocessableEntity().body(APIError.from(exception));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(final NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIError.from(exception));
    }

    record APIError(String message, List<Error> errors) {
        static APIError from(final DomainException exception) {
            return new APIError(exception.getMessage(), exception.getErrors());
        }
    }
}
