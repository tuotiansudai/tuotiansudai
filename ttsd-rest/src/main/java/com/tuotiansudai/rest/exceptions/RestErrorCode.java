package com.tuotiansudai.rest.exceptions;

public enum RestErrorCode implements ErrorCode {
    UnKnown(10000, "unknown exception"),
    LoginNameNotPresent(10101, "login name not present in request");

    private final int code;
    private final String message;

    RestErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
