package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.service.RepayService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RepayServiceImpl implements RepayService {

    static Logger logger = Logger.getLogger(RepayServiceImpl.class);

    private static String REPAY_ORDER_ID_TEMPLATE = "{0}REPAY{1}";

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
    private AccountMapper accountMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Override
    @Transactional
    public void generateRepay(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        InterestInitiateType interestInitiateType = loanModel.getType().getInterestInitiateType();
        boolean isPeriodUnitDay = LoanPeriodUnit.DAY == loanModel.getType().getLoanPeriodUnit();
        int totalPeriods = loanModel.calculateLoanRepayTimes();

        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        DateTime lastPeriodRepayDate = new DateTime(loanModel.getRecheckTime()).minusDays(1).withTimeAtStartOfDay();
        for (int index = 0; index < totalPeriods; index++) {
            int period = index + 1;
            int currentPeriodDuration = isPeriodUnitDay ? loanModel.getPeriods() : lastPeriodRepayDate.plusDays(1).dayOfMonth().getMaximumValue();

            DateTime currentPeriodRepayDate = lastPeriodRepayDate.plusDays(currentPeriodDuration).withTimeAtStartOfDay().toDateTime();

            long expectedLoanInterest = InterestCalculator.calculateInterest(loanModel, this.generateLoanRepayCorpusMultiplyPeriodDays(interestInitiateType, successInvestModels, period, lastPeriodRepayDate, currentPeriodRepayDate));

            LoanRepayModel loanRepayModel = new LoanRepayModel(idGenerator.generate(), loanModel.getId(), period, expectedLoanInterest, currentPeriodRepayDate.toDate(), RepayStatus.REPAYING);

            long currentPeriodCorpus = 0;
            for (InvestModel successInvestModel : successInvestModels) {
                long expectedInvestInterest = InterestCalculator.calculateInterest(loanModel, this.generateInvestRepayCorpusMultiplyPeriodDays(interestInitiateType, successInvestModel, period, lastPeriodRepayDate, currentPeriodRepayDate));
                long expectedFee = new BigDecimal(expectedInvestInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();

                InvestRepayModel investRepayModel = new InvestRepayModel(idGenerator.generate(), successInvestModel.getId(), period, expectedInvestInterest, expectedFee, currentPeriodRepayDate.toDate(), RepayStatus.REPAYING);
                if (period == totalPeriods) {
                    investRepayModel.setCorpus(successInvestModel.getAmount());
                }
                currentPeriodCorpus += investRepayModel.getCorpus();
                investRepayModels.add(investRepayModel);
            }

            loanRepayModel.setCorpus(currentPeriodCorpus);
            loanRepayModels.add(loanRepayModel);
            lastPeriodRepayDate = currentPeriodRepayDate;
        }

        loanRepayMapper.create(loanRepayModels);
        investRepayMapper.create(investRepayModels);
    }

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> repay(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        LoanRepayModel enabledLoanRepay = loanRepayMapper.findEnabledRepayByLoanId(loanId);

        DateTime actualRepayDate = new DateTime().withTimeAtStartOfDay();

        long actualInterest = this.calculateActualLoanRepayInterest(loanModel, enabledLoanRepay.getPeriod(), actualRepayDate);

        String orderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(enabledLoanRepay.getId()), String.valueOf(actualRepayDate.getMillis()));

        AccountModel accountModel = accountMapper.findByLoginName(loanModel.getLoanerLoginName());
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newRepayRequest(
                String.valueOf(loanId),
                orderId,
                accountModel.getPayUserId(),
                String.valueOf(actualInterest + enabledLoanRepay.getDefaultInterest() + enabledLoanRepay.getCorpus()));
        try {
            return payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
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
        try {
            String orderId = callbackRequestModel.getOrderId();
            String[] splits = orderId.split("REPAY");
            long loanRepayId = Long.parseLong(splits[0]);
            Date actualRepayDate = new Date(Long.parseLong(splits[1]));
            LoanRepayModel loanRepayModel = loanRepayMapper.findById(loanRepayId);
            LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());

            long loanRepayInterest = this.calculateActualLoanRepayInterest(loanModel,
                    loanRepayModel.getPeriod(),
                    new DateTime(actualRepayDate).withTimeAtStartOfDay());

            loanRepayModel.setActualInterest(loanRepayInterest);
            loanRepayModel.setActualRepayDate(actualRepayDate);
            loanRepayModel.setStatus(RepayStatus.COMPLETE);

            if (loanModel.calculateLoanRepayTimes() == loanRepayModel.getPeriod()) {
                loanMapper.updateStatus(loanModel.getId(), LoanStatus.COMPLETE);
                //TODO:最后一期更新标的状态
            }

            loanRepayMapper.update(loanRepayModel);

            //TODO: 1.出借人返款 2.多余的钱返平台

        } catch (NumberFormatException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private long calculateActualLoanRepayInterest(LoanModel loanModel, int period, DateTime actualRepayDate) {
        InterestInitiateType interestInitiateType = loanModel.getType().getInterestInitiateType();

        DateTime lastPeriodRepayDate = new DateTime(loanModel.getRecheckTime()).minusDays(1).withTimeAtStartOfDay();
        if (period > 1) {
            lastPeriodRepayDate = new DateTime(loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), period - 1).getActualRepayDate()).withTimeAtStartOfDay();
        }

        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());
        return InterestCalculator.calculateInterest(loanModel,
                this.generateLoanRepayCorpusMultiplyPeriodDays(interestInitiateType, successInvestModels, period, lastPeriodRepayDate, actualRepayDate));
    }

    private long generateLoanRepayCorpusMultiplyPeriodDays(InterestInitiateType interestInitiateType, List<InvestModel> successInvestModels, int period, DateTime lastPeriodRepayDate, DateTime currentPeriodRepayDate) {
        long corpusMultiplyPeriodDays = 0;
        for (InvestModel successInvest : successInvestModels) {
            corpusMultiplyPeriodDays += this.generateInvestRepayCorpusMultiplyPeriodDays(interestInitiateType, successInvest, period, lastPeriodRepayDate, currentPeriodRepayDate);
        }
        return corpusMultiplyPeriodDays;
    }

    private long generateInvestRepayCorpusMultiplyPeriodDays(InterestInitiateType interestInitiateType, InvestModel investModel, int period, DateTime lastPeriodRepayDate, DateTime currentPeriodRepayDate) {
        boolean isFirstPeriod = period == 1;
        DateTime investRepayStartDate = new DateTime(lastPeriodRepayDate).plusDays(1).withTimeAtStartOfDay();
        if (isFirstPeriod && InterestInitiateType.INTEREST_START_AT_INVEST == interestInitiateType) {
            investRepayStartDate = new DateTime(investModel.getCreatedTime()).withTimeAtStartOfDay();
        }
        int periodDuration = Days.daysBetween(investRepayStartDate, currentPeriodRepayDate).getDays() + 1;
        return investModel.getAmount() * periodDuration;
    }
}
