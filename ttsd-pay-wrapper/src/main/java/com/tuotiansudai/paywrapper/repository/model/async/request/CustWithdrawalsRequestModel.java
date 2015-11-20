package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.repository.model.Source;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CustWithdrawalsRequestModel extends BaseAsyncRequestModel {

    private String applyNotifyFlag;

    private String orderId;

    private String merDate;

    private String userId;

    private String amount;

    public CustWithdrawalsRequestModel() {
    }

    public CustWithdrawalsRequestModel(String orderId, String userId, String amount,Source source) {
        super(source,"cust_withdrawals");
        this.service = "cust_withdrawals";
        this.applyNotifyFlag = "1";
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), "withdraw_notify");

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
        return payRequestData;
    }
}
