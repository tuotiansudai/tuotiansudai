package com.tuotiansudai.fudian.ump.asyn.request;


import com.tuotiansudai.fudian.ump.AsyncUmPayService;
import com.tuotiansudai.fudian.ump.UmPayParticAccType;
import com.tuotiansudai.fudian.ump.UmPayTransAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TransferRequestModel extends BaseAsyncRequestModel {

    private String orderId;

    private String merDate;

    private String merAccountId;

    private String particAccType = UmPayParticAccType.INDIVIDUAL.getCode();

    private String transAction = UmPayTransAction.OUT.getCode();

    private String particUserId;

    private String amount;

    public TransferRequestModel() {
    }

    private TransferRequestModel(String orderId, String payUserId, String amount, AsyncUmPayService asyncUmPayService) {
        super(asyncUmPayService);
        this.service = asyncUmPayService.getServiceName();
        this.orderId = orderId;
        this.particUserId = payUserId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particAccType = UmPayParticAccType.INDIVIDUAL.getCode();
        this.transAction = UmPayTransAction.OUT.getCode();
    }

    public static TransferRequestModel newCouponRepayRequest(String orderId, String payUserId, String amount) {
        return new TransferRequestModel(orderId, payUserId, amount, AsyncUmPayService.COUPON_REPAY_TRANSFER);
    }

    public static TransferRequestModel newExtraRateRequest(String orderId, String payUserId, String amount) {
        return new TransferRequestModel(orderId, payUserId, amount, AsyncUmPayService.EXTRA_RATE_TRANSFER);
    }

    public static TransferRequestModel experienceInterestRequest(String orderId, String payUserId, String amount) {
        return new TransferRequestModel(orderId, payUserId, amount, AsyncUmPayService.EXPERIENCE_INTEREST_TRANSFER);
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("partic_acc_type", this.particAccType);
        payRequestData.put("trans_action", this.transAction);
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("partic_user_id", this.particUserId);
        payRequestData.put("amount", this.amount);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("notify_url", this.notifyUrl);
        return payRequestData;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getParticAccType() {
        return particAccType;
    }

    public void setParticAccType(String particAccType) {
        this.particAccType = particAccType;
    }

    public String getTransAction() {
        return transAction;
    }

    public void setTransAction(String transAction) {
        this.transAction = transAction;
    }

    public String getMerDate() {
        return merDate;
    }

    public void setMerDate(String merDate) {
        this.merDate = merDate;
    }

    public String getMerAccountId() {
        return merAccountId;
    }

    public void setMerAccountId(String merAccountId) {
        this.merAccountId = merAccountId;
    }

    public String getParticUserId() {
        return particUserId;
    }

    public void setParticUserId(String particUserId) {
        this.particUserId = particUserId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
