package com.tuotiansudai.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/15.
 */
public class AgreementDto implements Serializable {

    private String loginName;

    private boolean autoInvest;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }
}
