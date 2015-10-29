package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.UmPayService;
import com.tuotiansudai.repository.model.AgreementType;

import java.text.MessageFormat;
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
        this.retUrl =  (String)CALLBACK_HOST_PROPS.get("ump.callback.web.host");
        this.notifyUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.back.host"), UmPayService.NOTIFY_MER_BIND_AGREEMENT.getServiceName());
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
