package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestNotifyInfo;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class InvestSmsNotifyDto implements Serializable {
    private String loanName;
    private String mobile;
    private String amount;

    public InvestSmsNotifyDto() {
    }

    public InvestSmsNotifyDto(InvestNotifyInfo notifyInfo) {
        this.loanName = notifyInfo.getLoanName();
        this.mobile = notifyInfo.getMobile();
        this.amount = AmountConverter.convertCentToString(notifyInfo.getAmount());
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
