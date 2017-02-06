package com.tuotiansudai.rest.support.client.exceptions;

import com.tuotiansudai.rest.support.client.dto.ErrorResponse;
import feign.Request;
import feign.Response;

public class RestException extends RuntimeException {
    private final int status;
    private final String reason;
    private final ErrorResponse response;
    private final Response rawResponse;
    private final Request rawRequest;

    public RestException(Response response, ErrorResponse errorResponse) {
        super(buildExceptionMessage(response, errorResponse));
        this.rawResponse = response;
        this.rawRequest = response.request();
        this.status = response.status();
        this.reason = response.reason();
        this.response = errorResponse;
    }

    private static String buildExceptionMessage(Response response, ErrorResponse errorResponse) {
        if (errorResponse != null) {
            return String.format("status: %d, code: %d, message: %s, exception: %s",
                    response.status(),
                    errorResponse.getCode(), errorResponse.getMessage(), errorResponse.getException());
        } else {
            return String.format("status: %d, reason: %s", response.status(), response.reason());
        }
    }

    public boolean isClientException() {
        return status >= 400 && status <= 499;
    }

    public boolean isServerException() {
        return status >= 500 && status <= 599;
    }

    public int getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public ErrorResponse getResponse() {
        return response;
    }

    public Response getRawResponse() {
        return rawResponse;
    }

    public Request getRawRequest() {
        return rawRequest;
    }
}
