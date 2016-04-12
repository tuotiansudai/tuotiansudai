package com.tuotiansudai.paywrapper.repository.model.sync.response;

import java.util.Map;

public class ProjectTransferResponseModel extends BaseSyncResponseModel {
    private String merDate;
    private String tradeNo;
    private String merCheckDate;

    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.merDate = resData.get("mer_date");
        this.tradeNo = resData.get("trade_no");
        this.merCheckDate = resData.get("mer_check_date");
    }
}

