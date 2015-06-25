package com.tuotiansudai.web.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class RegisterVerifyDto {
    @JsonView(RegisterVerifyJsonView.RegisterVerify.class)
    private RegisterVerificationStatus status;
    @JsonView(RegisterVerifyJsonView.RegisterVerify.class)
    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public RegisterVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(RegisterVerificationStatus status) {
        this.status = status;
    }

}
