package com.ttsd.api.dto;

public class SendSmsRequestDto extends BaseParamDto{
    private String type;
    private String phoneNum;

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
