package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.UmPayService;
import com.tuotiansudai.repository.model.Source;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PtpMerBindCardRequestModel extends BaseAsyncRequestModel {

    private String orderId;

    private String merDate;

    private String userId;

    private String cardId;

    private String accountName;

    private String identityType = "IDENTITY_CARD";

    private String identityCode;

    private String isOpenFastPayment = "0";

    public PtpMerBindCardRequestModel() {
    }

    public PtpMerBindCardRequestModel(String orderId, String cardNumber, String payUserId, String userName, String identityNumber, Source source) {
        this(orderId, cardNumber, payUserId, userName, identityNumber, source, false);
    }

    public PtpMerBindCardRequestModel(String orderId, String cardNumber, String payUserId, String userName, String identityNumber,Source source, boolean isOpenFastPayment) {
        super(source,"ptp_mer_bind_card");
        this.service = "ptp_mer_bind_card";
        this.orderId = orderId;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.cardId = cardNumber;
        this.userId = payUserId;
        this.accountName = userName;
        this.identityCode = identityNumber;
        this.isOpenFastPayment = isOpenFastPayment?"1":"0";
        this.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), UmPayService.NOTIFY_MER_BIND_CARD.getServiceName());
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
