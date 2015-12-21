package com.tuotiansudai.paywrapper.repository.model.async.callback;

import com.tuotiansudai.repository.model.AgreementType;

public class BankCardNotifyRequestModel extends BaseCallbackRequestModel {
    private String userId;

    private String lastFourCardid;

    private String userBindAgreementList;

    private String gateId;

    private static String SUCCESS_CODE = "0000";

    public boolean isOpenPay() {
        return userBindAgreementList != null && userBindAgreementList.contains(AgreementType.ZKJP0700.name()) && userBindAgreementList.lastIndexOf(SUCCESS_CODE) != -1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastFourCardid() {
        return lastFourCardid;
    }

    public void setLastFourCardid(String lastFourCardid) {
        this.lastFourCardid = lastFourCardid;
    }

    public String getUserBindAgreementList() {
        return userBindAgreementList;
    }

    public void setUserBindAgreementList(String userBindAgreementList) {
        this.userBindAgreementList = userBindAgreementList;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }
}