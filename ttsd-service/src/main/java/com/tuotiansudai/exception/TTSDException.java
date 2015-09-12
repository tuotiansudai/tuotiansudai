package com.tuotiansudai.exception;

/**
 * 异常的基类，便于Controller统一catch
 */
public class TTSDException extends Exception {
    public TTSDException() {
    }

    public TTSDException(String msg) {
        super(msg);
    }

    public TTSDException(String msg, Throwable e) {
        super(msg, e);
    }
}
