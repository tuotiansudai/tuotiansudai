package com.tuotiansudai.paywrapper.repository.model.sync.request;

import com.tuotiansudai.paywrapper.repository.model.*;
import com.tuotiansudai.paywrapper.repository.model.async.request.BaseAsyncRequestModel;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProjectTransferNopwdRequestModel extends BaseAsyncRequestModel {
    private String orderId;
    private String merDate;
    private String projectId;
    private String servType;
    private String transAction;
    private String particType;
    private String particAccType;
    private String userId;
    private String amount;

    public ProjectTransferNopwdRequestModel() {

    }

    public static ProjectTransferNopwdRequestModel newInvestNopwdRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferNopwdRequestModel model = new ProjectTransferNopwdRequestModel(projectId, orderId, userId, amount, UmPayServType.TRANSFER_IN_INVEST, "invest_notify");
        return model;
    }

    public static ProjectTransferNopwdRequestModel newRepayNopwdRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferNopwdRequestModel model = new ProjectTransferNopwdRequestModel(projectId, orderId, userId, amount, UmPayServType.TRANSFER_IN_REPAY, "repay_notify");
        return model;
    }

    private ProjectTransferNopwdRequestModel(String projectId, String orderId, String userId, String amount, UmPayServType umPayServType, String url) {
        super();
        this.service = UmPayService.PROJECT_TRANSFER_NOPWD.getServiceName();
        this.servType = umPayServType.getCode();
        this.transAction = UmPayTransAction.IN.getCode();
        this.orderId = orderId;
        this.projectId = projectId;
        this.userId = userId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particAccType = UmPayParticAccType.INDIVIDUAL.getCode();
        this.particType = UmPayParticType.INVESTOR.getCode();
        this.notifyUrl = MessageFormat.format("{0}/{1}", CALLBACK_HOST_PROPS.get("pay.callback.back.host"), url);
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("notify_url", notifyUrl);
        payRequestData.put("order_id", orderId);
        payRequestData.put("project_id", projectId);
        payRequestData.put("mer_date", merDate);
        payRequestData.put("serv_type", servType);
        payRequestData.put("trans_action", transAction);
        payRequestData.put("partic_type", particType);
        payRequestData.put("partic_acc_type", particAccType);
        payRequestData.put("partic_user_id", userId);
        payRequestData.put("amount", amount);
        return payRequestData;
    }
}
