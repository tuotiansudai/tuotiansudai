package com.tuotiansudai.fudian.dto.umpRequest;

import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.repository.model.Source;

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

    private String comAmtType;

    public MerRechargeRequestModel() {
    }

    public MerRechargeRequestModel(String orderId, String amount, String gateId) {
        super(Source.WEB, AsyncUmPayService.MER_RECHARGE);
        this.service = AsyncUmPayService.MER_RECHARGE.getServiceName();
        this.orderId = orderId;
        this.rechargeMerId = UMP_PROPS.getProperty("mer_id");
        this.accountType = "01"; // 01 现金账户
        this.amount = amount;
        this.gateId = gateId;
        this.comAmtType = "2"; //1 前向手续费：交易方承担 2 前向手续费：平台商户（手续费账户）承担
        this.payType = NORMAL_PAY;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
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
        payRequestData.put("com_amt_type", this.comAmtType);
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

    public void setComAmtType(String comAmtType) {
        this.comAmtType = comAmtType;
    }
}
