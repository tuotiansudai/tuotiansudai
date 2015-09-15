package com.tuotiansudai.paywrapper.repository.model.async.callback;

/**
 * Created by Administrator on 2015/9/15.
 */
public class AgreementNotifyRequestModel extends BaseCallbackRequestModel {

    private String userId;

    private String userBindAgreementList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserBindAgreementList() {
        return userBindAgreementList;
    }

    public void setUserBindAgreementList(String userBindAgreementList) {
        this.userBindAgreementList = userBindAgreementList;
    }
}
