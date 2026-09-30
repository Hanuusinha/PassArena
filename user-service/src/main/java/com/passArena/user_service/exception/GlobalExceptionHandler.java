package com.passArena.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("timestamp" , LocalDateTime.now()
                ,"status" , 404, "error", "USER_NOT_FOUND",
                        "message",ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handlerUserAlreadyExistsException(UserAlreadyExistsException ex)
    {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("timestamp", LocalDateTime.now(),
                        "status", 409, "error", "USER_ALREADY_EXISTS",
                        "message", ex.getMessage()));
    }

}
