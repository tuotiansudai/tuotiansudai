package com.tuotiansudai.paywrapper.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.dto.InvestRepayJobResultDto;
import com.tuotiansudai.paywrapper.dto.LoanRepayJobResultDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.NormalRepayJob;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
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

    protected final static String LOAN_REPAY_JOB_DATA_KEY_TEMPLATE = "pay:repay:{0}";

    protected final static String REPAY_JOB_NAME_TEMPLATE = "REPAY-{0}-{1}";

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

    @Autowired
    protected RedisWrapperClient redisWrapperClient;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public boolean autoRepay(long loanRepayId) {
        LoanRepayModel loanRepayModel = loanRepayMapper.findById(loanRepayId);
        long loanId = loanRepayModel.getLoanId();
        logger.info("auto repay start , loanId : " + loanId);
        LoanModel loanModel = loanMapper.findById(loanId);
        if (RepayStatus.REPAYING != loanRepayModel.getStatus()) {
            logger.info("can not auto repay, because loan repay status is not repaying , loanId : " + loanId);
            return false;
        }
        AccountModel accountModel = accountMapper.findByLoginName(loanModel.getAgentLoginName());
        if (!accountModel.isAutoRepay()) {
            logger.info("can not auto repay, because agent is not open auto repay , loanId : " + loanId);
            return false;
        }
        long balance = accountModel.getBalance();
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
        LoanRepayModel enabledLoanRepay = getEnabledLoanRepay(loanModel, loanRepayModels);
        if (balance < enabledLoanRepay.getCorpus() + enabledLoanRepay.getActualInterest() + enabledLoanRepay.getDefaultInterest()) {
            logger.info("can not auto repay, because agent balance is not enough , loanId : " + loanId);
            return false;
        }
        long defaultInterest = this.calculateLoanRepayDefaultInterest(loanRepayModels);
        loanRepayMapper.update(enabledLoanRepay);
        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newRepayNopwdRequest(
                String.valueOf(loanId),
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(enabledLoanRepay.getId()), String.valueOf(new DateTime(enabledLoanRepay.getActualRepayDate()).getMillis())),
                accountModel.getPayUserId(),
                String.valueOf(enabledLoanRepay.getCorpus() + enabledLoanRepay.getActualInterest() + defaultInterest)
        );
        ProjectTransferNopwdResponseModel responseModel;
        try {
            responseModel = paySyncClient.send(
                    ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
        return responseModel.isSuccess();
    }

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> repay(long loanId) {

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        LoanModel loanModel = loanMapper.findById(loanId);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
        LoanRepayModel enabledLoanRepay = getEnabledLoanRepay(loanModel, loanRepayModels);

        if (enabledLoanRepay == null) {
            logger.error(MessageFormat.format("[Normal Repay] There is no enabled loan repay (loanId = {0})", String.valueOf(loanId)));
            return baseDto;
        }

        long defaultInterest = this.calculateLoanRepayDefaultInterest(loanRepayModels);
        loanRepayMapper.update(enabledLoanRepay);
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newRepayRequest(String.valueOf(loanId),
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(enabledLoanRepay.getId()), String.valueOf(new DateTime(enabledLoanRepay.getActualRepayDate()).getMillis())),
                accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId(),
                String.valueOf(enabledLoanRepay.getCorpus() + enabledLoanRepay.getActualInterest() + defaultInterest));
        try {
            baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("[Normal Repay] Generate loan repay form data is failed (loanRepayId = {0})", String.valueOf(enabledLoanRepay.getId())), e);
        }

        return baseDto;
    }

    private LoanRepayModel getEnabledLoanRepay(LoanModel loanModel, List<LoanRepayModel> loanRepayModels) {
        LoanRepayModel enabledLoanRepay = loanRepayMapper.findEnabledLoanRepayByLoanId(loanModel.getId());
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());
        DateTime actualRepayDate = new DateTime();
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels, actualRepayDate);
        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, actualRepayDate);
        enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
        enabledLoanRepay.setActualInterest(actualInterest);
        enabledLoanRepay.setActualRepayDate(actualRepayDate.toDate());
        return enabledLoanRepay;
    }

    @Override
    @Transactional
    public String repayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap,
                originalQueryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        Long loanRepayId = this.parseLoanRepayId(callbackRequest);
        if (loanRepayId != null) {
            this.createRepayJob(this.generateJobData(loanRepayId, false), 5);
            try {
                this.updateLoanAgentUserBill(loanRepayId, UserBillBusinessType.NORMAL_REPAY);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Normal Repay] Update loan agent bill is failed (loanRepayId = {0})", String.valueOf(loanRepayId)), e);
            }
            try {
                this.updateLoanRepayStatus(loanRepayId);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Normal Repay] Update loan repay status is failed (loanRepayId = {0})", String.valueOf(loanRepayId)), e);
            }

        }

        return callbackRequest.getResponseData();
    }

    @Override
    @Transactional
    public boolean postRepayCallback(long loanRepayId) {
        LoanRepayJobResultDto jobData;
        try {
            String value = redisWrapperClient.get(MessageFormat.format(LOAN_REPAY_JOB_DATA_KEY_TEMPLATE, String.valueOf(loanRepayId)));
            logger.info(MessageFormat.format("[Normal Repay] Job data = {0}", value));
            jobData = objectMapper.readValue(value, LoanRepayJobResultDto.class);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Normal Repay] Fetch job data from redis is failed (loanRepayId = {0})", String.valueOf(loanRepayId)));
            return false;
        }

        try {
            this.paybackInvestRepay(jobData);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Normal Repay] Payback invest repay is failed (loanRepayId = {0})", String.valueOf(jobData.getLoanRepayId())), e);
        }

        try {
            this.transferLoanBalance(jobData);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Normal Repay] Transfer loan balance is failed (loanRepayId = {0})", String.valueOf(jobData.getLoanRepayId())), e);
        }

        if (!jobData.isUpdateLoanStatusSuccess()) {
            try {
                BaseDto<PayDataDto> dto = loanService.updateLoanStatus(jobData.getLoanId(), jobData.isLastPeriod() ? LoanStatus.COMPLETE : LoanStatus.REPAYING);
                jobData.setUpdateLoanStatusSuccess(dto.getData().getStatus());
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Normal Repay] Update loan status is failed (loanRepayId = {0})", String.valueOf(jobData.getLoanRepayId())), e);
            }
        }

        this.createRepayJob(jobData, 60);

        if (!jobData.jobRetry()) {
            logger.info(MessageFormat.format("[Normal Repay] Normal repay is success (loanRepayId = {0})", String.valueOf(loanRepayId)));
            try {
                redisWrapperClient.del(MessageFormat.format(LOAN_REPAY_JOB_DATA_KEY_TEMPLATE, String.valueOf(loanRepayId)));
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Normal Repay] Delete job data is failed (loanRepayId = {0})", String.valueOf(loanRepayId)), e);
            }
            return true;
        }

        return false;
    }

    @Override
    public String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);
        return callbackRequest != null ? callbackRequest.getResponseData() : null;
    }

    @Override
    public String investFeeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);
        return callbackRequest != null ? callbackRequest.getResponseData() : null;
    }

    protected Long parseLoanRepayId(BaseCallbackRequestModel callbackRequest) {
        if (!callbackRequest.isSuccess()) {
            logger.error(MessageFormat.format("[Repay] Loan repay callback is not success (loanRepayId = {0})", callbackRequest.getOrderId()));
            return null;
        }

        long loanRepayId;
        try {
            loanRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[Repay] Loan repay id is invalid (loanRepayId = {0})", callbackRequest.getOrderId()), e);
            return null;
        }

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findById(loanRepayId);
        if (enabledLoanRepay == null || enabledLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[Repay] Loan repay is not existing or status is not WAIT_PAY (loanRepayId = {0})", String.valueOf(loanRepayId)));
            return null;
        }
        return loanRepayId;
    }

    protected LoanRepayJobResultDto generateJobData(long loanRepayId, boolean isAdvanceRepay) {
        LoanRepayModel loanRepayModel = loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());

        DateTime currentRepayDate = new DateTime(loanRepayModel.getActualRepayDate());
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel,
                loanRepayModels,
                currentRepayDate);

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        List<InvestRepayJobResultDto> investRepayJobResults = Lists.newArrayList();

        long loanRepayBalance = (isAdvanceRepay ? loanRepayModels.get(loanRepayModels.size() - 1).getCorpus() : loanRepayModel.getCorpus()) + loanRepayModel.getActualInterest() + this.calculateLoanRepayDefaultInterest(loanRepayModels);

        for (InvestModel investModel : successInvests) {
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            InvestRepayModel investRepayModel = investRepayModels.get(loanRepayModel.getPeriod() - 1);

            long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel, lastRepayDate, currentRepayDate);
            long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue();
            long defaultInterest = this.calculateInvestRepayDefaultInterest(investRepayModels);
            long corpus = isAdvanceRepay ? investModel.getAmount() : investRepayModel.getCorpus();

            loanRepayBalance -= corpus + actualInterest + defaultInterest;
            investRepayJobResults.add(new InvestRepayJobResultDto(investModel.getId(),
                    investRepayModel.getId(),
                    investModel.getLoginName(),
                    corpus,
                    actualInterest,
                    actualFee,
                    defaultInterest));

        }

        return new LoanRepayJobResultDto(loanModel.getId(),
                loanRepayModel.getId(),
                loanModel.getStatus() == LoanStatus.OVERDUE,
                loanRepayBalance,
                loanRepayModel.getActualRepayDate(),
                loanModel.calculateLoanRepayTimes() == loanRepayModel.getPeriod(),
                investRepayJobResults);
    }

    protected void createRepayJob(LoanRepayJobResultDto loanRepayJobResultDto, int delayMinutes) {
        long loanRepayId = loanRepayJobResultDto.getLoanRepayId();
        try {
            if (this.storeJobData(loanRepayJobResultDto) && loanRepayJobResultDto.jobRetry()) {
                Date temMinutesLater = new DateTime().plusMinutes(delayMinutes).toDate();
                jobManager.newJob(JobType.NormalRepay, NormalRepayJob.class)
                        .runOnceAt(temMinutesLater)
                        .addJobData(NormalRepayJob.LOAN_REPAY_ID, loanRepayId)
                        .withIdentity(JobType.NormalRepay.name(), MessageFormat.format(REPAY_JOB_NAME_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(new DateTime().getMillis())))
                        .submit();
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Normal Repay] Create normal repay job failed (loanRepayId = {0})", String.valueOf(loanRepayId)), e);
        }
    }

    protected long calculateInvestRepayDefaultInterest(List<InvestRepayModel> investRepayModels) {
        long defaultInterest = 0;
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getStatus() == RepayStatus.OVERDUE) {
                defaultInterest += investRepayModel.getDefaultInterest();
            }
        }
        return defaultInterest;
    }

    protected long calculateLoanRepayDefaultInterest(List<LoanRepayModel> loanRepayModels) {
        long defaultInterest = 0;
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (Lists.newArrayList(RepayStatus.OVERDUE, RepayStatus.WAIT_PAY).contains(loanRepayModel.getStatus())) {
                defaultInterest += loanRepayModel.getDefaultInterest();
            }
        }
        return defaultInterest;
    }

    protected void updateLoanAgentUserBill(long loanRepayId, UserBillBusinessType userBillBusinessType) {
        LoanRepayModel loanRepayModel = loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());

        long defaultInterest = this.calculateLoanRepayDefaultInterest(loanRepayModels);
        long repayAmount = (UserBillBusinessType.ADVANCE_REPAY == userBillBusinessType ? loanRepayModels.get(loanRepayModels.size() - 1).getCorpus() : loanRepayModel.getCorpus()) + loanRepayModel.getActualInterest() + defaultInterest;

        try {
            amountTransfer.transferOutBalance(loanModel.getAgentLoginName(),
                    loanRepayId,
                    repayAmount,
                    loanModel.getStatus() == LoanStatus.OVERDUE ? UserBillBusinessType.OVERDUE_REPAY : userBillBusinessType,
                    null, null);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("[{0} Repay] Transfer out balance for loan repay interest is failed (loanRepayId = {1})",
                    UserBillBusinessType.ADVANCE_REPAY == userBillBusinessType ? "Advance" : "Normal",
                    String.valueOf(loanRepayId)), e);
        }
    }

    protected void updateLoanRepayStatus(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        Date actualRepayDate = currentLoanRepay.getActualRepayDate();
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (Lists.newArrayList(RepayStatus.WAIT_PAY, RepayStatus.OVERDUE).contains(loanRepayModel.getStatus())) {
                loanRepayModel.setStatus(RepayStatus.COMPLETE);
                loanRepayModel.setActualRepayDate(actualRepayDate);
                loanRepayMapper.update(loanRepayModel);
            }
        }
    }

    protected void transferLoanBalance(LoanRepayJobResultDto jobData) {
        //多余利息返平台
        long loanId = jobData.getLoanId();
        long loanRepayId = jobData.getLoanRepayId();
        long loanRepayBalance = jobData.getLoanRepayBalance();

        if (jobData.getLoanRepayBalanceStatus() == SyncRequestStatus.READY) {
            try {
                jobData.setLoanRepayBalanceStatus(SyncRequestStatus.SENT);
                String orderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(new DateTime().getMillis()));
                ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newLoanRemainAmountRequest(String.valueOf(loanId),
                        orderId,
                        String.valueOf(loanRepayBalance));
                ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                jobData.setLoanRepayBalanceStatus(responseModel.isSuccess() ? SyncRequestStatus.SUCCESS : SyncRequestStatus.FAILURE);
            } catch (PayException e) {
                logger.error(MessageFormat.format("[Repay] Transfer Loan repay balance is failed (loanRepayId = {0} amount = {1})", String.valueOf(loanRepayId), String.valueOf(loanRepayBalance)), e);
            }
        }

        if (jobData.getLoanRepayBalanceStatus() == SyncRequestStatus.SUCCESS && !jobData.isUpdateSystemBillSuccess()) {
            try {
                systemBillService.transferIn(loanRepayId,
                        loanRepayBalance,
                        SystemBillBusinessType.LOAN_REMAINING_INTEREST,
                        MessageFormat.format(SystemBillDetailTemplate.LOAN_REMAINING_INTEREST_DETAIL_TEMPLATE.getTemplate(), String.valueOf(loanRepayId), String.valueOf(loanRepayBalance)));
                jobData.setUpdateSystemBillSuccess(true);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Repay] Update system bill loan repay balance is failed (loanRepayId = {0} amount = {1})", String.valueOf(loanRepayId), String.valueOf(loanRepayBalance)), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void paybackInvestRepay(LoanRepayJobResultDto jobData) {
        long loanId = jobData.getLoanId();
        Date actualRepayDate = jobData.getActualRepayDate();
        boolean isOverdueRepay = jobData.isOverdueRepay();

        for (InvestRepayJobResultDto investRepayJobResult : jobData.getInvestRepayJobResults()) {
            long investId = investRepayJobResult.getInvestId();
            long investRepayId = investRepayJobResult.getInvestRepayId();
            String investorLoginName = investRepayJobResult.getInvestorLoginName();
            long corpus = investRepayJobResult.getCorpus();
            long actualInterest = investRepayJobResult.getActualInterest();
            long actualFee = investRepayJobResult.getActualFee();
            long defaultInterest = investRepayJobResult.getDefaultInterest();

            AccountModel accountModel = accountMapper.findByLoginName(investorLoginName);

            if (investRepayJobResult.getInterestStatus() == SyncRequestStatus.READY) {
                try {
                    investRepayJobResult.setInterestStatus(SyncRequestStatus.SENT);
                    ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newRepayPaybackRequest(String.valueOf(loanId),
                            MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(new DateTime().getMillis())),
                            accountModel.getPayUserId(),
                            String.valueOf(corpus + actualInterest + defaultInterest - actualFee));
                    ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                    investRepayJobResult.setInterestStatus(responseModel.isSuccess() ? SyncRequestStatus.SUCCESS : SyncRequestStatus.FAILURE);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Normal Repay] Transfer invest interest is failed (investRepayId = {0})", String.valueOf(investRepayId)), e);
                }
            }

            if (investRepayJobResult.getInterestStatus() == SyncRequestStatus.SUCCESS && !investRepayJobResult.isUpdateInvestorUserBillSuccess()) {
                try {
                    this.updateInvestorUserBill(isOverdueRepay, investorLoginName, investRepayId, corpus + actualInterest + defaultInterest, actualFee);
                    investRepayJobResult.setUpdateInvestorUserBillSuccess(true);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Repay] Update investor user bill failed (investRepayId = {0})", String.valueOf(investRepayId)), e);
                }
            }

            if (investRepayJobResult.getInterestStatus() == SyncRequestStatus.SUCCESS && !investRepayJobResult.isUpdateInvestRepaySuccess()) {
                try {
                    this.updateInvestRepay(investId, investRepayId, actualInterest, actualFee, actualRepayDate);
                    investRepayJobResult.setUpdateInvestRepaySuccess(true);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Repay] Update invest repay is failed (investRepayId = {0})", String.valueOf(investRepayId)), e);
                }
            }

            if (investRepayJobResult.getFeeStatus() == SyncRequestStatus.READY) {
                try {
                    investRepayJobResult.setFeeStatus(SyncRequestStatus.SENT);
                    ProjectTransferRequestModel projectTransferRequestModel = ProjectTransferRequestModel.newRepayInvestFeeRequest(String.valueOf(loanId),
                            MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayId), String.valueOf(new Date().getTime())),
                            String.valueOf(actualFee));
                    ProjectTransferResponseModel responseModel = this.paySyncClient.send(ProjectTransferMapper.class, projectTransferRequestModel, ProjectTransferResponseModel.class);
                    investRepayJobResult.setFeeStatus(responseModel.isSuccess() ? SyncRequestStatus.SUCCESS : SyncRequestStatus.FAILURE);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Repay] Transfer invest fee is failed(investRepayId = {0})", String.valueOf(investRepayId)), e);
                }
            }

            if (investRepayJobResult.getFeeStatus() == SyncRequestStatus.SUCCESS && !investRepayJobResult.isUpdateSystemBillSuccess()) {
                try {
                    systemBillService.transferIn(investRepayId,
                            actualFee,
                            SystemBillBusinessType.INVEST_FEE,
                            MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), investorLoginName, String.valueOf(investRepayId)));
                    investRepayJobResult.setUpdateSystemBillSuccess(true);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Repay] Update system bill invest fee failed (investRepayId = {0})", String.valueOf(investRepayId)), e);
                }
            }
        }
    }

    protected void updateInvestRepay(long investId, long currentInvestRepayId, long actualInterest, long actualFee, Date actualRepayDate) {
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investId);
        final InvestRepayModel currentInvestRepay = investRepayMapper.findById(currentInvestRepayId);
        UnmodifiableIterator<InvestRepayModel> filteredInvestRepayModels = Iterators.filter(investRepayModels.iterator(), new Predicate<InvestRepayModel>() {
            @Override
            public boolean apply(InvestRepayModel input) {
                return input.getPeriod() < currentInvestRepay.getPeriod();
            }
        });

        while (filteredInvestRepayModels.hasNext()) {
            InvestRepayModel investRepayModel = filteredInvestRepayModels.next();
            investRepayModel.setStatus(RepayStatus.COMPLETE);
            investRepayModel.setActualRepayDate(investRepayModel.getActualRepayDate() == null ? actualRepayDate : investRepayModel.getActualRepayDate());
            investRepayMapper.update(investRepayModel);
        }

        currentInvestRepay.setActualInterest(actualInterest);
        currentInvestRepay.setActualFee(actualFee);
        currentInvestRepay.setActualRepayDate(actualRepayDate);
        currentInvestRepay.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(currentInvestRepay);
    }

    protected void updateInvestorUserBill(boolean isOverdueRepay, String investorLoginName, long investRepayId, long actualPaybackAmount, long actualFee) throws AmountTransferException {
        amountTransfer.transferInBalance(investorLoginName, investRepayId, actualPaybackAmount,
                isOverdueRepay ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.NORMAL_REPAY,
                null, null);
        amountTransfer.transferOutBalance(investorLoginName, investRepayId, actualFee, UserBillBusinessType.INVEST_FEE, null, null);
    }

    protected boolean storeJobData(LoanRepayJobResultDto loanRepayJobResultDto) {
        long loanRepayId = loanRepayJobResultDto.getLoanRepayId();
        try {
            String key = MessageFormat.format(LOAN_REPAY_JOB_DATA_KEY_TEMPLATE, String.valueOf(loanRepayId));
            String value = objectMapper.writeValueAsString(loanRepayJobResultDto);
            this.redisWrapperClient.set(key, value);
            logger.info(MessageFormat.format("[Normal Repay] repay job jobData : {0}", value));
            return true;
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Repay]Store repay data in redis is failed (loanRepayId={0})", String.valueOf(loanRepayId)));
        }
        return false;
    }
}