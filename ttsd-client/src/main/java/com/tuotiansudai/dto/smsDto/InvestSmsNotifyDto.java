package com.tuotiansudai.dto.smsDto;

import java.io.Serializable;

public class InvestSmsNotifyDto implements Serializable {
    private String loanName;
    private String mobile;
    private String amount;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
