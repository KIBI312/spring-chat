package com.example.chat.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendToUser("/queue/messages")
    public String methodArgumentNotValidWebSocketExceptionHandler(MethodArgumentNotValidException e) {
        return "Malformed message detected";
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/messages")
    public String somethingGoesWrong(Exception e) {
        return e.getMessage();
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception exc, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            new Date(),
            exc.getMessage(),
            request.getDescription(false));
    return message;
    }
}