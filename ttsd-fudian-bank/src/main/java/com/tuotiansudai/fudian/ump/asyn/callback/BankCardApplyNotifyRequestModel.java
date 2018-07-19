package com.tuotiansudai.fudian.ump.asyn.callback;

public class BankCardApplyNotifyRequestModel extends BaseCallbackRequestModel {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}