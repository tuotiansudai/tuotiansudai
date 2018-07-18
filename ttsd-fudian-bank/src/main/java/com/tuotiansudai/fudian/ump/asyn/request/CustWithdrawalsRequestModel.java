package com.tuotiansudai.fudian.ump.asyn.request;


import com.tuotiansudai.fudian.ump.AsyncUmPayService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CustWithdrawalsRequestModel extends BaseAsyncRequestModel {

    private String applyNotifyFlag;

    private String orderId;

    private String merDate;

    private String userId;

    private String amount;

    private String comAmtType;

    public CustWithdrawalsRequestModel() {
    }

    public CustWithdrawalsRequestModel(String orderId, String userId, String amount) {
        super(AsyncUmPayService.CUST_WITHDRAWALS);
        this.service = AsyncUmPayService.CUST_WITHDRAWALS.getServiceName();
        this.applyNotifyFlag = "1";
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.comAmtType = "1"; //1 前向手续费：交易方承担 2 前向手续费：平台商户（手续费账户）承担
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    @Override
    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", this.retUrl);
        payRequestData.put("notify_url", this.notifyUrl);
        payRequestData.put("apply_notify_flag", this.applyNotifyFlag);
        payRequestData.put("order_id", this.orderId);
        payRequestData.put("mer_date", this.merDate);
        payRequestData.put("user_id", this.userId);
        payRequestData.put("amount", this.amount);
        payRequestData.put("com_amt_type", this.comAmtType);
        return payRequestData;
    }
}
