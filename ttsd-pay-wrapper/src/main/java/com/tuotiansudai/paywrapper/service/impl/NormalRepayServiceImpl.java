package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.paywrapper.service.RepayService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.InterestCalculator;
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
public class NormalRepayServiceImpl implements RepayService {

    static Logger logger = Logger.getLogger(NormalRepayServiceImpl.class);

    protected final static String REPAY_ORDER_ID_SEPARATOR = "X";

    protected final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    protected AccountMapper accountMapper;

    @Autowired
    protected InvestMapper investMapper;

    @Autowired
    protected LoanMapper loanMapper;

    @Autowired
    protected InvestRepayMapper investRepayMapper;

    @Autowired
    protected LoanRepayMapper loanRepayMapper;

    @Autowired
    protected AmountTransfer amountTransfer;

    @Autowired
    protected SystemBillService systemBillService;

    @Autowired
    protected PayAsyncClient payAsyncClient;

    @Autowired
    protected PaySyncClient paySyncClient;

    @Autowired
    protected InvestService investService;

    @Autowired
    protected LoanService loanService;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> repay(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        LoanRepayModel enabledLoanRepay = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        if (enabledLoanRepay == null) {
            logger.error(MessageFormat.format("[Normal Repay] There is no enabled loan repay (loanId = {0})", String.valueOf(loanId)));
            return baseDto;
        }

        DateTime actualRepayDate = new DateTime();
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId), actualRepayDate);
        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, actualRepayDate);

        enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
        enabledLoanRepay.setActualInterest(actualInterest);
        enabledLoanRepay.setActualRepayDate(actualRepayDate.toDate());
        loanRepayMapper.update(enabledLoanRepay);

        String loanRepayOrderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(enabledLoanRepay.getId()), String.valueOf(actualRepayDate.getMillis()));

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newRepayRequest(
                String.valueOf(loanId),
                loanRepayOrderId,
                accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId(),
                String.valueOf(actualInterest + enabledLoanRepay.getDefaultInterest() + enabledLoanRepay.getCorpus()));
        try {
            baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("[Normal Repay] Generate loan repay form data failed (loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())));
            logger.error(e.getLocalizedMessage(), e);
        }

        return baseDto;
    }

    public String repayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postRepayCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    public String repayPaybackCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postRepayPaybackCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    protected void postRepayCallback(BaseCallbackRequestModel callbackRequestModel) {
        if (!callbackRequestModel.isSuccess()) {
            return;
        }

        long loanRepayId;
        LoanRepayModel enabledLoanRepay;
        try {
            loanRepayId = Long.parseLong(callbackRequestModel.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
            enabledLoanRepay = loanRepayMapper.findById(loanRepayId);
            if (enabledLoanRepay == null || enabledLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
                logger.error(MessageFormat.format("[Normal Repay] Loan repay is not existing or status is not WAIT_PAY (loanRepayId = {0})", String.valueOf(loanRepayId)));
                return;
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[Normal Repay] Loan repay id is invalid (loanRepayId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
            return;
        }

        LoanModel loanModel = loanMapper.findById(enabledLoanRepay.getLoanId());

        //更新代理人账户
        try {
            amountTransfer.transferOutBalance(loanModel.getAgentLoginName(),
                    enabledLoanRepay.getId(),
                    enabledLoanRepay.getActualInterest() + enabledLoanRepay.getDefaultInterest() + enabledLoanRepay.getCorpus(),
                    UserBillBusinessType.NORMAL_REPAY, null, null);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("[Normal Repay] Transfer out balance for loan repay interest is failed(loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())));
            logger.error(e.getLocalizedMessage(), e);
        }

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());
        for (InvestModel successInvest : successInvests) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(successInvest.getId(), enabledLoanRepay.getPeriod());
            if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
                logger.error(MessageFormat.format("[Normal Repay] Invest repay has been COMPLETE (investRepayId = {0})", String.valueOf(investRepayModel.getId())));
                continue;
            }
            this.paybackInvestRepay(investRepayModel.getId());
            this.paybackInvestFee(investRepayModel.getId());
        }

        //更新LoanRepay状态
        this.updateLoanRepayStatus(enabledLoanRepay);

        this.investService.notifyInvestorRepaySuccessfulByEmail(loanModel.getId(), enabledLoanRepay.getPeriod());

        //最后一期更新Loan Status = COMPLETE
        boolean isLastPeriod = loanModel.calculateLoanRepayTimes() == enabledLoanRepay.getPeriod();
        if (isLastPeriod) {
            //多余利息返平台 最后一期更新标的状态
            if (this.transferInLoanRemainingAmount(loanModel.getId())) {
                loanService.updateLoanStatus(loanModel.getId(), LoanStatus.COMPLETE);
            }
        }
    }

    protected void postRepayPaybackCallback(BaseCallbackRequestModel callbackRequestModel) {
        if (!callbackRequestModel.isSuccess()) {
            return;
        }

        try {
            long investRepayId = Long.parseLong(callbackRequestModel.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
            this.updateInvestRepay(investRepayModel.getId(), actualInvestRepayInterest, actualInvestFee, enabledLoanRepay.getActualRepayDate());
            try {
                amountTransfer.transferInBalance(investorLoginName,
                        investRepayModel.getId(),
                        investRepayModel.getActualInterest() + investRepayModel.getDefaultInterest() + investRepayModel.getCorpus(),
                        UserBillBusinessType.NORMAL_REPAY, null, null);
            } catch (AmountTransferException e) {
                logger.error(MessageFormat.format("[Normal Repay] Invest payback transfer in balance failed (investRepayId = {0})", String.valueOf(investRepayModel.getId())));
                logger.error(e.getLocalizedMessage(), e);
            }

        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[Normal Repay] Loan repay id is invalid (investRepayId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }

    }

    protected void paybackInvestRepay(long investRepayId) {
        InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);
        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        LoanRepayModel enabledLoanRepay = loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), investRepayModel.getPeriod());

        DateTime currentRepayDate = new DateTime(enabledLoanRepay.getActualRepayDate());
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId()), currentRepayDate);

        String investorLoginName = investModel.getLoginName();
        AccountModel accountModel = accountMapper.findByLoginName(investorLoginName);

        long actualInvestRepayInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel, lastRepayDate, currentRepayDate);
        long actualInvestFee = new BigDecimal(actualInvestRepayInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();

        boolean investRepayPaybackSuccess = actualInvestRepayInterest == 0;
        if (actualInvestRepayInterest > 0) {
            ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newRepayPaybackRequest(String.valueOf(loanModel.getId()),
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(new Date().getTime())),
                    accountModel.getPayUserId(),
                    String.valueOf(actualInvestRepayInterest - actualInvestFee + investRepayModel.getDefaultInterest() + investRepayModel.getCorpus()));

            try {
                ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                investRepayPaybackSuccess = responseModel.isSuccess();
            } catch (PayException e) {
                logger.error(MessageFormat.format("[Normal Repay] Invest payback is failed (investRepayId = {0})", String.valueOf(investRepayId)));
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        if (investRepayPaybackSuccess) {
            this.updateInvestRepay(investRepayModel.getId(), actualInvestRepayInterest, actualInvestFee, enabledLoanRepay.getActualRepayDate());
            try {
                amountTransfer.transferInBalance(investorLoginName,
                        investRepayModel.getId(),
                        investRepayModel.getActualInterest() + investRepayModel.getDefaultInterest() + investRepayModel.getCorpus(),
                        UserBillBusinessType.NORMAL_REPAY, null, null);
            } catch (AmountTransferException e) {
                logger.error(MessageFormat.format("[Normal Repay] Invest payback transfer in balance failed (investRepayId = {0})", String.valueOf(investRepayModel.getId())));
                logger.error(e.getLocalizedMessage(), e);
            }
        }
    }

    protected void paybackInvestFee(long investRepayId) {
        InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);
        long actualFee = investRepayModel.getActualFee();
        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        String investorLoginName = investModel.getLoginName();
        AccountModel accountModel = accountMapper.findByLoginName(investorLoginName);

        if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
            boolean investRepayFeePaybackSuccess = actualFee == 0;
            if (actualFee > 0) {
                try {
                    ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newRepayInvestFeeRequest(String.valueOf(loanModel.getId()),
                            MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(new Date().getTime())),
                            String.valueOf(actualFee));
                    ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                    investRepayFeePaybackSuccess = responseModel.isSuccess();
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Repay] invest repay fee is failed(investRepayId = {0})", String.valueOf(investRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
            }

            if (investRepayFeePaybackSuccess) {
                try {
                    systemBillService.transferIn(investRepayId,
                            actualFee,
                            SystemBillBusinessType.INVEST_FEE,
                            MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), investorLoginName, String.valueOf(investRepayId)));
                    amountTransfer.transferOutBalance(accountModel.getLoginName(), investRepayId, actualFee, UserBillBusinessType.INVEST_FEE, null, null);
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("[Repay] invest repay fee is failed(investRepayId = {0})", String.valueOf(investRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    @Transactional
    protected boolean transferInLoanRemainingAmount(long loanId) {
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoanId(loanId);
        long remainingAmount = 0;

        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            remainingAmount += loanRepayModel.getActualInterest() + loanRepayModel.getDefaultInterest() + loanRepayModel.getCorpus();
        }

        for (InvestRepayModel investRepayModel : investRepayModels) {
            remainingAmount -= investRepayModel.getActualInterest() + investRepayModel.getDefaultInterest() + investRepayModel.getCorpus();
        }

        if (remainingAmount == 0) {
            return true;
        }

        String remainingAmountTransferOrderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(loanId), String.valueOf(new Date().getTime()));
        ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newLoanRemainAmountRequest(String.valueOf(loanId),
                remainingAmountTransferOrderId,
                String.valueOf(remainingAmount));

        try {
            ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
            if (responseModel.isSuccess()) {
                systemBillService.transferIn(loanId,
                        remainingAmount,
                        SystemBillBusinessType.LOAN_REMAINING_INTEREST,
                        MessageFormat.format(SystemBillDetailTemplate.LOAN_REMAINING_INTEREST_DETAIL_TEMPLATE.getTemplate(), String.valueOf(loanId), String.valueOf(remainingAmount)));
                return true;
            }
        } catch (PayException e) {
            logger.error(MessageFormat.format("[Normal Repay] Loan remaining interest transfer is failed (loanId = {0} remainingInterest = {1})", String.valueOf(loanId), String.valueOf(remainingAmount)));
            logger.error(e.getLocalizedMessage(), e);
        }

        return false;
    }

    @Transactional
    protected void updateLoanRepayStatus(LoanRepayModel loanRepay) {
        //更新LoanRepay Status = COMPLETE
        loanRepay.setStatus(RepayStatus.COMPLETE);
        loanRepayMapper.update(loanRepay);
    }

    @Transactional
    protected void updateInvestRepay(long investRepayId, long actualInvestRepayInterest, long actualInvestFee, Date actualRepayDate) {
        InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);
        investRepayModel.setActualInterest(actualInvestRepayInterest);
        investRepayModel.setActualFee(actualInvestFee);
        investRepayModel.setActualRepayDate(actualRepayDate);
        //更新InvestRepay Status = COMPLETE
        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(investRepayModel);
    }
}
