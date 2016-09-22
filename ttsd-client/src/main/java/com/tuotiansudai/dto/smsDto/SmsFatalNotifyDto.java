package com.tuotiansudai.dto.smsDto;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class SmsFatalNotifyDto implements Serializable {

    @NotEmpty
    private String errorMessage;

    public SmsFatalNotifyDto() {
    }

    public SmsFatalNotifyDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
