package com.ttsd.api.dto;

public class RegisterResponseDto {
    private String code;
    private String message;
    private RegisterDataDto data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public com.ttsd.api.dto.RegisterDataDto getData() {
        return data;
    }

    public void setData(com.ttsd.api.dto.RegisterDataDto data) {
        this.data = data;
    }
}
