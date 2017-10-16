package com.tuotiansudai.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserRestResponseBase implements Serializable {
    @JsonProperty("result")
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
