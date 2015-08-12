package com.tuotiansudai.paywrapper.repository.model.sync.response;

import java.util.Map;

public class MerRegisterPersonResponseModel extends BaseSyncResponseModel {

    private String registerDate;

    private String umpUserId;

    private String umpAccountId;

    @Override
    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.umpUserId = resData.get("user_id");
        this.umpAccountId = resData.get("account_id");
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getUmpUserId() {
        return umpUserId;
    }

    public void setUmpUserId(String umpUserId) {
        this.umpUserId = umpUserId;
    }

    public String getUmpAccountId() {
        return umpAccountId;
    }

    public void setUmpAccountId(String umpAccountId) {
        this.umpAccountId = umpAccountId;
    }
}
