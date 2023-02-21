package com.abneco.delivery.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequestException extends RuntimeException {
    private final String message;

    public RequestException(String message) {
        this.message = message;
    }
}