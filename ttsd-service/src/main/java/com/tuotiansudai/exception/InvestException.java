package com.tuotiansudai.exception;

public class InvestException extends BaseException {

    public InvestException() {
    }

    public InvestException(String msg) {
        super(msg);
    }

    public InvestException(String msg, Throwable e) {
        super(msg, e);
    }
}
