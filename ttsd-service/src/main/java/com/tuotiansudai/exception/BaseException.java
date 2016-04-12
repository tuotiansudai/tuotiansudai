package com.tuotiansudai.exception;

public class BaseException extends Exception {

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
