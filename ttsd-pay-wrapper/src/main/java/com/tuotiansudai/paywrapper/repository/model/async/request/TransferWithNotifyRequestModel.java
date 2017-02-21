package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.*;
import com.tuotiansudai.repository.model.Source;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TransferWithNotifyRequestModel extends BaseAsyncRequestModel {

    private String orderId;
    private String particUserId;
    private String amount;
    private String merDate;
    private String servType;
    private String transAction;
    private String particType;
    private String particAccType;
    private String merAccountId;
    private String particAccountId;

    public TransferWithNotifyRequestModel() {

    }

    public static TransferWithNotifyRequestModel newCouponRepayRequest(String orderId, String payUserId, String particAccountId, String amount) {

        TransferWithNotifyRequestModel model = new TransferWithNotifyRequestModel(orderId, payUserId, particAccountId, amount);
        model.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "coupon_repay_notify");
        model.servType = UmPayServType.TRANSFER_OUT_REPAY_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    public static TransferWithNotifyRequestModel newExtraRateRequest(String orderId, String payUserId, String particAccountId, String amount) {
        TransferWithNotifyRequestModel model = new TransferWithNotifyRequestModel(orderId, payUserId, particAccountId, amount);
        model.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "extra_rate_notify");
        model.servType = UmPayServType.TRANSFER_OUT_REPAY_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    public static TransferWithNotifyRequestModel experienceInterestRequest(String orderId, String payUserId, String particAccountId, String amount) {
        TransferWithNotifyRequestModel model = new TransferWithNotifyRequestModel(orderId, payUserId, particAccountId,amount);
        model.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "experience_repay_notify");
        model.servType = UmPayServType.TRANSFER_OUT_REPAY_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    private TransferWithNotifyRequestModel(String orderId, String payUserId, String particAccountId, String amount) {
        super();
        this.service = UmPayService.TRANSFER.getServiceName();
        this.orderId = orderId;
        this.particUserId = payUserId;
        this.particAccountId = particAccountId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particAccType = UmPayParticAccType.INDIVIDUAL.getCode();
    }

    private TransferWithNotifyRequestModel(String orderId, String payUserId, String amount, Source source) {
        super(source, "transfer");
        this.service = UmPayService.TRANSFER.getServiceName();
        this.orderId = orderId;
        this.particUserId = payUserId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particAccType = UmPayParticAccType.INDIVIDUAL.getCode();
    }


    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("notify_url", notifyUrl);
        payRequestData.put("order_id", orderId);
        payRequestData.put("mer_date", merDate);
        payRequestData.put("serv_type", servType);
        payRequestData.put("trans_action", transAction);
        payRequestData.put("partic_type", particType);
        payRequestData.put("partic_acc_type", particAccType);
        payRequestData.put("partic_user_id", particUserId);
        payRequestData.put("amount", amount);
        return payRequestData;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getParticUserId() {
        return particUserId;
    }

    public String getAmount() {
        return amount;
    }

    public String getMerAccountId() {
        return merAccountId;
    }

    public String getParticAccountId() {
        return particAccountId;
    }
}
