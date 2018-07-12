package com.tuotiansudai.fudian.ump.asyn.request;

import com.tuotiansudai.fudian.dto.umpRequest.AsyncUmPayService;
import com.tuotiansudai.fudian.dto.umpRequest.BaseAsyncRequestModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class BindCardRequestModel extends BaseAsyncRequestModel {

    private String orderId;

    private String merDate;

    private String userId;

    private String cardId;

    private String accountName;

    private String identityType = "IDENTITY_CARD";

    private String identityCode;

    private String isOpenFastPayment = "0";

    public BindCardRequestModel() {
    }

    public BindCardRequestModel(String orderId, String cardNumber, String payUserId, String userName, String identityNumber, boolean isOpenFastPayment) {
        super(AsyncUmPayService.PTP_MER_BIND_CARD);
        this.service = AsyncUmPayService.PTP_MER_BIND_CARD.getServiceName();
        this.orderId = orderId;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.cardId = cardNumber;
        this.userId = payUserId;
        this.accountName = userName;
        this.identityCode = identityNumber;
        this.isOpenFastPayment = isOpenFastPayment ? "1" : "0";
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
        payRequestData.put("is_open_fastPayment", this.isOpenFastPayment);
        return payRequestData;
    }
}
