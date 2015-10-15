package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.repository.model.AgreementType;

import java.util.Map;

/**
 * Created by Administrator on 2015/9/15.
 */
public class PtpMerBindAgreementRequestModel extends BaseAsyncRequestModel {

    private String userId;

    private AgreementType userBindAgreementList;

    public PtpMerBindAgreementRequestModel() {
    }

    public PtpMerBindAgreementRequestModel(String userId, AgreementType userBindAgreementList) {
        super();
        this.service = "ptp_mer_bind_agreement";
        this.userId = userId;
        this.setNotifyUrl("http://121.43.71.173:13003/trusteeship_return_s2s/ptp_mer_bind_agreement");
        this.setRetUrl("http://121.43.71.173:13003/trusteeship_return_web/ptp_mer_bind_agreement");
        this.userBindAgreementList = userBindAgreementList;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", "/");
        payRequestData.put("notify_url", this.getNotifyUrl());
        payRequestData.put("user_id", this.userId);
        payRequestData.put("user_bind_agreement_list", this.userBindAgreementList.name());
        return payRequestData;
    }

}
