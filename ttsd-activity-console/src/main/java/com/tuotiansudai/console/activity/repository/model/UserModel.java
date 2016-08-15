package com.tuotiansudai.console.activity.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserModel implements Serializable {

    private long id;

    private String loginName;

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
}
