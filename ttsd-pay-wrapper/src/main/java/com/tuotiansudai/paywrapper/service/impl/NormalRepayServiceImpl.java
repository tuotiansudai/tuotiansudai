package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
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
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.paywrapper.service.NormalRepayService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.JobManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NormalRepayServiceImpl implements NormalRepayService {

    private static Logger logger = Logger.getLogger(NormalRepayServiceImpl.class);

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    private final static String REPAY_JOB_NAME_TEMPLATE = "NORMAL_REPAY_{0}_{1}";

    private final static String REPAY_REDIS_KEY_TEMPLATE = "NORMAL_REPAY:{0}";

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private SystemBillMapper systemBillMapper;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private LoanService loanService;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public boolean autoRepay(long loanRepayId) {
        logger.info(MessageFormat.format("auto repay (loanRepayId = {0}) starting... ", String.valueOf(loanRepayId)));

        LoanRepayModel loanRepayModel = loanRepayMapper.findById(loanRepayId);

        if (RepayStatus.REPAYING != loanRepayModel.getStatus()) {
            logger.error(MessageFormat.format("can not auto repay (loanRepayId = {0}), due to loan repay status is {1}", String.valueOf(loanRepayId), loanRepayModel.getStatus().name()));
            return false;
        }

        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());

        AccountModel accountModel = accountMapper.findByLoginName(loanModel.getAgentLoginName());
        if (!accountModel.isAutoRepay()) {
            logger.info(MessageFormat.format("can not auto repay (loanRepayId = {0}), because agent (loginName = {1}) is not open auto repay", String.valueOf(loanRepayId), loanModel.getAgentLoginName()));
            return false;
        }

        long loanId = loanRepayModel.getLoanId();
        long actualInterest = this.calculateLoanRepayActualInterest(loanId, loanRepayModel);
        long repayAmount = loanRepayModel.getCorpus() + actualInterest;
        if (accountModel.getBalance() < repayAmount) {
            logger.info(MessageFormat.format("can not auto repay (loanRepayId = {0}), because agent balance (balance = {1}) is not enough", String.valueOf(loanRepayId), String.valueOf(accountModel.getBalance())));
            return false;
        }

        loanRepayModel.setActualInterest(actualInterest);
        loanRepayModel.setActualRepayDate(new Date());
        loanRepayModel.setRepayAmount(repayAmount);
        loanRepayModel.setStatus(RepayStatus.WAIT_PAY);
        loanRepayMapper.update(loanRepayModel);

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newRepayNopwdRequest(
                String.valueOf(loanId),
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(loanRepayModel.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                String.valueOf(repayAmount));

        try {
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(ProjectTransferNopwdMapper.class, requestModel, ProjectTransferNopwdResponseModel.class);
            return responseModel.isSuccess();
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return false;
    }

    /**
     * 生成借款人还款form data
     *
     * @param loanId 标的id
     * @return form data
     */
    @Override
    public BaseDto<PayFormDataDto> generateRepayFormData(long loanId) {
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        LoanRepayModel enabledLoanRepay = loanRepayMapper.findEnabledLoanRepayByLoanId(loanId);
        if (enabledLoanRepay == null || enabledLoanRepay.getStatus() == RepayStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[Normal Repay] There is no enabled loan repay (loanId = {0})", String.valueOf(loanId)));
            return baseDto;
        }

        LoanModel loanModel = loanMapper.findById(loanId);

        Date actualRepayDate = new Date();
        long actualInterest = this.calculateLoanRepayActualInterest(loanId, enabledLoanRepay);
        long repayAmount = enabledLoanRepay.getCorpus() + actualInterest;

        logger.info(MessageFormat.format("[Normal Repay {0}] generate repay form data is {1} + {2}",
                String.valueOf(enabledLoanRepay.getId()), String.valueOf(enabledLoanRepay.getCorpus()), String.valueOf(actualInterest)));

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newRepayRequest(String.valueOf(loanId),
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(enabledLoanRepay.getId()), String.valueOf(actualRepayDate.getTime())),
                    accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId(),
                    String.valueOf(repayAmount),
                    false);
            baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("[Normal Repay {0}] generate loan repay form data is failed", String.valueOf(enabledLoanRepay.getId())), e);
            return baseDto;
        }

        enabledLoanRepay.setActualInterest(actualInterest);
        enabledLoanRepay.setActualRepayDate(actualRepayDate);
        enabledLoanRepay.setRepayAmount(repayAmount);
        enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
        loanRepayMapper.update(enabledLoanRepay);
        logger.info(MessageFormat.format("[Normal Repay {0}] generate repay form data to update loan repay status to WAIT_PAY", String.valueOf(enabledLoanRepay.getId())));

        return baseDto;
    }

    /**
     * 借款人还款, 联动优势回调处理
     *
     * @param paramsMap           请求参数Map
     * @param originalQueryString 原始请求参数
     * @return 回调响应数据
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String repayCallback(Map<String, String> paramsMap, String originalQueryString) throws Exception {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap,
                originalQueryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.error(MessageFormat.format("[Normal Repay] parse loan repay callback is failed (queryString = {0})", originalQueryString));
            return null;
        }

        long loanRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);

        if (!callbackRequest.isSuccess()) {
            logger.error(MessageFormat.format("[Normal Repay {0}] loan repay callback is not success", String.valueOf(loanRepayId)));
            return callbackRequest.getResponseData();
        }

        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);

        if (currentLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[Normal Repay {0}] loan repay callback status is not WAIT_PAY", String.valueOf(loanRepayId)));
            return callbackRequest.getResponseData();
        }

        // clear redis
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.del(redisKey);

        LoanModel loanModel = loanMapper.findById(currentLoanRepay.getLoanId());

        // update agent user bill
        UserBillBusinessType businessType = loanModel.getStatus() == LoanStatus.OVERDUE ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.NORMAL_REPAY;
        amountTransfer.transferOutBalance(loanModel.getAgentLoginName(), loanRepayId, currentLoanRepay.getRepayAmount(), businessType, null, null);
        logger.info(MessageFormat.format("[Normal Repay {0}] loan repay callback transfer out agent({1}) amount({2}) ",
                String.valueOf(loanRepayId), loanModel.getAgentLoginName(), String.valueOf(currentLoanRepay.getRepayAmount())));

        // update current loan repay status
        currentLoanRepay.setStatus(RepayStatus.COMPLETE);
        loanRepayMapper.update(currentLoanRepay);
        logger.info(MessageFormat.format("[Normal Repay {0}] loan repay callback update current loan repay status to COMPLETE", String.valueOf(loanRepayId)));

        // update all overdue loan repay status
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (RepayStatus.OVERDUE == loanRepayModel.getStatus()) {
                loanRepayModel.setStatus(RepayStatus.COMPLETE);
                loanRepayModel.setActualRepayDate(currentLoanRepay.getActualRepayDate());
                loanRepayMapper.update(loanRepayModel);
                logger.info(MessageFormat.format("[Normal Repay {0}] loan repay callback update overdue loan repay({1}) status to COMPLETE",
                        String.valueOf(loanRepayId), String.valueOf(loanRepayModel.getId())));
            }
        }

        // update invest repay actual interest fee status
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());
        for (InvestModel investModel : successInvests) {
            //投资人当期还款计划
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepay.getPeriod());
            //实际利息
            long actualInterest = this.calculateInvestRepayActualInterest(investModel.getId(), investRepayModel);
            //实际手续费
            long actualFee = this.calculateInvestRepayActualFee(investModel.getId(), investRepayModel);
            //实收金额
            long repayAmount = investRepayModel.getCorpus() + actualInterest - actualFee;

            investRepayModel.setActualInterest(actualInterest);
            investRepayModel.setActualFee(actualFee);
            investRepayModel.setRepayAmount(repayAmount);
            investRepayModel.setActualRepayDate(currentLoanRepay.getActualRepayDate());
            investRepayModel.setStatus(RepayStatus.WAIT_PAY);
            investRepayMapper.update(investRepayModel);
            logger.info(MessageFormat.format("[Normal Repay {0}] update invest repay({1}) corpus({2}) actual interest({3}) actual fee({4}) and status",
                    String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId()), String.valueOf(repayAmount), String.valueOf(actualInterest), String.valueOf(actualFee)));

            redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()), SyncRequestStatus.READY.name());

            logger.info(MessageFormat.format("[Normal Repay {0}] put invest repay id({1}) into redis READY",
                    String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId())));
        }

        redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.READY.name());
        logger.info(MessageFormat.format("[Normal Repay {0}] put loan repay id into redis READY", String.valueOf(loanRepayId)));

        // create payback invest job
        this.createRepayJob(loanRepayId, 2);

        return callbackRequest.getResponseData();
    }

    /**
     * 借款人还款后Job回调，返款投资人
     *
     * @param loanRepayId 标的还款计划id
     * @return 处理结果(true 所有投资人返款已成功发放)
     */
    @Override
    public boolean paybackInvest(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();

        //投资人实收利息总计
        long interestWithoutFee = 0;

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));

        for (InvestModel investModel : successInvests) {
            //投资人当期还款计划
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepay.getPeriod());

            interestWithoutFee += investRepayModel.getActualInterest() - investRepayModel.getActualFee();

            SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(investRepayModel.getId())));

            logger.info(MessageFormat.format("[Normal Repay {0}] invest payback({1}) redis status is {2} and payback amount is {3}",
                    String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId()), syncRequestStatus.name(), String.valueOf(currentLoanRepay.getRepayAmount())));

            if (Lists.newArrayList(SyncRequestStatus.READY, SyncRequestStatus.FAILURE).contains(syncRequestStatus)) {
                if (investRepayModel.getRepayAmount() > 0) {
                    // transfer investor interest(callback url: repay_payback_notify)
                    try {
                        ProjectTransferRequestModel repayPaybackRequest = ProjectTransferRequestModel.newRepayPaybackRequest(String.valueOf(loanId),
                                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayModel.getId()), String.valueOf(new Date().getTime())),
                                accountMapper.findByLoginName(investModel.getLoginName()).getPayUserId(),
                                String.valueOf(investRepayModel.getRepayAmount()));

                        redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()), SyncRequestStatus.SENT.name());
                        logger.info(MessageFormat.format("[Normal Repay {0}] invest payback({1}) send payback request",
                                String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId())));

                        ProjectTransferResponseModel repayPaybackResponse = this.paySyncClient.send(ProjectTransferMapper.class, repayPaybackRequest, ProjectTransferResponseModel.class);

                        redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()),
                                repayPaybackResponse.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                        logger.info(MessageFormat.format("[Normal Repay {0}] invest payback({1}) payback response is {2}",
                                String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId()), String.valueOf(repayPaybackResponse.isSuccess())));
                    } catch (PayException e) {
                        logger.error(MessageFormat.format("[Normal Repay {0}] invest payback({1}) payback throw exception",
                                String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId())), e);
                    }
                } else {
                    try {
                        this.processInvestRepay(loanRepayId, investRepayModel);
                        redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()), SyncRequestStatus.SUCCESS.name());
                    } catch (Exception e) {
                        redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()), SyncRequestStatus.FAILURE.name());
                        logger.error(MessageFormat.format("[Normal Repay {0}] invest payback({1}) payback throw exception",
                                String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId())), e);
                    }
                }
            }
        }

        //平台利息管理费
        long feeAmount = currentLoanRepay.getActualInterest() - interestWithoutFee;

        SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(loanRepayId)));
        logger.info(MessageFormat.format("[Normal Repay {0}] invest fee redis status is {1} total amount is {2}",
                String.valueOf(loanRepayId), syncRequestStatus.name(), String.valueOf(feeAmount)));

        if (Lists.newArrayList(SyncRequestStatus.READY, SyncRequestStatus.FAILURE).contains(syncRequestStatus)) {
            if (feeAmount > 0) {
                // transfer investor fee(callback url: repay_invest_fee_notify)
                try {
                    ProjectTransferRequestModel repayInvestFeeRequest = ProjectTransferRequestModel.newRepayInvestFeeRequest(String.valueOf(loanId),
                            MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(new Date().getTime())),
                            String.valueOf(feeAmount));

                    redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.SENT.name());
                    logger.info(MessageFormat.format("[Normal Repay {0}] invest fee send payback request", String.valueOf(loanRepayId)));

                    ProjectTransferResponseModel repayInvestFeeResponse = this.paySyncClient.send(ProjectTransferMapper.class, repayInvestFeeRequest, ProjectTransferResponseModel.class);

                    redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId),
                            repayInvestFeeResponse.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                    logger.info(MessageFormat.format("[Normal Repay {0}] invest fee payback response is {1}",
                            String.valueOf(loanRepayId), String.valueOf(repayInvestFeeResponse.isSuccess())));
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Normal Repay {0}] invest fee payback amount({1}) throw exception", String.valueOf(loanRepayId), String.valueOf(feeAmount)), e);
                }
            } else {
                redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.SUCCESS.name());
                logger.info(MessageFormat.format("[Advance Repay {0}] invest fee is 0 set redis status to SUCCESS", String.valueOf(loanRepayId)));
            }
        }

        if (this.isPaybackInvestSuccess(currentLoanRepay, successInvests)) {
            boolean isLastPeriod = loanRepayMapper.findLastLoanRepay(loanId).getPeriod() == currentLoanRepay.getPeriod();
            BaseDto<PayDataDto> dto = loanService.updateLoanStatus(loanId, isLastPeriod ? LoanStatus.COMPLETE : LoanStatus.REPAYING);
            logger.info(MessageFormat.format("[Normal Repay {0}] update loan({1}) status to {2} is {3}",
                    String.valueOf(loanRepayId), String.valueOf(loanId), (isLastPeriod ? LoanStatus.COMPLETE.name() : LoanStatus.REPAYING.name()), String.valueOf(dto.getData().getStatus())));
            return dto.getData().getStatus();
        }

        try {
            this.createRepayJob(loanRepayId, 60);
        } catch (SchedulerException e) {
            logger.error(MessageFormat.format("[Normal Repay {0}] create repay job failed", String.valueOf(loanRepayId)));
        }

        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString) throws Exception {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.error(MessageFormat.format("[Normal Repay] invest payback callback parse is failed (queryString = {0})", originalQueryString));
            return null;
        }

        long investRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
        InvestRepayModel currentInvestRepay = investRepayMapper.findById(investRepayId);

        LoanRepayModel currentLoanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(investMapper.findById(currentInvestRepay.getInvestId()).getLoanId(), currentInvestRepay.getPeriod());
        long loanRepayId = currentLoanRepayModel.getId();

        if (!callbackRequest.isSuccess()) {
            logger.error(MessageFormat.format("[Normal Repay {0}] invest payback({1}) callback is not success",
                    String.valueOf(loanRepayId), String.valueOf(investRepayId)));
            return callbackRequest.getResponseData();
        }

        if (currentInvestRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[Normal Repay {0}] invest payback({1}) status({2}) is not WAIT_PAY",
                    String.valueOf(loanRepayId), String.valueOf(investRepayId), currentInvestRepay.getStatus().name()));
            return callbackRequest.getResponseData();
        }

        this.processInvestRepay(loanRepayId, currentInvestRepay);
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.hset(redisKey, String.valueOf(investRepayId), SyncRequestStatus.SUCCESS.name());

        return callbackRequest.getResponseData();
    }

    @Override
    public String investFeeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);
        if (callbackRequest == null) {
            logger.error(MessageFormat.format("[Normal Repay] invest fee callback parse failed (queryString = {0})", originalQueryString));
            return null;
        }

        long loanRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);

        if (!callbackRequest.isSuccess()) {
            logger.error(MessageFormat.format("[Normal Repay {0}] invest fee callback is not success", String.valueOf(loanRepayId)));
            return callbackRequest.getResponseData();
        }

        if (systemBillMapper.findByOrderId(loanRepayId, SystemBillBusinessType.INVEST_FEE) != null) {
            logger.error(MessageFormat.format("[Normal Repay {0}] invest fee callback system bill is exist", String.valueOf(loanRepayId)));
            return callbackRequest.getResponseData();
        }

        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(currentLoanRepay.getLoanId());

        //投资人实收利息总和
        long interestWithoutFee = 0;
        for (InvestModel investModel : successInvests) {
            //投资人当期还款计划
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepay.getPeriod());
            interestWithoutFee += investRepayModel.getActualInterest() - investRepayModel.getActualFee();
        }

        //平台利息管理费总和
        long feeAmount = currentLoanRepay.getActualInterest() - interestWithoutFee;
        systemBillService.transferIn(loanRepayId,
                feeAmount,
                SystemBillBusinessType.INVEST_FEE,
                MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), String.valueOf(currentLoanRepay.getLoanId()), String.valueOf(loanRepayId)));
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.SUCCESS.name());

        logger.info(MessageFormat.format("[Normal Repay {0}] invest fee callback to update system bill amount({1})",
                String.valueOf(loanRepayId), String.valueOf(feeAmount)));

        return callbackRequest.getResponseData();
    }

    /**
     * 投资人收到返款处理
     *
     * @param currentInvestRepay 投资人还款计划
     * @throws AmountTransferException
     */
    private void processInvestRepay(long loanRepayId, InvestRepayModel currentInvestRepay) throws AmountTransferException {
        long investRepayId = currentInvestRepay.getId();
        InvestModel investModel = investMapper.findById(currentInvestRepay.getInvestId());

        // interest user bill
        long paybackAmount = currentInvestRepay.getCorpus() + currentInvestRepay.getActualInterest();
        amountTransfer.transferInBalance(investModel.getLoginName(),
                investRepayId,
                paybackAmount,
                currentInvestRepay.getActualRepayDate().before(currentInvestRepay.getRepayDate()) ? UserBillBusinessType.NORMAL_REPAY : UserBillBusinessType.OVERDUE_REPAY,
                null, null);

        logger.info(MessageFormat.format("[Normal Repay {0}] invest repay({1}) update user bill payback amount({2})",
                String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId()), String.valueOf(paybackAmount)));

        // fee user bill
        amountTransfer.transferOutBalance(investModel.getLoginName(),
                investRepayId,
                currentInvestRepay.getActualFee(),
                UserBillBusinessType.INVEST_FEE, null, null);

        logger.info(MessageFormat.format("[Normal Repay {0}] invest repay({1}) update user bill fee amount({2})",
                String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId()), String.valueOf(currentInvestRepay.getActualFee())));

        //update invest repay
        currentInvestRepay.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(currentInvestRepay);

        logger.info(MessageFormat.format("[Normal Repay {0}] invest repay({1}) update status to COMPLETE",
                String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId())));

        // update all overdue invest repay
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getStatus() == RepayStatus.OVERDUE) {
                investRepayModel.setStatus(RepayStatus.COMPLETE);
                investRepayModel.setActualRepayDate(currentInvestRepay.getActualRepayDate());
                investRepayMapper.update(investRepayModel);
                logger.info(MessageFormat.format("[Normal Repay {0}] invest repay({1}) update overdue invest repay({2}) status to COMPLETE",
                        String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId()), String.valueOf(investRepayModel.getId())));
            }
        }
    }

    private long calculateInvestRepayActualInterest(long investId, InvestRepayModel enabledInvestRepay) {
        if (enabledInvestRepay.getStatus() == RepayStatus.REPAYING) {
            return enabledInvestRepay.getExpectedInterest();
        }

        long actualInterest = 0;
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investId);
        for (InvestRepayModel investRepayModel : investRepayModels) {
            actualInterest += investRepayModel.getStatus() == RepayStatus.OVERDUE ? investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() : 0;
        }
        return actualInterest;
    }

    private long calculateInvestRepayActualFee(long investId, InvestRepayModel enabledInvestRepay) {
        if (enabledInvestRepay.getStatus() == RepayStatus.REPAYING) {
            return enabledInvestRepay.getExpectedFee();
        }

        long actualFee = 0;
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investId);
        for (InvestRepayModel investRepayModel : investRepayModels) {
            actualFee += investRepayModel.getStatus() == RepayStatus.OVERDUE ? investRepayModel.getExpectedFee() : 0;
        }
        return actualFee;
    }

    protected long calculateLoanRepayActualInterest(long loanId, LoanRepayModel enabledLoanRepay) {
        if (enabledLoanRepay.getStatus() == RepayStatus.REPAYING) {
            return enabledLoanRepay.getExpectedInterest();
        }

        long actualInterest = 0;
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            actualInterest += loanRepayModel.getStatus() == RepayStatus.OVERDUE ? loanRepayModel.getExpectedInterest() + loanRepayModel.getDefaultInterest() : 0;
        }
        return actualInterest;
    }

    private void createRepayJob(long loanRepayId, int delayMinutes) throws SchedulerException {
        Date fewMinutesLater = new DateTime().plusMinutes(delayMinutes).toDate();
        jobManager.newJob(JobType.NormalRepay, NormalRepayJob.class)
                .runOnceAt(fewMinutesLater)
                .addJobData(NormalRepayJob.LOAN_REPAY_ID, loanRepayId)
                .withIdentity(JobType.NormalRepay.name(), MessageFormat.format(REPAY_JOB_NAME_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(fewMinutesLater.getTime())))
                .submit();
        logger.info(MessageFormat.format("[Normal Repay {0}] create invest payback job, start at {1}", String.valueOf(loanRepayId), fewMinutesLater.toString()));
    }

    private boolean isPaybackInvestSuccess(LoanRepayModel currentLoanRepayModel, List<InvestModel> successInvests) {
        logger.info(MessageFormat.format("[Normal Repay {0}] invest payback status summary", String.valueOf(currentLoanRepayModel.getId())));
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(currentLoanRepayModel.getId()));
        boolean isSuccess = true;
        for (InvestModel investModel : successInvests) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepayModel.getPeriod());
            SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(investRepayModel.getId())));
            logger.info(MessageFormat.format("[Normal Repay {0}] invest payback({1}) redis status is {2}",
                    String.valueOf(currentLoanRepayModel.getId()), String.valueOf(investRepayModel.getId()), syncRequestStatus.name()));
            if (syncRequestStatus == SyncRequestStatus.FAILURE) {
                isSuccess = false;
            }
        }

        SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(currentLoanRepayModel.getId())));
        logger.info(MessageFormat.format("[Normal Repay {0}] invest fee redis status is {2}", String.valueOf(currentLoanRepayModel.getId()), syncRequestStatus.name()));
        if (syncRequestStatus == SyncRequestStatus.FAILURE) {
            isSuccess = false;
        }

        return isSuccess;
    }
}