package com.tuotiansudai.rest.exceptions;

import org.springframework.util.StringUtils;

import java.io.Serializable;

public class RestExceptionDto implements Serializable {
    private final int code;
    private final String message;
    private final String detail;

    public RestExceptionDto(int code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public RestExceptionDto(ErrorCode errorCode, String detail) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.detail = detail;
    }

    private RestExceptionDto(RestException exception) {
        this.code = exception.getErrorCode().getCode();
        this.message = exception.getErrorCode().getMessage();
        this.detail = exception.getLocalizedMessage();
    }

    public static RestExceptionDto fromException(Exception exception) {
        if (exception instanceof RestException) {
            return new RestExceptionDto((RestException) exception);
        } else {
            String message = exception.getLocalizedMessage();
            if (StringUtils.isEmpty(message)) {
                message = exception.getClass().getCanonicalName();
            }
            return new RestExceptionDto(RestErrorCode.UnKnown, message);
        }
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }
}
