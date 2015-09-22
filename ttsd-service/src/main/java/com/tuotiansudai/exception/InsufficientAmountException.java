package com.tuotiansudai.exception;

/**
 * 金额不足的异常
 */
public class InsufficientAmountException extends TTSDException {

    public InsufficientAmountException() {
    }

    public InsufficientAmountException(String msg) {
        super(msg);
    }

    public InsufficientAmountException(String msg, Throwable e) {
        super(msg, e);
    }
}
