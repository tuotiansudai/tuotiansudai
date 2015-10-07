package com.tuotiansudai.paywrapper.repository.model.async.request;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MerRechargePersonRequestModel extends BaseAsyncRequestModel {

    private String orderId;

    private String merDate;

    private String payType = "B2CDEBITBANK";

    private String userId;

    private String amount;

    private String gateId;

    public MerRechargePersonRequestModel() {
    }

    public MerRechargePersonRequestModel(String orderId, String userId, String amount, String gateId) {
        super();
        this.service = "mer_recharge_person";
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.gateId = gateId;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.retUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.web.host"), this.service);
        this.notifyUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.back.host"), this.service);
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", this.retUrl);
        payRequestData.put("notify_url", this.notifyUrl);
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("pay_type", this.payType);
        payRequestData.put("user_id", this.userId);
        payRequestData.put("amount", this.amount);
        payRequestData.put("gate_id", this.gateId);
        return payRequestData;
    }
}
