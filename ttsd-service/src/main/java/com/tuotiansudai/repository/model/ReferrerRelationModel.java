package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class ReferrerRelationModel implements Serializable {

    private String referrerLoginName;

    private String loginName;

    private int level;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public void setReferrerLoginName(String referrerLoginName) {
        this.referrerLoginName = referrerLoginName;
    }
}
