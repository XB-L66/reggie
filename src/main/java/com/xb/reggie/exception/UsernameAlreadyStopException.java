package com.xb.reggie.exception;

public class UsernameAlreadyStopException extends BaseException{
    public UsernameAlreadyStopException() {
        super();
    }

    public UsernameAlreadyStopException(String message) {
        super(message);
    }

    public UsernameAlreadyStopException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyStopException(Throwable cause) {
        super(cause);
    }

    protected UsernameAlreadyStopException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
