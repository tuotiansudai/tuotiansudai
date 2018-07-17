package com.tuotiansudai.repository.model;

import java.io.Serializable;

/**
 * Created by qduljs2011 on 2018/7/10.
 */
public class RechargePaginationView extends RechargeModel implements Serializable {
    String userName;

    String mobile;

    String isStaff;

    public RechargePaginationView() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }
}
