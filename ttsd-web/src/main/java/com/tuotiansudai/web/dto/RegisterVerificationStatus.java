package com.tuotiansudai.web.dto;

public enum RegisterVerificationStatus {

    SUCCESS("success"),
    FAIL("fail");

    private String status;

    RegisterVerificationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }


}
