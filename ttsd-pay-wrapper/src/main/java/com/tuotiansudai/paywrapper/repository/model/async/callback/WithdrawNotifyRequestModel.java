package com.tuotiansudai.paywrapper.repository.model.async.callback;

public class WithdrawNotifyRequestModel extends BaseCallbackRequestModel {
    private String amount;

    private String tradeState;

    private String transferDate;

    private String transferSettleDate;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
