package com.tuotiansudai.web.dto;

import java.util.List;

/**
 * Created by Administrator on 2015/6/24.
 */
public class JsonDto {

    private RegisterVerificationStatus status;

    private List modelList;

    public RegisterVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(RegisterVerificationStatus status) {
        this.status = status;
    }

    public List getModelList() {
        return modelList;
    }

    public void setModelList(List modelList) {
        this.modelList = modelList;
    }
}
