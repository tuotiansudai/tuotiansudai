package com.tuotiansudai.exception;

public class EditUserException extends BaseException {

    public EditUserException() {
    }

    public EditUserException(String msg) {
        super(msg);
    }

    public EditUserException(String msg, Throwable e) {
        super(msg, e);
    }
}
