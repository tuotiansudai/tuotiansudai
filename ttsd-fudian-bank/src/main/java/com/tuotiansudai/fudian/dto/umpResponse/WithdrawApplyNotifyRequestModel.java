package com.tuotiansudai.fudian.dto.umpResponse;

public class WithdrawApplyNotifyRequestModel extends BaseCallbackRequestModel {

    private String amount;

    private String comAmt;

    private String comAmtType;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
