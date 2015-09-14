package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.service.RepayService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class RepayServiceImpl implements RepayService {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> repay(RepayDto repayDto) {
        DateTime today = new DateTime().withTimeAtStartOfDay();
        long loanId = repayDto.getLoanId();
        int period = repayDto.getPeriod();
        LoanModel loanModel = loanMapper.findById(loanId);
        DateTime loanDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
        LoanType loanType = loanModel.getType();
        LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(loanId, period);
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);
        boolean isFirstPeriod = period == 1;
        boolean isLastPeriod = (LoanPeriodUnit.MONTH == loanType.getLoanPeriodUnit() && period == loanModel.getPeriods())
                || (LoanPeriodUnit.DAY == loanType.getLoanPeriodUnit() && period == 1);

        long corpusMultiplyPeriodDays = 0;
        if (isFirstPeriod) {
            for (InvestModel successInvest : successInvests) {
                int days;
                if (InterestInitiateType.INTEREST_START_AT_INVEST == loanType.getInterestInitiateType()) {
                    DateTime investDate = new DateTime(successInvest.getSuccessTime()).withTimeAtStartOfDay();
                    days = Days.daysBetween(investDate, today).getDays() + 1;
                } else {
                    days = Days.daysBetween(loanDate, today).getDays() + 1;
                }
                corpusMultiplyPeriodDays += successInvest.getAmount() * days;
            }
        } else {
            LoanRepayModel lastLoanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(loanId, period - 1);
            DateTime lastActualRepayDate = new DateTime(lastLoanRepayModel.getActualRepayDate()).withTimeAtStartOfDay();
            int days = Days.daysBetween(lastActualRepayDate, today).getDays() + 1;
            for (InvestModel successInvest : successInvests) {
                corpusMultiplyPeriodDays += successInvest.getAmount() * days;
            }
        }
        long actualInterest = this.calculateInterest(loanModel, corpusMultiplyPeriodDays);

        return null;
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
        try {
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            LoanRepayModel loanRepayModel = loanRepayMapper.findById(orderId);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void generateRepay(long loanId, List<InvestModel> successInvestModels) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<LoanRepayModel> loanRepayModels = this.generateLoanRepay(loanModel, successInvestModels);

        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        for (InvestModel successInvestModel : successInvestModels) {
            investRepayModels.addAll(this.generateInvestRepay(loanModel, successInvestModel));
        }

        loanRepayMapper.create(loanRepayModels);
        investRepayMapper.create(investRepayModels);
    }

    private List<LoanRepayModel> generateLoanRepay(LoanModel loanModel, List<InvestModel> successInvestModels) {
        DateTime loanDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
        InterestInitiateType interestInitiateType = loanModel.getType().getInterestInitiateType();
        LoanPeriodUnit loanPeriodUnit = loanModel.getType().getLoanPeriodUnit();

        int totalPeriods = LoanPeriodUnit.DAY == loanPeriodUnit ? 1 : loanModel.getPeriods();

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();

        for (int index = 0; index < totalPeriods; index++) {
            DateTime periodEndDate;

            boolean isFirstPeriod = index == 0;
            boolean isLastPeriod = index + 1 == totalPeriods;

            if (isFirstPeriod) {
                int daysOfMonth = loanDate.dayOfMonth().getMaximumValue();
                periodEndDate = loanDate.plusDays(daysOfMonth);
                if (LoanPeriodUnit.DAY == loanPeriodUnit) {
                    periodEndDate = loanDate.plusDays(loanModel.getPeriods());
                }
            } else {
                DateTime lastPeriodEndDate = new DateTime(loanRepayModels.get(index - 1).getRepayDate()).withTimeAtStartOfDay();
                int daysOfMonth = lastPeriodEndDate.plusDays(1).dayOfMonth().getMaximumValue();
                periodEndDate = new DateTime(lastPeriodEndDate).plusDays(daysOfMonth);
            }

            long corpusMultiplyPeriodDays = 0;
            for (InvestModel successInvest : successInvestModels) {
                DateTime periodStartDate;
                if (isFirstPeriod) {
                    periodStartDate = loanDate;
                    if (InterestInitiateType.INTEREST_START_AT_INVEST == interestInitiateType) {
                        periodStartDate = new DateTime(successInvest.getSuccessTime()).withTimeAtStartOfDay();
                    }
                } else {
                    periodStartDate = new DateTime(loanRepayModels.get(index - 1).getRepayDate()).plusDays(1);
                }

                int daysOfPeriod = Days.daysBetween(periodStartDate, periodEndDate).getDays() + 1;
                corpusMultiplyPeriodDays += successInvest.getAmount() * daysOfPeriod;

            }

            LoanRepayModel loanRepayModel = new LoanRepayModel();
            loanRepayModel.setId(idGenerator.generate());
            loanRepayModel.setLoanId(loanModel.getId());
            loanRepayModel.setExpectedInterest(calculateInterest(loanModel, corpusMultiplyPeriodDays));
            loanRepayModel.setPeriod(index + 1);
            loanRepayModel.setStatus(RepayStatus.REPAYING);
            loanRepayModel.setRepayDate(periodEndDate.plusDays(1).minusMillis(1).toDate());

            if (isLastPeriod) {
                long totalCorpus = 0;
                for (InvestModel successInvest : successInvestModels) {
                    totalCorpus += successInvest.getAmount();
                }
                loanRepayModel.setCorpus(totalCorpus);
            }

            loanRepayModels.add(loanRepayModel);
        }

        return loanRepayModels;
    }

    private List<InvestRepayModel> generateInvestRepay(LoanModel loanModel, InvestModel investModel) {
        double investFeeRate = loanModel.getInvestFeeRate();
        DateTime loanTime = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
        InterestInitiateType interestInitiateType = loanModel.getType().getInterestInitiateType();
        LoanPeriodUnit loanPeriodUnit = loanModel.getType().getLoanPeriodUnit();

        int totalPeriod = LoanPeriodUnit.DAY == loanPeriodUnit ? 1 : loanModel.getPeriods();

        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        for (int index = 0; index < totalPeriod; index++) {
            InvestRepayModel investRepayModel = new InvestRepayModel();
            investRepayModel.setId(idGenerator.generate());
            investRepayModel.setInvestId(investModel.getId());
            investRepayModel.setPeriod(index + 1);
            investRepayModel.setStatus(RepayStatus.REPAYING);
            boolean isFirstPeriod = index == 0;
            boolean isLastPeriod = index + 1 == totalPeriod;
            if (isFirstPeriod) {
                DateTime periodStartDate = loanTime;
                if (InterestInitiateType.INTEREST_START_AT_INVEST == interestInitiateType) {
                    periodStartDate = new DateTime(investModel.getSuccessTime()).withTimeAtStartOfDay();
                }
                int daysOfPeriod = LoanPeriodUnit.DAY == loanModel.getType().getLoanPeriodUnit() ?
                        loanModel.getPeriods() : loanTime.dayOfMonth().getMaximumValue();
                DateTime periodEndDate = loanTime.plusDays(daysOfPeriod).withTimeAtStartOfDay().minusMillis(1);

                investRepayModel.setRepayDate(periodEndDate.toDate());
                int days = Days.daysBetween(periodStartDate, periodEndDate).getDays() + 1;
                long expectedInterest = calculateInterest(loanModel, investModel.getAmount() * days);
                long expectedFee = (long) (expectedInterest * investFeeRate);
                investRepayModel.setExpectedInterest(expectedInterest);
                investRepayModel.setExpectedFee(expectedFee);
            } else {
                DateTime periodStartDate = new DateTime(investRepayModels.get(index - 1).getRepayDate()).plusDays(1).withTimeAtStartOfDay();
                int daysOfPeriod = periodStartDate.dayOfMonth().getMaximumValue();
                DateTime periodEndDate = periodStartDate.plusDays(daysOfPeriod).withTimeAtStartOfDay().minusMillis(1);
                investRepayModel.setRepayDate(periodEndDate.toDate());
                int days = Days.daysBetween(periodStartDate, periodEndDate).getDays() + 1;
                long expectedInterest = calculateInterest(loanModel, investModel.getAmount() * days);
                long expectedFee = (long) (expectedInterest * investFeeRate);
                investRepayModel.setExpectedInterest(expectedInterest);
                investRepayModel.setExpectedFee(expectedFee);
            }

            if (isLastPeriod) {
                investRepayModel.setCorpus(investModel.getAmount());
            }

            investRepayModels.add(investRepayModel);
        }

        return investRepayModels;
    }

    private long calculateInterest(LoanModel loanModel, long corpusMultiplyPeriodDays) {
        double loanRate = loanModel.getBaseRate() + loanModel.getActivityRate();
        DateTime loanDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
        int daysOfYear = loanDate.dayOfYear().getMaximumValue();
        return (long) (corpusMultiplyPeriodDays * loanRate / daysOfYear);
    }
}
