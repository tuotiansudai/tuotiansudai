package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.repository.model.Source;

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
        return new TransferAsynRequestModel(orderId, payUserId, payAccountId, amount, source, AsyncUmPayService.MEMBERSHIP_PRIVILEGE_PURCHASE_TRANSFER_ASYN);
    }

    public static TransferAsynRequestModel createSystemRechargeRequestModel(String orderId, String payUserId, String payAccountId, String amount) {
        TransferAsynRequestModel transferAsynRequestModel = new TransferAsynRequestModel(orderId, payUserId, payAccountId, amount, Source.WEB, AsyncUmPayService.SYSTEM_RECHARGE_TRANSFER_ASYN);
        transferAsynRequestModel.setRetUrl(MessageFormat.format("{0}/{1}", PAY_CALLBACK_CONSOLE_HOST, AsyncUmPayService.SYSTEM_RECHARGE_TRANSFER_ASYN.getWebRetCallbackPath()));
        return transferAsynRequestModel;
    }

    private TransferAsynRequestModel(String orderId, String payUserId, String payAccountId, String amount, Source source, AsyncUmPayService asyncUmPayService) {
        super(source, asyncUmPayService);
        this.service = asyncUmPayService.getServiceName();
        this.orderId = orderId;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particUserId = payUserId;
        this.particAccountId = payAccountId;
        this.amount = amount;
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
