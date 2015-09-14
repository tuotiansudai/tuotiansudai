package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProjectTransferModel extends BaseAsyncModel {
    private String projectId;
    private String orderId;
    private String userId;
    private String amount;
    private String merDate;

    public ProjectTransferModel() {

    }

    public ProjectTransferModel(String projectId, String orderId, String userId, String amount) {
        super();
        this.service = UmPayService.PROJECT_TRANSFER.getServiceName();
        this.orderId = orderId;
        this.projectId = projectId;
        this.userId = userId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", retUrl);
        payRequestData.put("notify_url", notifyUrl);
        payRequestData.put("order_id", orderId);
        payRequestData.put("project_id", projectId);
        payRequestData.put("mer_date", merDate);
        payRequestData.put("serv_type", UmPayServType.TRANSFER_IN_INVEST.getCode());
        payRequestData.put("trans_action", UmPayTransAction.IN.getCode());
        payRequestData.put("partic_type", UmPayParticType.INVESTOR.getCode());
        payRequestData.put("partic_acc_type", UmPayParticAccType.PERSON.getCode());
        payRequestData.put("partic_user_id", userId);
        payRequestData.put("amount", amount);
        return payRequestData;
    }
}
