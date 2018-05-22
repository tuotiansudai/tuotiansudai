package com.tuotiansudai.dto.sms;


import java.io.Serializable;

public class SmsRegisterSuccessNotifyDto implements Serializable{

    private String mobile;

    private String referrerMobile;

    public SmsRegisterSuccessNotifyDto() {
    }

    public SmsRegisterSuccessNotifyDto(String mobile, String referrerMobile) {
        this.mobile = mobile;
        this.referrerMobile = referrerMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }
}
