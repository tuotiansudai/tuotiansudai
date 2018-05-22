package com.tuotiansudai.dto.sms;


import java.io.Serializable;

public class SmsUsePointNotifyDto implements Serializable{

    private String mobile;

    private String usePoint;

    private String surplusPoint;

    public SmsUsePointNotifyDto() {
    }

    public SmsUsePointNotifyDto(String mobile, String usePoint, String surplusPoint) {
        this.mobile = mobile;
        this.usePoint = usePoint;
        this.surplusPoint = surplusPoint;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsePoint() {
        return usePoint;
    }

    public void setUsePoint(String usePoint) {
        this.usePoint = usePoint;
    }

    public String getSurplusPoint() {
        return surplusPoint;
    }

    public void setSurplusPoint(String surplusPoint) {
        this.surplusPoint = surplusPoint;
    }
}
