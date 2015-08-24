package com.tuotiansudai.paywrapper.repository.model.async.callback;

public class RechargeNotifyRequestModel extends BaseCallbackRequestModel {
    private String merCheckDate;
    private String balance;
    private String comAmt;


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
}
