package com.tuotiansudai.repository.model;

import java.util.Date;

public class ReferrerRelationView extends ReferrerRelationModel {

    private Date registerTime;

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }
}
