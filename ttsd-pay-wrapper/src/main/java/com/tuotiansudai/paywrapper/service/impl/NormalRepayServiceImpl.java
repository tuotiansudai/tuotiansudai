package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
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
import com.tuotiansudai.paywrapper.service.NormalRepayService;
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
public class NormalRepayServiceImpl implements NormalRepayService {

    static Logger logger = Logger.getLogger(NormalRepayServiceImpl.class);

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

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
            logger.error(MessageFormat.format("Currently, there is no enabled loan repay in loan (loanId = {0})", String.valueOf(loanId)));
            return baseDto;
        }

        DateTime actualRepayDate = new DateTime();
        DateTime lastRepayDate = this.getLastRepayDate(loanModel, actualRepayDate);
        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, actualRepayDate);

        enabledLoanRepay.setStatus(RepayStatus.CONFIRMING);
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
            logger.error(MessageFormat.format("Generate repay form data failed (loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())));
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
            loanRepayId = Long.parseLong(callbackRequestModel.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
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
                    enabledLoanRepay.getActualInterest() + enabledLoanRepay.getDefaultInterest() + enabledLoanRepay.getCorpus(),
                    UserBillBusinessType.NORMAL_REPAY);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("Transfer out balance for loan repay interest (loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())));
            logger.error(e.getLocalizedMessage(), e);
        }

        //还款后回款
        this.paybackInvestRepay(loanModel, enabledLoanRepay);

        //更新LoanRepay Status = COMPLETE
        enabledLoanRepay.setStatus(RepayStatus.COMPLETE);
        loanRepayMapper.update(enabledLoanRepay);

        //最后一期更新Loan Status = COMPLETE
        boolean isLastPeriod = loanModel.calculateLoanRepayTimes() == enabledLoanRepay.getPeriod();
        if (isLastPeriod) {
            loanMapper.updateStatus(loanModel.getId(), LoanStatus.COMPLETE);
            //TODO:最后一期更新标的状态
        }

        // TODO: 2.多余的钱返平台
    }

    private void paybackInvestRepay(LoanModel loanModel, LoanRepayModel enabledLoanRepay) {
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        DateTime currentRepayDate = new DateTime(enabledLoanRepay.getActualRepayDate());
        DateTime lastRepayDate = this.getLastRepayDate(loanModel, currentRepayDate);

        for (InvestModel successInvest : successInvests) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(successInvest.getId(), enabledLoanRepay.getPeriod());
            long investRepayId = investRepayModel.getId();
            if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
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
                        String.valueOf(investRepayId),
                        accountModel.getPayUserId(),
                        String.valueOf(actualInvestRepayInterest - actualInvestFee + investRepayModel.getDefaultInterest() + investRepayModel.getCorpus()));

                try {
                    ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                    investRepayPaybackSuccess = responseModel.isSuccess();
                } catch (PayException e) {
                    logger.error(MessageFormat.format("Repay payback failed (investRepayId = {0})", String.valueOf(investRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
            }

            if (investRepayPaybackSuccess) {
                investRepayModel.setActualInterest(actualInvestRepayInterest);
                investRepayModel.setActualFee(actualInvestFee);
                investRepayModel.setActualRepayDate(enabledLoanRepay.getActualRepayDate());
                investRepayModel.setStatus(RepayStatus.COMPLETE);
                investRepayMapper.update(investRepayModel);
                long investRepayAmount = actualInvestRepayInterest + investRepayModel.getDefaultInterest() + investRepayModel.getCorpus();
                userBillService.transferInBalance(investorLoginName, investRepayId, investRepayAmount, UserBillBusinessType.NORMAL_REPAY);
                this.paybackInvestFee(loanModel.getId(), investRepayId, accountModel, actualInvestFee);
            }

        }
    }

    private void paybackInvestFee(long loanId, long investRepayId, AccountModel investorAccount, long actualInvestFee) {
        boolean repayInvestFeeSuccess = actualInvestFee == 0;
        if (actualInvestFee > 0) {
            String investFeeOrderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(new Date().getTime()));
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

    private DateTime getLastRepayDate(LoanModel loanModel, final DateTime currentRepayDate) {
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).minusDays(1).withTimeAtStartOfDay();

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());

        Optional<LoanRepayModel> optional = Iterators.tryFind(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() == RepayStatus.COMPLETE && new DateTime(input.getActualRepayDate()).withTimeAtStartOfDay().getMillis() <= currentRepayDate.withTimeAtStartOfDay().getMillis();
            }
        });

        if (optional.isPresent()) {
            lastRepayDate = new DateTime(optional.get().getActualRepayDate()).withTimeAtStartOfDay();
        }

        return lastRepayDate;
    }
}
