package com.tuotiansudai.fudian.ump.asyn.request;


import com.tuotiansudai.fudian.ump.AsyncUmPayService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PtpMerReplaceCardRequestModel extends BaseAsyncRequestModel {

    private String orderId;

    private String merDate;

    private String userId;

    private String cardId;

    private String accountName;

    private String identityType = "IDENTITY_CARD";

    private String identityCode;

    public PtpMerReplaceCardRequestModel() {

    }

    public PtpMerReplaceCardRequestModel(String orderId, String cardNumber, String payUserId, String userName, String identityNumber) {
        super(AsyncUmPayService.PTP_MER_REPLACE_CARD);
        this.service = AsyncUmPayService.PTP_MER_REPLACE_CARD.getServiceName();
        this.orderId = orderId;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.cardId = cardNumber;
        this.userId = payUserId;
        this.accountName = userName;
        this.identityCode = identityNumber;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", this.getRetUrl());
        payRequestData.put("notify_url", this.getNotifyUrl());
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("user_id", this.userId);
        payRequestData.put("card_id", this.cardId);
        payRequestData.put("account_name", this.accountName);
        payRequestData.put("identity_type", this.identityType);
        payRequestData.put("identity_code", this.identityCode);
        return payRequestData;
    }
}
