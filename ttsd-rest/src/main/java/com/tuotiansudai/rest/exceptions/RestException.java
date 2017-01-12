package com.tuotiansudai.rest.exceptions;

public class RestException extends RuntimeException {
    private final ErrorCode errorCode;

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public RestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public RestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public RestException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RestException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }
}
