package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.UmPayParticAccType;
import com.tuotiansudai.paywrapper.repository.model.UmPayParticType;
import com.tuotiansudai.paywrapper.repository.model.UmPayServType;
import com.tuotiansudai.paywrapper.repository.model.UmPayTransAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProjectTransferModel extends BaseAsyncModel {
    private String loanId;
    private String orderId;
    private String userId;
    private String amount;

    public ProjectTransferModel() {

    }

    public ProjectTransferModel(String loanId, String orderId, String userId, String amount) {
        super();
        this.service = "project_transfer_invest";
        this.retUrl = "/";
        this.notifyUrl = "http://121.43.71.173:13002/trusteeship_return_s2s/invest_notify";
        this.orderId = orderId;
        this.loanId = loanId;
        this.userId = userId;
        this.amount = amount;
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", retUrl);
        payRequestData.put("notify_url", notifyUrl);
        payRequestData.put("order_id", orderId);
        payRequestData.put("project_id", String.valueOf(loanId));
        payRequestData.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
        payRequestData.put("serv_type", UmPayServType.TRANSFER_IN_INVEST.getCode());
        payRequestData.put("trans_action", UmPayTransAction.IN.getCode());
        payRequestData.put("partic_type", UmPayParticType.INVESTOR.getCode());
        payRequestData.put("partic_acc_type", UmPayParticAccType.PERSON.getCode());
        payRequestData.put("partic_user_id", userId);
        payRequestData.put("amount",String.valueOf(amount));
        return payRequestData;
    }
}
