package com.some.project.dataJpa.reservation_system;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
            Exception e
    ) {
        log.error("Handle exception", e);

        var errorDto = new ErrorResponseDto(
                "Internal server error",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(
            EntityNotFoundException e
    ) {
        log.error("Handle EntityNotFoundException", e);

        var errorDto = new ErrorResponseDto(
                "Entity not found error",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler(exception = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(
            Exception e
    ) {
        log.error("Handle handleBadRequest", e);

        var errorDto = new ErrorResponseDto(
                "Bad request",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }
}
