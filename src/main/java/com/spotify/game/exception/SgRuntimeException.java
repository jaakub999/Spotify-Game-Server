package com.spotify.game.exception;

import lombok.Getter;

import static com.spotify.game.util.HostName.currentHostName;

@Getter
public class SgRuntimeException extends RuntimeException {

    private final ExceptionCode code;

    public SgRuntimeException(ExceptionCode code) {
        super(String.format("%s: code: %s message: %s",
                currentHostName(), code.name(), code.getMessage()));
        this.code = code;
    }

    public SgRuntimeException(ExceptionCode code, Throwable cause) {
        super(String.format("%s: code: %s message: %s cause: %s",
                currentHostName(), code.name(), code.getMessage(), cause.getMessage()), cause);
        this.code = code;
    }
}