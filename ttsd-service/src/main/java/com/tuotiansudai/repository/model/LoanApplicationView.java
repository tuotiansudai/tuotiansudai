package com.tuotiansudai.repository.model;

public class LoanApplicationView extends LoanApplicationModel {
    private String userName;
    private String mobile;

    public LoanApplicationView() {
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
}
