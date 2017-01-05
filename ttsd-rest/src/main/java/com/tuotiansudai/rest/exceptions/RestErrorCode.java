package com.tuotiansudai.rest.exceptions;

public enum RestErrorCode implements ErrorCode {
    Empty(0, "no message available"),
    UnexpectedError(10002, "unexpected error"),
    RequestDataValidFailed(10101, "validation failed for argument in request data"),
    LoginNameNotPresent(10201, "login name not present in request");

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
