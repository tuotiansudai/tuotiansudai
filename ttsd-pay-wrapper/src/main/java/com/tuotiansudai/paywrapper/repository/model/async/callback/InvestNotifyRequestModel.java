package com.tuotiansudai.paywrapper.repository.model.async.callback;

import com.tuotiansudai.paywrapper.repository.model.InvestNotifyProcessStatus;

public class InvestNotifyRequestModel extends BaseCallbackRequestModel {

    private InvestNotifyProcessStatus status;

    public InvestNotifyProcessStatus getStatus() {
        return status;
    }

    public void setStatus(InvestNotifyProcessStatus status) {
        this.status = status;
    }
}
