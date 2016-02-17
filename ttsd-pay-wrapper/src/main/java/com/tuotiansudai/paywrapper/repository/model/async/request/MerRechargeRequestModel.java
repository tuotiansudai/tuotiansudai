package com.tuotiansudai.paywrapper.repository.model.async.request;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MerRechargeRequestModel extends BaseAsyncRequestModel {

    private static final String NORMAL_PAY = "B2CDEBITBANK";

    private String orderId;

    private String merDate;

    private String payType;

    private String rechargeMerId;

    private String accountType;

    private String amount;

    private String gateId;

    public MerRechargeRequestModel() {

    }

    public static MerRechargeRequestModel newRecharge(String orderId, String amount, String gateId) {
        MerRechargeRequestModel model = new MerRechargeRequestModel();
        model.setService("mer_recharge");
        model.setOrderId(orderId);
        model.setRechargeMerId(UMP_PROPS.getProperty("mer_id"));
        model.setAccountType("01"); // 01 现金账户
        model.setAmount(amount);
        model.setGateId(gateId);
        model.setPayType(NORMAL_PAY);
        model.setMerDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setRetUrl(MessageFormat.format("{0}/account", CALLBACK_HOST_PROPS.get("pay.callback.web.host")));
        model.setNotifyUrl(MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "recharge_notify"));
        return model;
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", this.retUrl);
        payRequestData.put("notify_url", this.notifyUrl);
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("pay_type", this.payType);
        payRequestData.put("recharge_mer_id", this.rechargeMerId);
        payRequestData.put("account_type", this.accountType);
        payRequestData.put("amount", this.amount);
        payRequestData.put("gate_id", this.gateId);
        return payRequestData;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setMerDate(String merDate) {
        this.merDate = merDate;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public void setRechargeMerId(String rechargeMerId) {
        this.rechargeMerId = rechargeMerId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }
}
