package com.tuotiansudai.fudian.ump.asyn.request;


import com.tuotiansudai.fudian.ump.AsyncUmPayService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class RechargeRequestModel extends BaseAsyncRequestModel {

    private static final String NORMAL_PAY = "B2CDEBITBANK";

    private static final String FAST_PAY = "DEBITCARD";

    private String orderId;

    private String merDate;

    private String payType;

    private String userId;

    private String amount;

    private String gateId;

    private String comAmtType;

    public RechargeRequestModel() {

    }

    private RechargeRequestModel(String orderId, String userId, String amount) {
        super(AsyncUmPayService.MER_RECHARGE_PERSON);
        this.service = AsyncUmPayService.MER_RECHARGE_PERSON.getServiceName();
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.comAmtType = "2"; //1 前向手续费：交易方承担 2 前向手续费：平台商户（手续费账户）承担
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());

    }

    public static RechargeRequestModel newRecharge(String orderId, String userId, String amount, String gateId) {
        RechargeRequestModel model = new RechargeRequestModel(orderId, userId, amount);
        model.setPayType(NORMAL_PAY);
        model.setGateId(gateId);
        return model;
    }

    public static RechargeRequestModel newFastRecharge(String orderId, String userId, String amount) {
        RechargeRequestModel model = new RechargeRequestModel(orderId, userId, amount);
        model.setPayType(FAST_PAY);
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
