package com.tuotiansudai.paywrapper.repository.model.async.callback;

public class ExtraRateNotifyRequestModel extends ProjectTransferNotifyRequestModel {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
