package com.tuotiansudai.paywrapper.repository.model.async.callback;

public class ProjectTransferNotifyRequestModel extends BaseCallbackRequestModel {

    private String merCheckDate;

    private Integer status;

    public String getMerCheckDate() {
        return merCheckDate;
    }

    public void setMerCheckDate(String merCheckDate) {
        this.merCheckDate = merCheckDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
