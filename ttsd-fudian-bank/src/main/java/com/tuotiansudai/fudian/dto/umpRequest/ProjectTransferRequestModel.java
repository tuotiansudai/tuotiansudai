package com.tuotiansudai.fudian.dto.umpRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ProjectTransferRequestModel extends BaseAsyncRequestModel {
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

    public static ProjectTransferRequestModel newRepayRequest(String projectId, String orderId, String userId, String amount, boolean isAdvanceRepay) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL,
                isAdvanceRepay ? AsyncUmPayService.ADVANCE_REPAY_PROJECT_TRANSFER : AsyncUmPayService.NORMAL_REPAY_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_REPAY.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.LOANER.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newNormalRepayPaybackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, AsyncUmPayService.NORMAL_REPAY_PAYBACK_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_REPAY_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newNormalRepayInvestFeeRequest(String projectId, String orderId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, UMP_PROPS.getProperty("mer_id"), amount, UmPayParticAccType.MERCHANT, AsyncUmPayService.NORMAL_REPAY_INVEST_FEE_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_PLATFORM_FEE.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newAdvanceRepayPaybackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, AsyncUmPayService.ADVANCE_REPAY_PAYBACK_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_REPAY_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newAdvanceRepayInvestFeeRequest(String projectId, String orderId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, UMP_PROPS.getProperty("mer_id"), amount, UmPayParticAccType.MERCHANT, AsyncUmPayService.ADVANCE_REPAY_INVEST_FEE_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_PLATFORM_FEE.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    private ProjectTransferRequestModel(String projectId, String orderId, String userId, String amount, UmPayParticAccType umPayParticAccType, AsyncUmPayService asyncUmPayService) {
        super(asyncUmPayService);
        this.service = asyncUmPayService.getServiceName();
        this.orderId = orderId;
        this.projectId = projectId;
        this.userId = userId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particAccType = umPayParticAccType.getCode();
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

    public String getProjectId() {
        return projectId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAmount() {
        return amount;
    }
}
