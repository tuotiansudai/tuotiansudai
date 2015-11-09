package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.*;

import java.text.MessageFormat;
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

    public static ProjectTransferRequestModel newInvestRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL);
        model.retUrl = MessageFormat.format("{0}/account", CALLBACK_HOST_PROPS.get("ump.callback.web.host"));
        model.notifyUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.back.host"), "invest_notify");
        model.servType = UmPayServType.TRANSFER_IN_INVEST.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newLoanOutRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL);
        model.retUrl = "/";
        model.notifyUrl = "/";
        model.servType = UmPayServType.TRANSFER_OUT_LOAN_OUT.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.LOANER.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newRepayRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL);
        model.retUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.web.host"), "");
        model.notifyUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.back.host"), "repay_notify");
        model.servType = UmPayServType.TRANSFER_IN_REPAY.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.LOANER.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newAdvanceRepayRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL);
        model.retUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.web.host"), "");
        model.notifyUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.back.host"), "advance_repay_notify");
        model.servType = UmPayServType.TRANSFER_IN_REPAY.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.LOANER.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newRepayPaybackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL);
        model.retUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.web.host"), "");
        model.notifyUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.back.host"), "repay_payback_notify");
        model.servType = UmPayServType.TRANSFER_OUT_REPAY_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newRepayInvestFeeRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.MERCHANT);
        model.retUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.web.host"), "");
        model.notifyUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.back.host"), "repay_invest_fee_notify");
        model.servType = UmPayServType.TRANSFER_OUT_PLATFORM_FEE.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    /**
     * 撤资后返款(超投后返款)
     *
     * @param projectId
     * @param orderId
     * @param userId
     * @param amount
     * @return
     */
    public static ProjectTransferRequestModel overInvestPaybackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.MERCHANT);
        model.retUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.web.host"), "");
        model.notifyUrl = MessageFormat.format("{0}/callback/{1}", CALLBACK_HOST_PROPS.get("ump.callback.back.host"), "over_invest_payback_notify");
        model.servType = UmPayServType.TRANSFER_OVER_INVEST_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    private ProjectTransferRequestModel(String projectId, String orderId, String userId, String amount, UmPayParticAccType umPayParticAccType) {
        super();
        this.service = UmPayService.PROJECT_TRANSFER.getServiceName();
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
}
