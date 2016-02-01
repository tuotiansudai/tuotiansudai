package com.tuotiansudai.repository.model;

import java.io.Serializable;

public class LoanRepayNotifyModel implements Serializable {

    private long repayAmount;

    private String loanName;

    private String mobile;

    public long getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(long repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
