package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class BankRechargePaginationView extends BankRechargeModel implements Serializable {

    private String userName;

    private String mobile;

    private String isStaff;

    public BankRechargePaginationView() {

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

    public boolean isStaff(){
        return "1".equals(isStaff);
    }
}
