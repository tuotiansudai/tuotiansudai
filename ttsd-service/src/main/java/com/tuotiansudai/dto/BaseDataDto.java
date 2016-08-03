package com.tuotiansudai.dto;

import java.io.Serializable;

public class BaseDataDto implements Serializable {

    protected boolean status;

    protected String message;

    public BaseDataDto() {
    }

    public BaseDataDto(boolean status) {
        this.status = status;
    }

    public BaseDataDto(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
