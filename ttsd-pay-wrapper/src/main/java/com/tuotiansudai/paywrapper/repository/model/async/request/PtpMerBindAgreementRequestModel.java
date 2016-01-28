package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.repository.model.AgreementType;
import com.tuotiansudai.repository.model.Source;

import java.text.MessageFormat;
import java.util.Map;

public class PtpMerBindAgreementRequestModel extends BaseAsyncRequestModel {

    private String userId;

    private AgreementType userBindAgreementList;

    public PtpMerBindAgreementRequestModel() {
    }

    public PtpMerBindAgreementRequestModel(String userId, AgreementType userBindAgreementList, Source source) {
        super(source, "ptp_mer_bind_agreement");
        this.service = "ptp_mer_bind_agreement";
        this.userId = userId;
        this.setNotifyUrl(MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "mer_bind_agreement_notify"));
        this.userBindAgreementList = userBindAgreementList;
        if ("HTML5".equals(this.getSourceV()) && this.userBindAgreementList == AgreementType.ZTBB0G00) {
            this.setRetUrl(MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("pay.callback.app.web.host"), "ptp_mer_no_password_invest"));
        }
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", this.getRetUrl());
        payRequestData.put("notify_url", this.getNotifyUrl());
        payRequestData.put("user_id", this.userId);
        payRequestData.put("user_bind_agreement_list", this.userBindAgreementList.name());
        return payRequestData;
    }

}
