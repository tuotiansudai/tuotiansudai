package com.tuotiansudai.dto.sms;

import java.io.Serializable;

public class InvestSmsNotifyDto implements Serializable {
    private String mobile;
    private String loanName;
    private String amount;

    public InvestSmsNotifyDto() {
    }

    public InvestSmsNotifyDto(String mobile, String loanName, String amount) {
        this.mobile = mobile;
        this.loanName = loanName;
        this.amount = amount;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
