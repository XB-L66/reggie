package com.xb.reggie.exception;

import com.fasterxml.jackson.databind.ser.Serializers;

public class PassWordNotMatchException extends BaseException {
    public PassWordNotMatchException() {
        super();
    }

    public PassWordNotMatchException(String message) {
        super(message);
    }

    public PassWordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PassWordNotMatchException(Throwable cause) {
        super(cause);
    }

    protected PassWordNotMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
