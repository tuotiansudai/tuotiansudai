package com.tuotiansudai.message;


import java.io.Serializable;

public class RepaySuccessAsyncCallBackMessage implements Serializable {

    private Long notifyRequestId;

    private boolean advanced;

    public RepaySuccessAsyncCallBackMessage(){}

    public RepaySuccessAsyncCallBackMessage(Long notifyRequestId, boolean advanced) {
        this.notifyRequestId = notifyRequestId;
        this.advanced = advanced;
    }

    public Long getNotifyRequestId() {
        return notifyRequestId;
    }

    public void setNotifyRequestId(Long notifyRequestId) {
        this.notifyRequestId = notifyRequestId;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }
}
