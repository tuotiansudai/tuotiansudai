package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.job.AdvanceRepayJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.NormalRepayJob;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdvanceRepayServiceImpl extends NormalRepayServiceImpl {

    static Logger logger = Logger.getLogger(NormalRepayServiceImpl.class);

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> repay(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        if (loanModel.getStatus() == LoanStatus.COMPLETE) {
            logger.error(MessageFormat.format("[Advance Repay] Loan has been complete (loanId = {0})", String.valueOf(loanId)));
            return baseDto;
        }

        LoanRepayModel waitPayLoanRepay = loanRepayMapper.findWaitPayLoanRepayByLoanId(loanId);
        if (waitPayLoanRepay != null) {
            logger.error(MessageFormat.format("[Advance Repay] The WAIT_PAY loan repay is exist (loanRepayId = {0})", String.valueOf(waitPayLoanRepay.getId())));
            return baseDto;
        }

        LoanRepayModel currentLoanRepay = loanRepayMapper.findCurrentLoanRepayByLoanId(loanId);

        if (currentLoanRepay == null) {
            logger.error(MessageFormat.format("[Advance Repay] There is no loan repay today ({0}) in loan (loanId = {1})", new Date(), String.valueOf(loanId)));
            return baseDto;
        }

        if (currentLoanRepay.getStatus() == RepayStatus.COMPLETE) {
            currentLoanRepay = loanRepayMapper.findByLoanIdAndPeriod(loanId, currentLoanRepay.getPeriod() + 1);
        }

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);

        DateTime currentRepayDate = new DateTime();

        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId), currentRepayDate.toDate());

        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvests, lastRepayDate, currentRepayDate);

        long defaultInterest = 0;
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            defaultInterest += loanRepayModel.getDefaultInterest();
        }

        currentLoanRepay.setStatus(RepayStatus.WAIT_PAY);
        currentLoanRepay.setActualInterest(actualInterest);
        currentLoanRepay.setActualRepayDate(currentRepayDate.toDate());
        loanRepayMapper.update(currentLoanRepay);

        String loanRepayOrderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(currentLoanRepay.getId()), String.valueOf(currentRepayDate.getMillis()));

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newAdvanceRepayRequest(
                String.valueOf(loanId),
                loanRepayOrderId,
                accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId(),
                String.valueOf(actualInterest + defaultInterest + loanRepayModels.get(loanRepayModels.size() - 1).getCorpus()));
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

        LoanRepayModel enabledLoanRepay;
        try {
            long loanRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
            enabledLoanRepay = loanRepayMapper.findById(loanRepayId);
            if (enabledLoanRepay != null && enabledLoanRepay.getStatus() == RepayStatus.WAIT_PAY) {
                this.createAdvanceRepayJob(loanRepayId);
            } else {
                logger.error(MessageFormat.format("[Normal Repay] Loan repay is not existing or status is not WAIT_PAY (loanRepayId = {0})", String.valueOf(loanRepayId)));
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[Normal Repay] Loan repay id is invalid (loanRepayId = {0})", callbackRequest.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }

        return callbackRequest.getResponseData();
    }

    @Override
    @Transactional
    public String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            long investRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
            InvestRepayModel currentInvestRepayModel = investRepayMapper.findById(investRepayId);

            if (currentInvestRepayModel.getStatus() == RepayStatus.REPAYING) {
                InvestModel investModel = investMapper.findById(currentInvestRepayModel.getInvestId());
                LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
                LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), currentInvestRepayModel.getPeriod());

                final DateTime currentRepayDate = new DateTime(loanRepayModel.getActualRepayDate());

                DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);
                if (currentInvestRepayModel.getPeriod() > 1) {
                    List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
                    for (LoanRepayModel model : loanRepayModels) {
                        if (loanRepayModel.getPeriod() > model.getPeriod() && model.getActualRepayDate() != null && model.getActualRepayDate().before(currentRepayDate.toDate())) {
                            lastRepayDate = new DateTime(model.getActualRepayDate());
                        }
                    }
                }

                long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel, lastRepayDate, currentRepayDate);
                long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();
                long defaultInterest = 0;
                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestId(investModel.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    defaultInterest += investRepayModel.getDefaultInterest();
                }

                this.updateInvestorUserBill(investModel.getLoginName(), investRepayId, investModel.getAmount() + actualInterest + defaultInterest, actualFee);
                this.updateInvestRepay(investRepayId, investModel.getId(), actualInterest, actualFee, loanRepayModel.getActualRepayDate());
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[Advance Repay] Loan repay id is invalid (investRepayId = {0})", callbackRequest.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("[Advance Repay] Update investor user bill failed (investRepayId = {0})", callbackRequest.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }

        return callbackRequest.getResponseData();
    }

    @Override
    public String investFeeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        try {
            long investRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
            InvestRepayModel currentInvestRepayModel = investRepayMapper.findById(investRepayId);
            if (systemBillMapper.findByOrderId(investRepayId) == null) {
                InvestModel investModel = investMapper.findById(currentInvestRepayModel.getInvestId());
                LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
                LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), currentInvestRepayModel.getPeriod());

                final DateTime currentRepayDate = new DateTime(loanRepayModel.getActualRepayDate());

                DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);
                if (currentInvestRepayModel.getPeriod() > 1) {
                    Ordering<LoanRepayModel> descOrdering = new Ordering<LoanRepayModel>() {
                        @Override
                        public int compare(LoanRepayModel left, LoanRepayModel right) {
                            return Ints.compare(right.getPeriod(), left.getPeriod());
                        }
                    };

                    List<LoanRepayModel> orderingLoanRepayModels = descOrdering.sortedCopy(loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId()));
                    Optional<LoanRepayModel> optional = Iterators.tryFind(orderingLoanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
                        @Override
                        public boolean apply(LoanRepayModel input) {
                            return input.getActualRepayDate() != null && input.getActualRepayDate().before(currentRepayDate.toDate());
                        }
                    });

                    if (optional.isPresent()) {
                        lastRepayDate = new DateTime(optional.get().getActualRepayDate()).withTimeAtStartOfDay();
                    }
                }

                long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel, lastRepayDate, currentRepayDate);
                long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();

                systemBillService.transferIn(investRepayId,
                        actualFee,
                        SystemBillBusinessType.INVEST_FEE,
                        MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), investModel.getLoginName(), String.valueOf(investRepayId)));
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[Normal Repay] Loan repay id is invalid (investRepayId = {0})", callbackRequest.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }

        return callbackRequest.getResponseData();
    }

    @Override
    public void postRepayCallback(long loanRepayId) {
        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepayId);

        LoanModel loanModel = loanMapper.findById(enabledLoanRepay.getLoanId());

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
        long defaultInterest = 0;
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            defaultInterest += loanRepayModel.getDefaultInterest();
        }

        long repayAmount = enabledLoanRepay.getActualInterest() + defaultInterest + loanRepayModels.get(loanRepayModels.size() - 1).getCorpus();

        //更新代理人账户
        try {
            amountTransfer.transferOutBalance(loanModel.getAgentLoginName(),
                    enabledLoanRepay.getId(),
                    repayAmount,
                    loanModel.getStatus() == LoanStatus.OVERDUE ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.ADVANCE_REPAY,
                    null, null);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("[Advance Repay] Transfer out balance for loan repay interest (loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())));
            logger.error(e.getLocalizedMessage(), e);
        }

        long totalPayback = this.paybackInvestRepay(enabledLoanRepay);

        this.investService.notifyInvestorRepaySuccessfulByEmail(loanModel.getId(), enabledLoanRepay.getPeriod());

        //更新LoanRepay Status = COMPLETE
        this.updateLoanRepayStatus(enabledLoanRepay);

        //多余利息返平台, 更新Loan Status = COMPLETE

        this.transferLoanBalance(enabledLoanRepay, repayAmount - totalPayback);

        loanService.updateLoanStatus(loanModel.getId(), LoanStatus.COMPLETE);
    }

    private long paybackInvestRepay(LoanRepayModel loanRepayModel) {
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        DateTime currentRepayDate = new DateTime(loanRepayModel.getActualRepayDate());
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId()), currentRepayDate.toDate());

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        long totalPayback = 0;

        for (InvestModel investModel : successInvests) {
            String investorLoginName = investModel.getLoginName();
            AccountModel accountModel = accountMapper.findByLoginName(investorLoginName);
            InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), loanRepayModel.getPeriod());

            long currentInvestRepayId = currentInvestRepayModel.getId();
            if (currentInvestRepayModel.getStatus() == RepayStatus.COMPLETE) {
                logger.error(MessageFormat.format("[Normal Repay] Invest repay has been COMPLETE (currentInvestRepayId = {0})", String.valueOf(currentInvestRepayId)));
                continue;
            }

            long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel, lastRepayDate, currentRepayDate);
            long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();
            long defaultInterest = 0;
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestId(investModel.getId());
            for (InvestRepayModel investRepayModel : investRepayModels) {
                defaultInterest += investRepayModel.getDefaultInterest();
            }

            long amount = investModel.getAmount() + actualInterest + defaultInterest - actualFee;

            totalPayback += investModel.getAmount() + actualInterest + defaultInterest;

            if (amount > 0) {
                ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newAdvanceRepayPaybackRequest(String.valueOf(loanModel.getId()),
                        MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(currentInvestRepayId), String.valueOf(currentRepayDate.getMillis())),
                        accountModel.getPayUserId(),
                        String.valueOf(amount));
                try {
                    this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Normal Repay] Invest payback is failed (currentInvestRepayId = {0})", String.valueOf(currentInvestRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                try {
                    this.updateInvestorUserBill(investorLoginName, currentInvestRepayId, investModel.getAmount() + actualInterest + defaultInterest, actualFee);
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("[Normal Repay] Update investor user bill failed (currentInvestRepayId = {0})", String.valueOf(currentInvestRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
                this.updateInvestRepay(currentInvestRepayId, investModel.getId(), actualInterest, actualFee, currentRepayDate.toDate());
            }

            if (actualFee > 0) {
                try {
                    ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newAdvanceRepayInvestFeeRequest(String.valueOf(loanModel.getId()),
                            MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(currentInvestRepayId), String.valueOf(new Date().getTime())),
                            String.valueOf(actualFee));
                    this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Repay] Invest fee is failed(currentInvestRepayId = {0})", String.valueOf(currentInvestRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                systemBillService.transferIn(currentInvestRepayId,
                        actualFee,
                        SystemBillBusinessType.INVEST_FEE,
                        MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), investorLoginName, String.valueOf(currentInvestRepayId)));
            }
        }

        return totalPayback;
    }

    @Transactional
    private void updateLoanRepayStatus(LoanRepayModel enabledLoanRepay) {
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(enabledLoanRepay.getLoanId());
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            loanRepayModel.setStatus(RepayStatus.COMPLETE);
            if (loanRepayModel.getActualRepayDate() == null) {
                loanRepayModel.setActualRepayDate(enabledLoanRepay.getActualRepayDate());
            }
            loanRepayMapper.update(loanRepayModel);
        }

    }

    @Transactional
    private void updateInvestRepay(long currentInvestRepayId, long investId, long actualInterest, long InvestFee, Date actualRepayDate) {
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestId(investId);
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getId() == currentInvestRepayId) {
                investRepayModel.setActualInterest(actualInterest);
                investRepayModel.setActualFee(InvestFee);
            }
            investRepayModel.setActualRepayDate(actualRepayDate);
            investRepayModel.setStatus(RepayStatus.COMPLETE);
            investRepayMapper.update(investRepayModel);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void updateInvestorUserBill(String investorLoginName, long investRepayId, long actualPaybackAmount, long actualFee) throws AmountTransferException {
        InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestId(investRepayModel.getInvestId());
        boolean isOverdueRepay = Iterators.tryFind(investRepayModels.iterator(), new Predicate<InvestRepayModel>() {
            @Override
            public boolean apply(InvestRepayModel input) {
                return input.getStatus() == RepayStatus.OVERDUE;
            }
        }).isPresent();

        amountTransfer.transferInBalance(investorLoginName, investRepayId, actualPaybackAmount,
                isOverdueRepay ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.ADVANCE_REPAY,
                null, null);
        amountTransfer.transferOutBalance(investorLoginName, investRepayId, actualFee, UserBillBusinessType.INVEST_FEE, null, null);
    }

    private void createAdvanceRepayJob(long loanRepayId) {
        Date fiveMinutesLater = new DateTime().plusMinutes(5).toDate();
        try {
            jobManager.newJob(JobType.AdvanceRepay, AdvanceRepayJob.class)
                    .runOnceAt(fiveMinutesLater)
                    .addJobData(NormalRepayJob.LOAN_REPAY_ID_KEY, String.valueOf(loanRepayId))
                    .submit();
        } catch (SchedulerException e) {
            logger.error(MessageFormat.format("Create normal repay job failed (loanRepayId = {0})", String.valueOf(loanRepayId)), e);
        }
    }
}
