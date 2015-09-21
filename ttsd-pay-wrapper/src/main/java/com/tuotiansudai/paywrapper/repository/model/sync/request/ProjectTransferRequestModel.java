package com.tuotiansudai.paywrapper.repository.model.sync.request;

import com.tuotiansudai.paywrapper.repository.model.*;
import com.tuotiansudai.paywrapper.repository.model.async.request.BaseAsyncModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProjectTransferRequestModel extends BaseAsyncModel {
    private String projectId;
    private String orderId;
    private String userId;
    private String amount;
    private String merDate;
    private String servType;
    private String transAction;
    private String particType;
    private String particAccType;

    public ProjectTransferRequestModel() {

    }

    public static ProjectTransferRequestModel newInvestRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount);
        model.retUrl = "/";
        model.notifyUrl = "http://121.43.71.173:13002/paywrapper/callback/invest_notify";
        model.servType = UmPayServType.TRANSFER_IN_INVEST.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newLoanOutRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount);
        model.retUrl = "/";
        model.notifyUrl = "/";
        model.servType = UmPayServType.TRANSFER_OUT_LOAN_OUT.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.LOANER.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newRepayRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount);
        model.retUrl = "/";
        model.notifyUrl = "/callback/repay_notify";
        model.servType = UmPayServType.TRANSFER_IN_REPAY.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.LOANER.getCode();
        return model;
    }

    private ProjectTransferRequestModel(String projectId, String orderId, String userId, String amount) {
        super();
        this.service = UmPayService.PROJECT_TRANSFER.getServiceName();
        this.orderId = orderId;
        this.projectId = projectId;
        this.userId = userId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particAccType = UmPayParticAccType.PERSON.getCode();
    }

    public Map<String, String> generatePayRequestData() {
        Map<String, String> payRequestData = super.generatePayRequestData();
        payRequestData.put("ret_url", retUrl);
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
