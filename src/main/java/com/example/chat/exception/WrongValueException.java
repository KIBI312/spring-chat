package com.example.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongValueException extends RuntimeException {
    public WrongValueException(String message) {
        super(message);
    }
}
