package com.tuotiansudai.activity.repository.dto;


import java.util.Date;

public class RedEnvelopSplitReferrerDto {

    private String mobile;

    private Date registerTime;

    public RedEnvelopSplitReferrerDto(String mobile, Date registerTime) {
        this.mobile = mobile;
        this.registerTime = registerTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
