package com.tuotiansudai.paywrapper.repository.model.sync.request;

import java.util.Map;

public class UserSearchRequestModel extends BaseSyncRequestModel {
    private String userId;

    private String isFindAccount;

    private String isSelectAgreement;

    public UserSearchRequestModel() {
    }

    public UserSearchRequestModel(String payUserId) {
        super();
        this.service = "user_search";
        this.userId = payUserId;
        this.isFindAccount = "01"; //查询余额
        this.isSelectAgreement = "1"; //查询授权协议
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("user_id", this.userId);
        payRequestData.put("is_find_account", this.isFindAccount);
        payRequestData.put("is_select_agreement", this.isSelectAgreement);
        return payRequestData;
    }
}
