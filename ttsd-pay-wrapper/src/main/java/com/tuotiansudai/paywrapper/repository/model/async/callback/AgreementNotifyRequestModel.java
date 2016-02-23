package com.tuotiansudai.paywrapper.repository.model.async.callback;

import com.tuotiansudai.repository.model.AgreementType;

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

    public boolean isAutoInvest(){
        return this.getUserBindAgreementList().contains(AgreementType.ZTBB0G00.name());
    }

    public boolean isFastPay(){
        return this.getUserBindAgreementList().contains(AgreementType.ZKJP0700.name());
    }

    public boolean isAutoRepay() {
        return this.getUserBindAgreementList().contains(AgreementType.ZHKB0H01.name());
    }
    
}
