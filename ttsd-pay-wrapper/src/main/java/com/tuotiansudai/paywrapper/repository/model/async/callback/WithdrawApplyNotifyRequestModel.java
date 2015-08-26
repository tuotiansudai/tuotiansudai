package com.tuotiansudai.paywrapper.repository.model.async.callback;

public class WithdrawApplyNotifyRequestModel extends BaseCallbackRequestModel{

    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
