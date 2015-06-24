package com.tuotiansudai.web.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class RegisterVerifyDto {
    @JsonView(RegisterVerifyJsonView.RegisterVerify.class)
    private String status;
    @JsonView(RegisterVerifyJsonView.RegisterVerify.class)
    private Data data;

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
