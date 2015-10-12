package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
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
import com.tuotiansudai.paywrapper.service.RepayService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.paywrapper.service.UserBillService;
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

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

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
    private UserBillService userBillService;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

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

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        if (enabledLoanRepay == null) {
            return baseDto;
        }

        DateTime actualRepayDate = new DateTime();
        long actualInterest = this.calculateActualLoanRepayInterest(loanModel, enabledLoanRepay.getPeriod(), actualRepayDate.withTimeAtStartOfDay());

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
    @Transactional
    public BaseDto<PayFormDataDto> advanceRepay(long loanId) {
        final DateTime now = new DateTime();

        LoanModel loanModel = loanMapper.findById(loanId);

        Ordering<LoanRepayModel> orderingByPeriod = new Ordering<LoanRepayModel>() {
            @Override
            public int compare(LoanRepayModel left, LoanRepayModel right) {
                return Ints.compare(left.getPeriod(), right.getPeriod());
            }
        };

        List<LoanRepayModel> loanRepayModels = orderingByPeriod.sortedCopy(loanRepayMapper.findByLoanId(loanId));


        Optional<LoanRepayModel> optional = Iterators.tryFind(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                Date actualRepayDate = input.getActualRepayDate();
                return actualRepayDate != null && actualRepayDate.before(now.toDate());
            }
        });

        LoanRepayModel currentLoanRepay = loanRepayMapper.findByLoanIdAndPeriod(loanId, 1);
        if (optional.isPresent()) {
            currentLoanRepay = optional.get();
        }

        long actualInterest = this.calculateActualLoanRepayInterest(loanModel, currentLoanRepay.getPeriod(), now.withTimeAtStartOfDay());

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
        for (InvestModel successInvest : successInvests) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(successInvest.getId(), enabledLoanRepay.getPeriod());
            long investRepayId = investRepayModel.getId();
            if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
                logger.error(MessageFormat.format("Invest repay payback has already completed (investRepayId = {0})", String.valueOf(investRepayId)));
                continue;
            }

            String investorLoginName = successInvest.getLoginName();
            AccountModel accountModel = accountMapper.findByLoginName(investorLoginName);

            long actualInvestRepayInterest = this.calculateActualInvestRepayInterest(loanModel, successInvest, enabledLoanRepay.getPeriod(), new DateTime(enabledLoanRepay.getActualRepayDate()).withTimeAtStartOfDay());
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

    private DateTime getLastPeriodRepayDate(LoanModel loanModel, int currentPeriod) {
        DateTime lastPeriodRepayDate = new DateTime(loanModel.getRecheckTime()).minusDays(1).withTimeAtStartOfDay();
        if (currentPeriod > 1) {
            lastPeriodRepayDate = new DateTime(loanRepayMapper.findByLoanIdAndPeriod(loanModel.getId(), currentPeriod - 1).getActualRepayDate()).withTimeAtStartOfDay();
        }
        return lastPeriodRepayDate;
    }

    private long calculateActualLoanRepayInterest(LoanModel loanModel, int period, DateTime actualRepayDate) {
        InterestInitiateType interestInitiateType = loanModel.getType().getInterestInitiateType();
        DateTime lastPeriodRepayDate = this.getLastPeriodRepayDate(loanModel, period);

        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());
        long corpusMultiplyPeriodDays = this.generateLoanRepayCorpusMultiplyPeriodDays(interestInitiateType, successInvestModels, period, lastPeriodRepayDate, actualRepayDate);

        return InterestCalculator.calculateInterest(loanModel, corpusMultiplyPeriodDays);
    }

    private long calculateActualInvestRepayInterest(LoanModel loanModel, InvestModel investModel, int period, DateTime actualRepayDate) {
        InterestInitiateType interestInitiateType = loanModel.getType().getInterestInitiateType();
        DateTime lastPeriodRepayDate = this.getLastPeriodRepayDate(loanModel, period);

        long corpusMultiplyPeriodDays = this.generateInvestRepayCorpusMultiplyPeriodDays(interestInitiateType, investModel, period, lastPeriodRepayDate, actualRepayDate);

        return InterestCalculator.calculateInterest(loanModel, corpusMultiplyPeriodDays);
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
