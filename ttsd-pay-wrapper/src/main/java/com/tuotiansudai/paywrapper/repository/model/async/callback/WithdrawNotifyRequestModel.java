package com.tuotiansudai.paywrapper.repository.model.async.callback;

public class WithdrawNotifyRequestModel extends BaseCallbackRequestModel {
    private String amount;

    private String comAmt;

    private String comAmtType;

    private String tradeState;

    private String transferDate;

    private String transferSettleDate;

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

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getTransferSettleDate() {
        return transferSettleDate;
    }

    public void setTransferSettleDate(String transferSettleDate) {
        this.transferSettleDate = transferSettleDate;
    }
}
