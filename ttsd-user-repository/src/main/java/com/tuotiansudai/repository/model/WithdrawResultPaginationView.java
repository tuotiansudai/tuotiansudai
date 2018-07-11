package com.tuotiansudai.repository.model;

import java.io.Serializable;

/**
 * Created by qduljs2011 on 2018/7/11.
 */
public class WithdrawResultPaginationView extends WithdrawModel implements Serializable {
    private String userName;

    private String mobile;

    private String isStaff;

    public WithdrawResultPaginationView() {
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
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
