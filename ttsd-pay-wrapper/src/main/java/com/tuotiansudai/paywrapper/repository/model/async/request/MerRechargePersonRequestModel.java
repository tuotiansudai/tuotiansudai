package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MerRechargePersonRequestModel extends BaseSyncRequestModel {

    private String orderId;

    private String merDate;

    private String payType = "B2CDEBITBANK";

    private String umpUserId;

    private String amount;

    public MerRechargePersonRequestModel() {

    }

    public MerRechargePersonRequestModel(String orderId, String umpUserId, String amount) {
        super();
        this.service = "mer_recharge_person";
        this.orderId = orderId;
        this.umpUserId = umpUserId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("pay_type", this.payType);
        payRequestData.put("user_id", this.umpUserId);
        payRequestData.put("amount", this.amount);
        return payRequestData;
    }



}
