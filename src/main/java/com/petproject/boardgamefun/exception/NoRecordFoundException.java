package com.petproject.boardgamefun.exception;

public class NoRecordFoundException extends RuntimeException {
    public NoRecordFoundException(String message) {
        super(message);
    }

    public NoRecordFoundException() {super();}
}
