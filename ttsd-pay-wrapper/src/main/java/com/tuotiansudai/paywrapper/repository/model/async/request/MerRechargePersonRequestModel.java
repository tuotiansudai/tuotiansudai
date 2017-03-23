package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.repository.model.Source;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MerRechargePersonRequestModel extends BaseAsyncRequestModel {

    private static final String NORMAL_PAY = "B2CDEBITBANK";

    private static final String FAST_PAY = "DEBITCARD";

    private String orderId;

    private String merDate;

    private String payType;

    private String userId;

    private String amount;

    private String gateId;

    private String comAmtType;

    public MerRechargePersonRequestModel() {

    }

    public MerRechargePersonRequestModel(Source source) {
        super(source, AsyncUmPayService.MER_RECHARGE_PERSON);
    }

    public static MerRechargePersonRequestModel newRecharge(String orderId, String userId, String amount, String gateId) {
        MerRechargePersonRequestModel model = new MerRechargePersonRequestModel();
        model.setService(AsyncUmPayService.MER_RECHARGE_PERSON.getServiceName());
        model.setOrderId(orderId);
        model.setUserId(userId);
        model.setAmount(amount);
        model.setGateId(gateId);
        model.setComAmtType("2"); //1 前向手续费：交易方承担 2 前向手续费：平台商户（手续费账户）承担
        model.setPayType(NORMAL_PAY);
        model.setMerDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setRetUrl(MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("pay.callback.web.host"), AsyncUmPayService.MER_RECHARGE_PERSON.getMobileRetCallbackPath()));
        model.setNotifyUrl(MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), AsyncUmPayService.MER_RECHARGE_PERSON.getNotifyCallbackPath()));
        return model;
    }

    public static MerRechargePersonRequestModel newFastRecharge(String orderId, String userId, String amount, Source source) {
        MerRechargePersonRequestModel model = new MerRechargePersonRequestModel(source);
        model.setService(AsyncUmPayService.MER_RECHARGE_PERSON.getServiceName());
        model.setOrderId(orderId);
        model.setUserId(userId);
        model.setComAmtType("2"); //1 前向手续费：交易方承担 2 前向手续费：平台商户（手续费账户）承担
        model.setAmount(amount);
        model.setPayType(FAST_PAY);
        model.setMerDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        model.setNotifyUrl(MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), AsyncUmPayService.MER_RECHARGE_PERSON.getNotifyCallbackPath()));
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
        payRequestData.put("user_id", this.userId);
        payRequestData.put("amount", this.amount);
        payRequestData.put("com_amt_type", this.comAmtType);
        if (NORMAL_PAY.equals(this.payType)) {
            payRequestData.put("gate_id", this.gateId);
        }

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public void setComAmtType(String comAmtType) {
        this.comAmtType = comAmtType;
    }
}
