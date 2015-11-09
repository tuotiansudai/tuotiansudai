package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.AdvanceRepayService;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdvanceRepayServiceImpl implements AdvanceRepayService {

    static Logger logger = Logger.getLogger(AdvanceRepayServiceImpl.class);

    private final static String ADVANCE_REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String ADVANCE_REPAY_ORDER_ID_TEMPLATE = "{0}" + ADVANCE_REPAY_ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private InvestService investService;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> repay(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        if (loanModel.getStatus() == LoanStatus.COMPLETE) {
            logger.error(MessageFormat.format("Loan (loanId = {0}) is complete", String.valueOf(loanId)));
            return baseDto;
        }

        LoanRepayModel confirmingLoanRepay = loanRepayMapper.findConfirmingLoanRepayByLoanId(loanId);
        if (confirmingLoanRepay != null) {
            logger.error(MessageFormat.format("The confirming loan repay is exist (loanRepayId = {0})", String.valueOf(confirmingLoanRepay.getId())));
            return baseDto;
        }

        LoanRepayModel currentLoanRepay = loanRepayMapper.findCurrentLoanRepayByLoanId(loanId);

        if (currentLoanRepay == null) {
            logger.error(MessageFormat.format("There is no loan repay today ({0}) in loan (loanId = {1})", new Date(), String.valueOf(loanId)));
            return baseDto;
        }

        if (currentLoanRepay.getStatus() == RepayStatus.COMPLETE) {
            currentLoanRepay = loanRepayMapper.findByLoanIdAndPeriod(loanId, currentLoanRepay.getPeriod() + 1);
        }

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);

        DateTime currentRepayDate = new DateTime();

        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId), currentRepayDate);

        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvests, lastRepayDate, currentRepayDate);

        currentLoanRepay.setStatus(RepayStatus.CONFIRMING);
        currentLoanRepay.setActualInterest(actualInterest);
        currentLoanRepay.setActualRepayDate(currentRepayDate.toDate());
        loanRepayMapper.update(currentLoanRepay);

        String loanRepayOrderId = MessageFormat.format(ADVANCE_REPAY_ORDER_ID_TEMPLATE, String.valueOf(currentLoanRepay.getId()), String.valueOf(currentRepayDate.getMillis()));

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newAdvanceRepayRequest(
                String.valueOf(loanId),
                loanRepayOrderId,
                accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId(),
                String.valueOf(actualInterest + loanModel.getLoanAmount()));
        try {
            baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("Generate repay form data failed (loanRepayId = {0})", String.valueOf(currentLoanRepay.getId())));
            logger.error(e.getLocalizedMessage(), e);
        }

        return baseDto;
    }

    @Override
    public String repayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postRepayCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    @Transactional
    private void postRepayCallback(BaseCallbackRequestModel callbackRequestModel) {
        if (!callbackRequestModel.isSuccess()) {
            return;
        }

        long loanRepayId;
        try {
            loanRepayId = Long.parseLong(callbackRequestModel.getOrderId().split(ADVANCE_REPAY_ORDER_ID_SEPARATOR)[0]);
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("Loan repay id is invalid (loanRepayId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepayId);
        if (enabledLoanRepay.getStatus() != RepayStatus.CONFIRMING) {
            logger.error(MessageFormat.format("Loan repay status is not confirming (loanRepayId = {0})", String.valueOf(loanRepayId)));
            return;
        }

        LoanModel loanModel = loanMapper.findById(enabledLoanRepay.getLoanId());

        //更新代理人账户
        try {
            userBillService.transferOutBalance(loanModel.getAgentLoginName(),
                    enabledLoanRepay.getId(),
                    enabledLoanRepay.getActualInterest() + loanModel.getLoanAmount(),
                    UserBillBusinessType.ADVANCE_REPAY);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("Transfer out balance for loan repay interest (loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())));
            logger.error(e.getLocalizedMessage(), e);
        }

        //还款后回款
        this.paybackInvestRepay(loanModel, enabledLoanRepay);

        //更新LoanRepay Status = COMPLETE
        for (LoanRepayModel loanRepayModel : loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId())) {
            loanRepayModel.setStatus(RepayStatus.COMPLETE);
            if (loanRepayModel.getActualRepayDate() == null) {
                loanRepayModel.setActualRepayDate(enabledLoanRepay.getActualRepayDate());
            }
            loanRepayMapper.update(loanRepayModel);
        }

        //更新Loan Status = COMPLETE
        loanMapper.updateStatus(loanModel.getId(), LoanStatus.COMPLETE);

        // TODO: 2.多余的钱返平台 更新联动优势状态
    }

    private void paybackInvestRepay(LoanModel loanModel, LoanRepayModel enabledLoanRepay) {
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        DateTime currentRepayDate = new DateTime(enabledLoanRepay.getActualRepayDate());
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId()), currentRepayDate);

        for (InvestModel successInvest : successInvests) {
            InvestRepayModel enabledInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(successInvest.getId(), enabledLoanRepay.getPeriod());
            long investRepayId = enabledInvestRepayModel.getId();
            if (enabledInvestRepayModel.getStatus() == RepayStatus.COMPLETE) {
                logger.error(MessageFormat.format("Invest repay payback has already completed (investRepayId = {0})", String.valueOf(investRepayId)));
                continue;
            }

            String investorLoginName = successInvest.getLoginName();
            AccountModel accountModel = accountMapper.findByLoginName(investorLoginName);

            long actualInvestRepayInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, successInvest, lastRepayDate, currentRepayDate);
            long actualInvestFee = new BigDecimal(actualInvestRepayInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();

            boolean investRepayPaybackSuccess = actualInvestRepayInterest == 0;
            if (actualInvestRepayInterest > 0) {
                ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newRepayPaybackRequest(String.valueOf(loanModel.getId()),
                        String.valueOf(MessageFormat.format(ADVANCE_REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(currentRepayDate.getMillis()))),
                        accountModel.getPayUserId(),
                        String.valueOf(actualInvestRepayInterest - actualInvestFee + successInvest.getAmount()));

                try {
                    ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                    investRepayPaybackSuccess = responseModel.isSuccess();
                } catch (PayException e) {
                    logger.error(MessageFormat.format("Repay payback failed (investRepayId = {0})", String.valueOf(investRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
            }

            if (investRepayPaybackSuccess) {
                for (InvestRepayModel investRepayModel : investRepayMapper.findByInvestId(successInvest.getId())) {
                    investRepayModel.setActualInterest(actualInvestRepayInterest);
                    investRepayModel.setActualFee(actualInvestFee);
                    investRepayModel.setActualRepayDate(enabledLoanRepay.getActualRepayDate());
                    investRepayModel.setStatus(RepayStatus.COMPLETE);
                    investRepayMapper.update(investRepayModel);
                }

                long investRepayAmount = actualInvestRepayInterest + successInvest.getAmount();
                userBillService.transferInBalance(investorLoginName, investRepayId, investRepayAmount, UserBillBusinessType.ADVANCE_REPAY);
                this.paybackInvestFee(loanModel.getId(), investRepayId, accountModel, actualInvestFee);
            }

        }
        investService.notifyInvestorRepaySuccessfulByEmail(loanModel.getId(),enabledLoanRepay.getPeriod());
    }

    private void paybackInvestFee(long loanId, long investRepayId, AccountModel investorAccount, long actualInvestFee) {
        boolean repayInvestFeeSuccess = actualInvestFee == 0;
        if (actualInvestFee > 0) {
            String investFeeOrderId = MessageFormat.format(ADVANCE_REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(new Date().getTime()));
            ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newRepayInvestFeeRequest(String.valueOf(loanId),
                    investFeeOrderId,
                    investorAccount.getPayUserId(),
                    String.valueOf(actualInvestFee));

            try {
                ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                repayInvestFeeSuccess = responseModel.isSuccess();
            } catch (PayException e) {
                logger.error(MessageFormat.format("Repay payback failed (investRepayId = {0})", String.valueOf(investRepayId)));
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        if (repayInvestFeeSuccess) {
            try {
                userBillService.transferOutBalance(investorAccount.getLoginName(), investRepayId, actualInvestFee, UserBillBusinessType.INVEST_FEE);
            } catch (AmountTransferException e) {
                logger.error(MessageFormat.format("transfer out balance for invest repay fee (investRepayId = {0})", String.valueOf(investRepayId)));
                logger.error(e.getLocalizedMessage(), e);
            }
            systemBillService.transferIn(actualInvestFee, String.valueOf(investRepayId), SystemBillBusinessType.INVEST_FEE, "");
        }
    }
}
