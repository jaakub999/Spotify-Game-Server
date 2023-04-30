package com.spotify.game.exception;

public class SgRuntimeException extends RuntimeException {
    public SgRuntimeException(ExceptionCode exceptionCode) {
        super(exceptionCode.getCode());
    }
}