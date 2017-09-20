package com.tuotiansudai.paywrapper.repository.model.async.callback;

public class HuiZuActivateAccountNotifyRequestModel extends ProjectTransferNotifyRequestModel {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
