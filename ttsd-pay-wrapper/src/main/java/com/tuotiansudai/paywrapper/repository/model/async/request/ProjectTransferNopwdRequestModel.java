package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.paywrapper.repository.model.*;
import com.tuotiansudai.repository.model.Source;

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

    private ProjectTransferNopwdRequestModel(String projectId, String orderId, String userId, String amount, Source source, UmPayServType umPayServType, UmPayParticType umPayParticType, AsyncUmPayService asyncUmPayService) {
        super(source == null ? Source.WEB : source, asyncUmPayService);
        this.service = asyncUmPayService.getServiceName();
        this.servType = umPayServType.getCode();
        this.transAction = UmPayTransAction.IN.getCode();
        this.orderId = orderId;
        this.projectId = projectId;
        this.userId = userId;
        this.amount = amount;
        this.merDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.particAccType = UmPayParticAccType.INDIVIDUAL.getCode();
        this.particType = umPayParticType.getCode();
    }

    public static ProjectTransferNopwdRequestModel newInvestNopwdRequest(String projectId, String orderId, String userId, String amount) {
        return new ProjectTransferNopwdRequestModel(projectId, orderId, userId, amount, Source.WEB, UmPayServType.TRANSFER_IN_INVEST, UmPayParticType.INVESTOR, AsyncUmPayService.INVEST_PROJECT_TRANSFER_NOPWD);
    }

    public static ProjectTransferNopwdRequestModel newPurchaseNopwdRequest(String projectId, String orderId, String userId, String amount) {
        return new ProjectTransferNopwdRequestModel(projectId, orderId, userId, amount, Source.WEB, UmPayServType.TRANSFER_IN_TRANSFER, UmPayParticType.INVESTOR, AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD);
    }

    public static ProjectTransferNopwdRequestModel newRepayNopwdRequest(String projectId, String orderId, String userId, String amount) {
        return new ProjectTransferNopwdRequestModel(projectId, orderId, userId, amount, Source.WEB, UmPayServType.TRANSFER_IN_REPAY, UmPayParticType.LOANER, AsyncUmPayService.NORMAL_REPAY_PROJECT_TRANSFER_NOPWD);
    }

    public static ProjectTransferNopwdRequestModel newCreditLoanRechargeNopwdRequest(String orderId, String userId, String amount) {
        return new ProjectTransferNopwdRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, Source.WEB, UmPayServType.TRANSFER_IN_TRANSFER, UmPayParticType.INVESTOR, AsyncUmPayService.CREDIT_LOAN_RECHARGE_TRANSFER_NOPWD);
    }

    public static ProjectTransferNopwdRequestModel newCreditLoanNoPasswordRepayRequest(String orderId, String userId, String amount) {
        return new ProjectTransferNopwdRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, Source.HUI_ZU, UmPayServType.TRANSFER_IN_TRANSFER, UmPayParticType.INVESTOR, AsyncUmPayService.CREDIT_LOAN_REPAY_PROJECT_TRANSFER_NOPWD);
    }

    public static ProjectTransferNopwdRequestModel newHuiZuRepayNopwdRequest(String orderId, String userId, String amount) {
        return new ProjectTransferNopwdRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, Source.HUI_ZU, UmPayServType.TRANSFER_IN_TRANSFER, UmPayParticType.INVESTOR, AsyncUmPayService.HUI_ZU_NO_PASSWORD_REPAY_PROJECT_TRANSFER);
    }

    public static ProjectTransferNopwdRequestModel newCreditLoanActivateAccountNopwdRequest(String orderId, String userId, String amount) {
        return new ProjectTransferNopwdRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, Source.HUI_ZU, UmPayServType.TRANSFER_IN_TRANSFER, UmPayParticType.INVESTOR, AsyncUmPayService.CREDIT_LOAN_ACTIVATE_ACCOUNT_PROJECT_TRANSFER_NOPWD);
    }

    public static ProjectTransferNopwdRequestModel newLuxuryStageRepayNopwdRequest(String orderId, String userId, String amount) {
        return new ProjectTransferNopwdRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, Source.HUI_ZU, UmPayServType.TRANSFER_IN_TRANSFER, UmPayParticType.INVESTOR, AsyncUmPayService.LUXURY_STAGE_REPAY_PROJECT_TRANSFER_NOPWD);
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