package com.xb.reggie.exception;

public class DishDuplicatedException extends BaseException{
    public DishDuplicatedException() {
        super();
    }

    public DishDuplicatedException(String message) {
        super(message);
    }

    public DishDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DishDuplicatedException(Throwable cause) {
        super(cause);
    }

    protected DishDuplicatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
