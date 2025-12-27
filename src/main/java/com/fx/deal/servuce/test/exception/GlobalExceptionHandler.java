package com.fx.deal.servuce.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse handleValidationErrors(MethodArgumentNotValidException ex) {

        List<ValidationErrorResponse.FieldError> errors =
                ex.getBindingResult().getFieldErrors()
                        .stream()
                        .map(error -> new ValidationErrorResponse.FieldError(
                                error.getField(),
                                error.getDefaultMessage()
                        ))
                        .collect(Collectors.toList());

        return new ValidationErrorResponse(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleDuplicateDeal(IllegalArgumentException ex) {
        if (ex.getMessage().startsWith("Duplicate dealId")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
    }

}