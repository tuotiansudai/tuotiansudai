package com.tuotiansudai.paywrapper.repository.model.sync.response;

import java.util.Map;

public class MerRegisterPersonResponseModel extends BaseSyncResponseModel {

    private String regDate;

    private String userId;

    private String accountId;

    @Override
    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.userId = resData.get("user_id");
        this.accountId = resData.get("account_id");
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
