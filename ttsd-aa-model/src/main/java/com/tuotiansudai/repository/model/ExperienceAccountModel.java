package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class ExperienceAccountModel implements Serializable {
    private long id;
    private String loginName;
    private long experienceBalance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getExperienceBalance() {
        return experienceBalance;
    }

    public void setExperienceBalance(long experienceBalance) {
        this.experienceBalance = experienceBalance;
    }
}
