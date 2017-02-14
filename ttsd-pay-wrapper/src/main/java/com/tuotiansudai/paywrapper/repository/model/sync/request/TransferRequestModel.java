package com.tuotiansudai.paywrapper.repository.model.sync.request;

import cn.jpush.api.utils.StringUtils;
import com.google.common.base.Strings;
import com.tuotiansudai.paywrapper.repository.model.UmPayParticAccType;
import com.tuotiansudai.paywrapper.repository.model.UmPayService;
import com.tuotiansudai.paywrapper.repository.model.UmPayTransAction;
import com.tuotiansudai.paywrapper.repository.model.async.request.BaseAsyncRequestModel;

import java.text.MessageFormat;
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

    private String particAccountId;

    private String amount;

    public TransferRequestModel() {
    }

    public static TransferRequestModel newRequest(String orderId, String payUserId, String amount) {
        TransferRequestModel model = new TransferRequestModel();
        model.service = UmPayService.TRANSFER.getServiceName();
        model.orderId = orderId;
        model.particUserId = payUserId;
        model.amount = amount;
        model.particAccType = UmPayParticAccType.INDIVIDUAL.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return model;
    }

    public static TransferRequestModel newTransferReferrerRewardRequest(String orderId, String payUserId, String amount) {
        TransferRequestModel model = newRequest(orderId, payUserId, amount);
        model.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "transfer_referrer_reward_notify");
        return model;
    }

    public static TransferRequestModel newTransferCouponRequest(String orderId, String payUserId, String amount) {
        TransferRequestModel model = newRequest(orderId, payUserId, amount);
        model.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "transfer_coupon_notify");
        return model;
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
        if(StringUtils.isNotEmpty(this.notifyUrl)){
            payRequestData.put("notify_url", this.notifyUrl);
        }
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

    public String getParticAccountId() {
        return particAccountId;
    }

    public void setParticAccountId(String particAccountId) {
        this.particAccountId = particAccountId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
