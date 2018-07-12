package com.tuotiansudai.fudian.dto.umpResponse;

public class RechargeNotifyModel extends BaseNotifyModel {

    private String merCheckDate;

    private String balance;

    private String comAmt;

    private String comAmtType;

    public String getMerCheckDate() {
        return merCheckDate;
    }

    public void setMerCheckDate(String merCheckDate) {
        this.merCheckDate = merCheckDate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getComAmt() {
        return comAmt;
    }

    public void setComAmt(String comAmt) {
        this.comAmt = comAmt;
    }

    public String getComAmtType() {
        return comAmtType;
    }

    public void setComAmtType(String comAmtType) {
        this.comAmtType = comAmtType;
    }
}
