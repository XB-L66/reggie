package com.xb.reggie.exception;

public class CategoryRelateDishException extends BaseException{
    public CategoryRelateDishException() {
        super();
    }

    public CategoryRelateDishException(String message) {
        super(message);
    }

    public CategoryRelateDishException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryRelateDishException(Throwable cause) {
        super(cause);
    }

    protected CategoryRelateDishException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
