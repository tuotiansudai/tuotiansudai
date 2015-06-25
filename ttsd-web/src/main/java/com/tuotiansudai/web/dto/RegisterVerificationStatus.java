package com.tuotiansudai.web.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RegisterVerificationStatus {

    SUCCESS("success"),
    FAIL("fail");

    private String status;

    private RegisterVerificationStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return this.status;
    }


}
