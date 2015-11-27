package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.NormalRepayJob;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
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
import com.tuotiansudai.util.JobManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
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

    private static Logger logger = Logger.getLogger(NormalRepayServiceImpl.class);

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
    protected SystemBillMapper systemBillMapper;

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

    @Autowired
    protected JobManager jobManager;

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

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);

        DateTime actualRepayDate = new DateTime();
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels, actualRepayDate);
        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, actualRepayDate);

        long defaultInterest = 0;
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            defaultInterest += loanRepayModel.getDefaultInterest();
        }

        enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
        enabledLoanRepay.setActualInterest(actualInterest);
        enabledLoanRepay.setActualRepayDate(actualRepayDate.toDate());
        loanRepayMapper.update(enabledLoanRepay);

        String loanRepayOrderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(enabledLoanRepay.getId()), String.valueOf(actualRepayDate.getMillis()));

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newRepayRequest(
                String.valueOf(loanId),
                loanRepayOrderId,
                accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId(),
                String.valueOf(actualInterest + defaultInterest + enabledLoanRepay.getCorpus()));
        try {
            baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("[Normal Repay] Generate loan repay form data failed (loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())));
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
                this.createNormalRepayJob(loanRepayId);
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
                long loanId = investModel.getLoanId();
                LoanModel loanModel = loanMapper.findById(loanId);
                List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
                LoanRepayModel currentLoanRepayModel = loanRepayModels.get(currentInvestRepayModel.getPeriod() - 1);

                DateTime currentRepayDate = new DateTime(currentLoanRepayModel.getActualRepayDate());
                DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels, currentRepayDate);

                long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel, lastRepayDate, currentRepayDate);
                long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();

                long defaultInterest = 0;
                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    defaultInterest += investRepayModel.getDefaultInterest();
                }

                this.updateInvestorUserBill(investModel.getLoginName(), investRepayId, currentInvestRepayModel.getCorpus() + actualInterest + defaultInterest, actualFee);
                this.updateInvestRepay(currentInvestRepayModel.getInvestId(), currentInvestRepayModel.getPeriod(), actualInterest, actualFee, currentLoanRepayModel.getActualRepayDate());
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[Normal Repay] Loan repay id is invalid (investRepayId = {0})", callbackRequest.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("[Normal Repay] Update investor user bill failed (investRepayId = {0})", callbackRequest.getOrderId()));
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
                List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
                LoanRepayModel currentLoanRepayModel = loanRepayModels.get(currentInvestRepayModel.getPeriod() - 1);

                DateTime currentRepayDate = new DateTime(currentLoanRepayModel.getActualRepayDate());

                DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels, currentRepayDate);

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

        //更新代理人账户
        long repayAmount = enabledLoanRepay.getActualInterest() + defaultInterest + enabledLoanRepay.getCorpus();
        try {
            amountTransfer.transferOutBalance(loanModel.getAgentLoginName(),
                    enabledLoanRepay.getId(),
                    repayAmount,
                    loanModel.getStatus() == LoanStatus.OVERDUE ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.NORMAL_REPAY,
                    null, null);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("[Normal Repay] Transfer out balance for loan repay interest is failed(loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())));
            logger.error(e.getLocalizedMessage(), e);
        }

        long totalPayback = this.paybackInvestRepay(enabledLoanRepay);

        //更新LoanRepay状态
        this.updateLoanRepayStatus(enabledLoanRepay);

        //多余利息返平台
        this.transferLoanBalance(enabledLoanRepay, repayAmount - totalPayback);

        //最后一期更新Loan Status = COMPLETE
        int currentPeriod = enabledLoanRepay.getPeriod();

        this.updateLoanStatus(loanModel, currentPeriod);

        this.investService.notifyInvestorRepaySuccessfulByEmail(loanModel.getId(), currentPeriod);
    }

    private long paybackInvestRepay(LoanRepayModel loanRepayModel) {
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        DateTime currentRepayDate = new DateTime(loanRepayModel.getActualRepayDate());
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId()), currentRepayDate);

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        long totalPayback = 0;

        for (InvestModel investModel : successInvests) {
            String investorLoginName = investModel.getLoginName();
            AccountModel accountModel = accountMapper.findByLoginName(investorLoginName);
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            InvestRepayModel currentInvestRepayModel = investRepayModels.get(loanRepayModel.getPeriod() - 1);

            long investRepayId = currentInvestRepayModel.getId();
            if (currentInvestRepayModel.getStatus() == RepayStatus.COMPLETE) {
                logger.error(MessageFormat.format("[Normal Repay] Invest repay has been COMPLETE (investRepayId = {0})", String.valueOf(investRepayId)));
                continue;
            }

            long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel, lastRepayDate, currentRepayDate);
            long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();
            long defaultInterest = 0;

            for (InvestRepayModel investRepayModel : investRepayModels) {
                defaultInterest += investRepayModel.getDefaultInterest();
            }
            long amount = currentInvestRepayModel.getCorpus() + actualInterest + defaultInterest - actualFee;

            totalPayback += currentInvestRepayModel.getCorpus() + actualInterest + defaultInterest;

            if (amount > 0) {
                ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newRepayPaybackRequest(String.valueOf(loanModel.getId()),
                        MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(currentRepayDate.getMillis())),
                        accountModel.getPayUserId(),
                        String.valueOf(amount));
                try {
                    this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Normal Repay] Invest payback is failed (investRepayId = {0})", String.valueOf(investRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                try {
                    this.updateInvestorUserBill(investorLoginName, investRepayId, currentInvestRepayModel.getCorpus() + actualInterest + defaultInterest, actualFee);
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("[Normal Repay] Update investor user bill failed (investRepayId = {0})", String.valueOf(investRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
                this.updateInvestRepay(investModel.getId(), loanRepayModel.getPeriod(), actualInterest, actualFee, currentRepayDate.toDate());
            }

            if (actualFee > 0) {
                try {
                    ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newRepayInvestFeeRequest(String.valueOf(loanModel.getId()),
                            MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(new Date().getTime())),
                            String.valueOf(actualFee));
                    this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Repay] Invest fee is failed(investRepayId = {0})", String.valueOf(investRepayId)));
                    logger.error(e.getLocalizedMessage(), e);
                }
            } else {
                systemBillService.transferIn(investRepayId,
                        actualFee,
                        SystemBillBusinessType.INVEST_FEE,
                        MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), investorLoginName, String.valueOf(investRepayId)));
            }
        }

        return totalPayback;
    }

    protected void transferLoanBalance(LoanRepayModel enabledLoanRepay, long amount) {
        if (amount <= 0) {
            return;
        }

        long loanRepayId = enabledLoanRepay.getId();
        String orderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(new Date().getTime()));
        ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newLoanRemainAmountRequest(String.valueOf(enabledLoanRepay.getLoanId()),
                orderId,
                String.valueOf(amount));

        try {
            ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
            if (responseModel.isSuccess()) {
                systemBillService.transferIn(loanRepayId,
                        amount,
                        SystemBillBusinessType.LOAN_REMAINING_INTEREST,
                        MessageFormat.format(SystemBillDetailTemplate.LOAN_REMAINING_INTEREST_DETAIL_TEMPLATE.getTemplate(), String.valueOf(loanRepayId), String.valueOf(amount)));
            }
        } catch (PayException e) {
            logger.error(MessageFormat.format("[Repay] Loan remaining interest transfer is failed (loanRepayId = {0} amount = {1})", String.valueOf(loanRepayId), String.valueOf(amount)));
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Transactional
    private void updateLoanRepayStatus(LoanRepayModel enabledLoanRepay) {
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(enabledLoanRepay.getLoanId());
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getStatus() == RepayStatus.OVERDUE || enabledLoanRepay.getId() == loanRepayModel.getId()) {
                loanRepayModel.setStatus(RepayStatus.COMPLETE);
                loanRepayModel.setActualRepayDate(enabledLoanRepay.getActualRepayDate());
                loanRepayMapper.update(loanRepayModel);
            }
        }
    }

    @Transactional
    private void updateInvestRepay(long investId, final int currentPeriod, long actualInterest, long InvestFee, Date actualRepayDate) {
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investId);
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getPeriod() <= currentPeriod) {
                if (investRepayModel.getStatus() == RepayStatus.OVERDUE) {
                    investRepayModel.setActualRepayDate(actualRepayDate);
                    investRepayModel.setStatus(RepayStatus.COMPLETE);
                }
                if (investRepayModel.getPeriod() == currentPeriod) {
                    investRepayModel.setActualInterest(actualInterest);
                    investRepayModel.setActualFee(InvestFee);
                    investRepayModel.setActualRepayDate(actualRepayDate);
                    investRepayModel.setStatus(RepayStatus.COMPLETE);
                }
                investRepayMapper.update(investRepayModel);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void updateInvestorUserBill(String investorLoginName, long investRepayId, long actualPaybackAmount, long actualFee) throws AmountTransferException {
        InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investRepayModel.getInvestId());
        boolean isOverdueRepay = Iterators.tryFind(investRepayModels.iterator(), new Predicate<InvestRepayModel>() {
            @Override
            public boolean apply(InvestRepayModel input) {
                return input.getStatus() == RepayStatus.OVERDUE;
            }
        }).isPresent();

        amountTransfer.transferInBalance(investorLoginName, investRepayId, actualPaybackAmount,
                isOverdueRepay ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.NORMAL_REPAY,
                null, null);
        amountTransfer.transferOutBalance(investorLoginName, investRepayId, actualFee, UserBillBusinessType.INVEST_FEE, null, null);
    }

    @Transactional
    private void updateLoanStatus(LoanModel loanModel, int currentPeriod) {
        boolean isLastPeriod = loanModel.calculateLoanRepayTimes() == currentPeriod;
        if (isLastPeriod) {
            loanService.updateLoanStatus(loanModel.getId(), LoanStatus.COMPLETE);
        } else {
            loanMapper.updateStatus(loanModel.getId(), LoanStatus.REPAYING);
        }
    }

    private void createNormalRepayJob(long loanRepayId) {
        Date fiveMinutesLater = new DateTime().plusMinutes(5).toDate();
        try {
            jobManager.newJob(JobType.NormalRepay, NormalRepayJob.class)
                    .runOnceAt(fiveMinutesLater)
                    .addJobData(NormalRepayJob.LOAN_REPAY_ID_KEY, String.valueOf(loanRepayId))
                    .submit();
        } catch (SchedulerException e) {
            logger.error(MessageFormat.format("Create normal repay job failed (loanRepayId = {0})", String.valueOf(loanRepayId)), e);
        }
    }
}
