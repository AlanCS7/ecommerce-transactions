package dev.alancss.handler;

import dev.alancss.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(ResourceNotFoundException ex) {
        var exceptionResponse = ExceptionResponse.builder()
                .status(NOT_FOUND.value())
                .timestamp(Instant.now())
                .error(ex.getMessage())
                .build();

        return ResponseEntity.status(NOT_FOUND).body(exceptionResponse);
    }

}