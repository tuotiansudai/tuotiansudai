package com.tuotiansudai.paywrapper.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.job.AdvanceRepayJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.NormalRepayJob;
import com.tuotiansudai.paywrapper.dto.LoanRepayJobResultDto;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdvanceRepayServiceImpl extends NormalRepayServiceImpl {

    static Logger logger = Logger.getLogger(AdvanceRepayServiceImpl.class);

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

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);

        Optional<LoanRepayModel> optional = Iterators.tryFind(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() != RepayStatus.COMPLETE && input.getRepayDate().after(new Date());
            }
        });

        LoanRepayModel currentLoanRepay = optional.isPresent() ? optional.get() : loanRepayModels.get(loanRepayModels.size() - 1);

        if (currentLoanRepay.getStatus() == RepayStatus.COMPLETE) {
            logger.error(MessageFormat.format("[Advance Repay] There is no loan repay today ({0}) in loan (loanId = {1})", new Date(), String.valueOf(loanId)));
            return baseDto;
        }

        DateTime currentRepayDate = new DateTime();
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels, currentRepayDate);

        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, investMapper.findSuccessInvestsByLoanId(loanId), lastRepayDate, currentRepayDate);
        long defaultInterest = this.calculateLoanRepayDefaultInterest(loanRepayModels);

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
            logger.error(MessageFormat.format("[Advance Repay] Generate repay form data failed (loanRepayId = {0})", String.valueOf(currentLoanRepay.getId())), e);
        }

        return baseDto;
    }

    @Override
    public String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);
        return callbackRequest == null ? null : callbackRequest.getResponseData();
    }

    @Override
    public String investFeeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);
        return callbackRequest == null ? null : callbackRequest.getResponseData();
    }

    @Override
    public boolean postRepayCallback(long loanRepayId) {
        LoanRepayJobResultDto jobData;
        try {
            String value = redisWrapperClient.get(MessageFormat.format(LOAN_REPAY_JOB_DATA_KEY_TEMPLATE, String.valueOf(loanRepayId)));
            jobData = objectMapper.readValue(value, LoanRepayJobResultDto.class);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Advance Repay] Fetch job data from redis is failed (loanRepayId = {0})", String.valueOf(loanRepayId)));
            return false;
        }

        if (!jobData.isUpdateAgentUserBillSuccess()) {
            try {
                this.updateLoanAgentUserBill(jobData, UserBillBusinessType.ADVANCE_REPAY);
                jobData.setUpdateAgentUserBillSuccess(true);
            } catch (AmountTransferException e) {
                logger.error(MessageFormat.format("[Advance Repay] Transfer out balance for loan repay interest is failed (loanRepayId = {0})", String.valueOf(jobData.getLoanRepayId())), e);
            }
        }

        if (!jobData.isUpdateLoanRepayStatusSuccess()) {
            try {
                this.updateLoanRepayStatus(jobData);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Advance Repay] Update loan repay status is failed (loanRepayId = {0})", String.valueOf(jobData.getLoanRepayId())), e);
            }
        }

        if (!jobData.isUpdateLoanStatusSuccess()) {
            try {
                loanService.updateLoanStatus(jobData.getLoanId(), LoanStatus.COMPLETE);
                jobData.setUpdateLoanStatusSuccess(true);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Advance Repay] Update loan status is failed (loanRepayId = {0})", String.valueOf(jobData.getLoanRepayId())), e);
            }
        }

        this.paybackInvestRepay(jobData);

        this.transferLoanBalance(jobData);

        this.storeJobData(jobData);

        boolean isFail = jobData.isFail();

        if (isFail) {
            this.createRepayJob(loanRepayId);
        }

        String value = redisWrapperClient.get(MessageFormat.format(LOAN_REPAY_JOB_DATA_KEY_TEMPLATE, String.valueOf(loanRepayId)));
        logger.info(MessageFormat.format("[Advance Repay] repay job data : {0}", value));

        return !isFail;
    }

    @Transactional
    protected void updateLoanRepayStatus(LoanRepayJobResultDto jobData) {
        long loanId = jobData.getLoanId();
        Date actualRepayDate = jobData.getActualRepayDate();
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            loanRepayModel.setStatus(RepayStatus.COMPLETE);
            loanRepayModel.setActualRepayDate(loanRepayModel.getActualRepayDate() == null ? actualRepayDate : loanRepayModel.getActualRepayDate());
            loanRepayMapper.update(loanRepayModel);
        }
    }

    @Transactional
    protected void updateInvestRepay(long investId, long currentInvestRepayId, long actualInterest, long actualFee, Date actualRepayDate) {
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investId);
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getId() == currentInvestRepayId) {
                investRepayModel.setActualInterest(actualInterest);
                investRepayModel.setActualFee(actualFee);
            }
            investRepayModel.setStatus(RepayStatus.COMPLETE);
            investRepayModel.setActualRepayDate(investRepayModel.getActualRepayDate() == null ? actualRepayDate : investRepayModel.getActualRepayDate());
            investRepayMapper.update(investRepayModel);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected void updateInvestorUserBill(boolean isOverdueRepay, String investorLoginName, long investRepayId, long actualPaybackAmount, long actualFee) throws AmountTransferException {
        amountTransfer.transferInBalance(investorLoginName, investRepayId, actualPaybackAmount,
                isOverdueRepay ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.ADVANCE_REPAY,
                null, null);
        amountTransfer.transferOutBalance(investorLoginName, investRepayId, actualFee, UserBillBusinessType.INVEST_FEE, null, null);
    }

    @Override
    protected void createRepayJob(long loanRepayId) {
        try {
            this.generateJobData(loanRepayId, true);
            Date fiveMinutesLater = new DateTime().plusMinutes(5).toDate();
            jobManager.newJob(JobType.AdvanceRepay, AdvanceRepayJob.class)
                    .runOnceAt(fiveMinutesLater)
                    .addJobData(NormalRepayJob.LOAN_REPAY_ID, loanRepayId)
                    .withIdentity(JobType.AdvanceRepay.name(), MessageFormat.format(REPAY_JOB_NAME_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(new DateTime().getMillis())))
                    .submit();
        } catch (JsonProcessingException e) {
            logger.error(MessageFormat.format("[Advance Repay] Generate advance repay job data failed (loanRepayId = {0})", String.valueOf(loanRepayId)), e);
        } catch (SchedulerException e) {
            logger.error(MessageFormat.format("[Advance Repay] Create advance repay job failed (loanRepayId = {0})", String.valueOf(loanRepayId)), e);
        }
    }
}
