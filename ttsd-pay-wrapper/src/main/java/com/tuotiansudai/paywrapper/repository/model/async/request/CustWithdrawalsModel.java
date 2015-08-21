package com.tuotiansudai.paywrapper.repository.model.async.request;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustWithdrawalsModel extends BaseAsyncModel {

    private String applyNotifyFlag;

    private String orderId;

    private String merDate;

    private String userId;

    private String amount;

    public CustWithdrawalsModel() {
    }

    public CustWithdrawalsModel(String orderId, String userId, String amount) {
        super();
        this.service = "cust_withdrawals";
        this.applyNotifyFlag = "1";
        this.retUrl = "/";
        this.notifyUrl = "http://121.43.71.173:13002/trusteeship_return_s2s/mer_recharge_person";
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
}
