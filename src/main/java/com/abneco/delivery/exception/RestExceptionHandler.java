package com.abneco.delivery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<?> handleRequestException(RequestException e) {
        ExceptionResponseDetails details = ExceptionResponseDetails.Builder
                .newBuilder()
                .title("Request Exception")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(e.getMessage())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .message(e.getClass().getName())
                .build();
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

}
