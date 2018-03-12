package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.paywrapper.repository.model.UmPayParticAccType;
import com.tuotiansudai.paywrapper.repository.model.UmPayParticType;
import com.tuotiansudai.paywrapper.repository.model.UmPayServType;
import com.tuotiansudai.paywrapper.repository.model.UmPayTransAction;
import com.tuotiansudai.repository.model.Source;

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

    public static ProjectTransferRequestModel newLoanOutRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.LOAN_OUT_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_LOAN_OUT.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.LOANER.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newLoanCancelPayBackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.LOAN_CANCEL_PAYBACK_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_CANCEL_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newInvestRequest(String projectId, String orderId, String userId, String amount, Source source) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, source, AsyncUmPayService.INVEST_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_INVEST.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newOverInvestPaybackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.OVER_INVEST_PAYBACK_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OVER_INVEST_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newRepayRequest(String projectId, String orderId, String userId, String amount, boolean isAdvanceRepay) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB,
                isAdvanceRepay ? AsyncUmPayService.ADVANCE_REPAY_PROJECT_TRANSFER : AsyncUmPayService.NORMAL_REPAY_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_REPAY.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.LOANER.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newNormalRepayPaybackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.NORMAL_REPAY_PAYBACK_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_REPAY_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newNormalRepayInvestFeeRequest(String projectId, String orderId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, UMP_PROPS.getProperty("mer_id"), amount, UmPayParticAccType.MERCHANT, Source.WEB, AsyncUmPayService.NORMAL_REPAY_INVEST_FEE_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_PLATFORM_FEE.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newAdvanceRepayPaybackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.ADVANCE_REPAY_PAYBACK_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_REPAY_PAYBACK.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newAdvanceRepayInvestFeeRequest(String projectId, String orderId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, UMP_PROPS.getProperty("mer_id"), amount, UmPayParticAccType.MERCHANT, Source.WEB, AsyncUmPayService.ADVANCE_REPAY_INVEST_FEE_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_PLATFORM_FEE.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newInvestTransferRequest(String projectId, String orderId, String userId, String amount, Source source) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, source, AsyncUmPayService.INVEST_TRANSFER_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_TRANSFER.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newHuiZuRepayPasswordRequest(String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.MOBILE, AsyncUmPayService.HUI_ZU_PASSWORD_REPAY_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_TRANSFER.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newInvestTransferPaybackRequest(String projectId, String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.INVEST_TRANSFER_PAYBACK_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_TRANSFER.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newRepayTransferFeeRequest(String projectId, String orderId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(projectId, orderId, UMP_PROPS.getProperty("mer_id"), amount, UmPayParticAccType.MERCHANT, Source.WEB, AsyncUmPayService.REPAY_TRANSFER_FEE_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_PLATFORM_FEE.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.PLATFORM.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newCreditLoanActivateAccountRequest(String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.MOBILE, AsyncUmPayService.CREDIT_LOAN_ACTIVATE_ACCOUNT_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_TRANSFER.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newCreditLoanRechargePwdRequest(String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.CREDIT_LOAN_RECHARGE_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_TRANSFER.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        model.setRetUrl(MessageFormat.format("{0}/{1}", PAY_CALLBACK_CONSOLE_HOST, AsyncUmPayService.CREDIT_LOAN_RECHARGE_TRANSFER.getWebRetCallbackPath()));
        return model;
    }

    public static ProjectTransferRequestModel newCreditLoanOutRequest(String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.CREDIT_LOAN_OUT_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_TRANSFER.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newCreditLoanRepayRequest(String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.MOBILE, AsyncUmPayService.CREDIT_LOAN_REPAY_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_TRANSFER.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newCreditLoanTransferAgentRequest(String orderId, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.WEB, AsyncUmPayService.CREDIT_LOAN_AGENT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_OUT_TRANSFER.getCode();
        model.transAction = UmPayTransAction.OUT.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        return model;
    }

    public static ProjectTransferRequestModel newLuxuryStageRepayRequest(String orderId, String luxuryOrderId, String period, String userId, String amount) {
        ProjectTransferRequestModel model = new ProjectTransferRequestModel(CREDIT_LOAN_ID, orderId, userId, amount, UmPayParticAccType.INDIVIDUAL, Source.MOBILE, AsyncUmPayService.LUXURY_STAGE_REPAY_PROJECT_TRANSFER);
        model.servType = UmPayServType.TRANSFER_IN_TRANSFER.getCode();
        model.transAction = UmPayTransAction.IN.getCode();
        model.particType = UmPayParticType.INVESTOR.getCode();
        model.retUrl = MessageFormat.format(AsyncUmPayService.LUXURY_STAGE_REPAY_PROJECT_TRANSFER.getMobileRetCallbackPath(),
                HUIZU_API_HOST,
                luxuryOrderId,
                period);

        return model;
    }

    private ProjectTransferRequestModel(String projectId, String orderId, String userId, String amount, UmPayParticAccType umPayParticAccType, Source source, AsyncUmPayService asyncUmPayService) {
        super(source, asyncUmPayService);
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
