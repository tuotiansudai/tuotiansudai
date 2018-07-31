package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class UserBillPaginationView extends UserBillModel implements Serializable {

    private String userName;

    private String mobile;

    private String isStaff;

    private String umpUserName;

    public UserBillPaginationView() {
        super();
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

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public boolean isStaff() {
        return "1".equals(isStaff);
    }

    public String getUmpUserName() {
        return umpUserName;
    }

    public void setUmpUserName(String umpUserName) {
        this.umpUserName = umpUserName;
    }
}
