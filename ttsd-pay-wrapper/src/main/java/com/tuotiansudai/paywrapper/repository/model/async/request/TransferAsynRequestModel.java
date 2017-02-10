package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.MobileFrontCallbackService;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class TransferAsynRequestModel extends BaseAsyncRequestModel {


    private String orderId;

    private String merAccountId;

    private String particUserId;

    private String particAccountId;

    private String particAccType = "01";

    private String merDate;

    private String amount;

    public TransferAsynRequestModel() {

    }

    public static TransferAsynRequestModel createMembershipPrivilegePurchaseRequestModel(String orderId, String payUserId, String payAccountId, String amount, Source source) {
        String retUrl = MessageFormat.format("{0}/callback/{1}", getCallbackMobileHost(), MobileFrontCallbackService.MEMBERSHIP_PRIVILEGE_PURCHASE.getServiceName());
        String notifyUrl = MessageFormat.format("{0}/{1}", getCallbackBackHost(), "membership-privilege-purchase-notify");
        TransferAsynRequestModel asynRequestModel = new TransferAsynRequestModel(orderId, payUserId,payAccountId,amount, retUrl, notifyUrl);
        if (Lists.newArrayList(Source.ANDROID, Source.IOS).contains(source)) {
            asynRequestModel.setSourceV("HTML5");
        }
        return asynRequestModel;
    }

    public static TransferAsynRequestModel createSystemRechargeRequestModel(String orderId, String payUserId, String payAccountId,String amount) {
        String retUrl = MessageFormat.format("{0}/finance-manage/system-bill", getCallbackConsoleHost());
        String notifyUrl = MessageFormat.format("{0}/{1}", getCallbackBackHost(), "system_recharge_notify");
        return new TransferAsynRequestModel(orderId, payUserId,payAccountId, amount, retUrl, notifyUrl);
    }

    private TransferAsynRequestModel(String orderId, String payUserId,String payAccountId, String amount, String retUrl, String notifyUrl) {
        this.service = "transfer_asyn";
        this.orderId = orderId;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particUserId = payUserId;
        this.particAccountId = payAccountId;
        this.amount = amount;
        this.retUrl = retUrl;
        this.notifyUrl = notifyUrl;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", this.retUrl);
        payRequestData.put("notify_url", this.notifyUrl);
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("partic_user_id", this.particUserId);
        payRequestData.put("partic_acc_type", this.particAccType);
        payRequestData.put("amount", this.amount);
        return payRequestData;
    }
}
