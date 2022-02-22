package com.petproject.boardgamefun.security.controller;

import com.petproject.boardgamefun.security.exception.RefreshTokenException;
import com.petproject.boardgamefun.security.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = RefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleRefreshTokenException(RefreshTokenException exception, WebRequest request) {
        return new ErrorMessage(HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false));
    }
}
