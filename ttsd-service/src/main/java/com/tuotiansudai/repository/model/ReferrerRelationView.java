package com.tuotiansudai.repository.model;

import java.util.Date;

public class ReferrerRelationView extends ReferrerRelationModel {

    private Date registerTime;

    private String mobile;

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getMobile() { return mobile; }

    public void setMobile(String mobile) { this.mobile = mobile; }
}
