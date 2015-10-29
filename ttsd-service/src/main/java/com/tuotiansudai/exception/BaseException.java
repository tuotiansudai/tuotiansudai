package com.tuotiansudai.exception;

public class BaseException extends Exception {
    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String msg, Throwable e) {
        super(msg, e);
    }
}
