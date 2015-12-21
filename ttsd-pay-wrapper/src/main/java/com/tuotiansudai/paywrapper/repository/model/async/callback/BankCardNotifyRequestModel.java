package com.tuotiansudai.paywrapper.repository.model.async.callback;

import com.tuotiansudai.repository.model.AgreementType;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class BankCardNotifyRequestModel extends BaseCallbackRequestModel {

    private String userId;

    private String lastFourCardid;

    private String userBindAgreementList;

    private String gateId;

    public boolean isOpenPay() {
        try {
            return userBindAgreementList != null && userBindAgreementList.contains(AgreementType.ZKJP0700.name())
                    && URLDecoder.decode(userBindAgreementList, StandardCharsets.UTF_8.toString()).split(",")[1].equals(SUCCESS_CODE);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
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