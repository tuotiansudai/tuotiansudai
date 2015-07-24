package com.ttsd.api.dto;

public class SendSmsRequestDto {
    private com.ttsd.api.dto.BaseParam baseParam;
    private String type;
    private String phoneNum;

    public com.ttsd.api.dto.BaseParam getBaseParam() {
        return baseParam;
    }

    public void setBaseParam(com.ttsd.api.dto.BaseParam baseParam) {
        this.baseParam = baseParam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
