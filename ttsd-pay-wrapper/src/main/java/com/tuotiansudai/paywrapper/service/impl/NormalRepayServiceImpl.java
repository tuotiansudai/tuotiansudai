package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.loanout.LoanService;
import com.tuotiansudai.paywrapper.repository.mapper.NormalRepayNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.NormalRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.NormalRepayService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final static String REPAY_REDIS_KEY_TEMPLATE = "NORMAL_REPAY:{0}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private SystemBillMapper systemBillMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private LoanService loanService;

    @Autowired
    private NormalRepayNotifyMapper normalRepayNotifyMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    @Override
    public boolean autoRepay(long loanRepayId) {
        logger.info(MessageFormat.format("auto repay (loanRepayId = {0}) starting... ", String.valueOf(loanRepayId)));

        LoanRepayModel loanRepayModel = loanRepayMapper.findById(loanRepayId);

        if (RepayStatus.REPAYING != loanRepayModel.getStatus()) {
            logger.warn(MessageFormat.format("can not auto repay (loanRepayId = {0}), due to loan repay status is {1}", String.valueOf(loanRepayId), loanRepayModel.getStatus().name()));
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
            baseDto.getData().setMessage("该标的今天没有待还款，或还款等待支付，请半小时后重试");
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
            baseDto.getData().setMessage("请求数据失败");
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
            logger.warn(MessageFormat.format("[Normal Repay {0}] loan repay callback status is {1}", String.valueOf(loanRepayId), currentLoanRepay.getStatus()));
            return callbackRequest.getResponseData();
        }

        // clear redis
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.del(redisKey);

        LoanModel loanModel = loanMapper.findById(currentLoanRepay.getLoanId());

        // update agent user bill
        UserBillBusinessType businessType = loanModel.getStatus() == LoanStatus.OVERDUE ? UserBillBusinessType.OVERDUE_REPAY : UserBillBusinessType.NORMAL_REPAY;
        AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, loanModel.getAgentLoginName(), loanRepayId, currentLoanRepay.getRepayAmount(), businessType, null, null);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
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
            //出借人当期还款计划
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepay.getPeriod());
            if (RepayStatus.COMPLETE == investRepayModel.getStatus()) {
                logger.info(String.format("[Normal Repay %s] investRepay %s  status is COMPLETE", String.valueOf(currentLoanRepay.getRepayAmount()), String.valueOf(investRepayModel.getId())));
                continue;
            }
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

        mqWrapperClient.publishMessage(MessageTopic.RepaySuccess, new RepaySuccessMessage(loanRepayId, false));
        logger.info(MessageFormat.format("[[Normal Repay {0}]: 正常还款成功,发送MQ消息", String.valueOf(loanRepayId)));

        return callbackRequest.getResponseData();
    }

    /**
     * 借款人还款后Job回调，返款出借人
     *
     * @param loanRepayId 标的还款计划id
     * @return 处理结果(true 所有出借人返款已成功发放)
     */
    @Override
    public boolean paybackInvest(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        //出借人实收利息总计
        long interestWithoutFee = 0;

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));

        for (InvestModel investModel : successInvests) {
            //出借人当期还款计划
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepay.getPeriod());

            interestWithoutFee += investRepayModel.getActualInterest() - investRepayModel.getActualFee();

            if (RepayStatus.COMPLETE == investRepayModel.getStatus()) {
                logger.info(String.format("[Normal Repay %s] investRepay %s  status is COMPLETE", String.valueOf(currentLoanRepay.getRepayAmount()), String.valueOf(investRepayModel.getId())));
                continue;
            }

            SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(investRepayModel.getId())));

            logger.info(MessageFormat.format("[Normal Repay {0}] invest payback({1}) redis status is {2} and payback amount is {3}",
                    String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId()), syncRequestStatus.name(), String.valueOf(currentLoanRepay.getRepayAmount())));

            if (Lists.newArrayList(SyncRequestStatus.READY, SyncRequestStatus.FAILURE).contains(syncRequestStatus)) {
                if (investRepayModel.getRepayAmount() > 0) {
                    try {
                        ProjectTransferRequestModel repayPaybackRequest = ProjectTransferRequestModel.newNormalRepayPaybackRequest(String.valueOf(loanId),
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
                try {
                    ProjectTransferRequestModel repayInvestFeeRequest = ProjectTransferRequestModel.newNormalRepayInvestFeeRequest(String.valueOf(loanId),
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
                logger.info(MessageFormat.format("[Normal Repay {0}] invest fee is 0 set redis status to SUCCESS", String.valueOf(loanRepayId)));
            }
        }

        if (this.isPaybackInvestSuccess(currentLoanRepay, successInvests)) {
            boolean isLastPeriod = loanRepayMapper.findLastLoanRepay(loanId).getPeriod() == currentLoanRepay.getPeriod();
            BaseDto<PayDataDto> dto = loanService.updateLoanStatus(loanId, isLastPeriod ? LoanStatus.COMPLETE : LoanStatus.REPAYING);
            logger.info(MessageFormat.format("[Normal Repay {0}] update loan({1}) status to {2} is {3}",
                    String.valueOf(loanRepayId), String.valueOf(loanId), (isLastPeriod ? LoanStatus.COMPLETE.name() : LoanStatus.REPAYING.name()), String.valueOf(dto.getData().getStatus())));
            return dto.getData().getStatus();
        }

        return false;
    }

    @Override
    public String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString) throws Exception {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                NormalRepayNotifyMapper.class,
                NormalRepayNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.error(MessageFormat.format("[Normal Repay] invest payback callback parse is failed (queryString = {0})", originalQueryString));
            return null;
        }

        mqWrapperClient.sendMessage(MessageQueue.RepaySuccessInvestRepayCallback, new RepaySuccessAsyncCallBackMessage(callbackRequest.getId(), false));
        logger.info(MessageFormat.format("[Normal Repay] 正常还款发放出借人收益回调消息发送成功,notifyRequestId:{0}", String.valueOf(callbackRequest.getId())));
        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncNormalRepayPaybackCallback(long notifyRequestId) {
        NormalRepayNotifyRequestModel model = normalRepayNotifyMapper.findById(notifyRequestId);
        if (updateNormalRepayNotifyRequestStatus(model)) {
            try {
                if (!this.processOneNormalRepayPaybackCallback(model)) {
                    fatalLog("normal repay callback, processOneNormalRepayPaybackCallback fail. investRepayId:" + model.getOrderId(), null);
                }
            } catch (Exception e) {
                fatalLog("normal repay callback, processOneNormalRepayPaybackCallback error. investRepayId:" + model.getOrderId(), e);
            }
        }

        BaseDto<PayDataDto> asyncNormalRepayNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncNormalRepayNotifyDto.setData(baseDataDto);

        return asyncNormalRepayNotifyDto;
    }

    private boolean updateNormalRepayNotifyRequestStatus(NormalRepayNotifyRequestModel model) {
        try {
            normalRepayNotifyMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
        } catch (Exception e) {
            fatalLog("update_normal_repay_notify_status_fail, orderId:" + model.getOrderId() + ",id:" + model.getId(), e);
            return false;
        }
        return true;
    }

    private boolean processOneNormalRepayPaybackCallback(NormalRepayNotifyRequestModel callbackRequestModel) {
        long investRepayId = Long.parseLong(callbackRequestModel.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
        InvestRepayModel currentInvestRepay = investRepayMapper.findById(investRepayId);

        LoanRepayModel currentLoanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(investMapper.findById(currentInvestRepay.getInvestId()).getLoanId(), currentInvestRepay.getPeriod());
        long loanRepayId = currentLoanRepayModel.getId();

        if (!callbackRequestModel.isSuccess()) {
            logger.error(MessageFormat.format("[Normal Repay {0}] invest payback({1}) callback is not success",
                    String.valueOf(loanRepayId), String.valueOf(investRepayId)));
            return false;
        }

        if (currentInvestRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.warn(MessageFormat.format("[Normal Repay {0}] invest payback({1}) status({2}) is not WAIT_PAY",
                    String.valueOf(loanRepayId), String.valueOf(investRepayId), currentInvestRepay.getStatus().name()));
            return true;
        }

        try {
            this.processInvestRepay(loanRepayId, currentInvestRepay);
        } catch (AmountTransferException e) {
            logger.error(MessageFormat.format("[Normal Repay {0}] processInvestRepay fail ({1}) status({2})",
                    String.valueOf(loanRepayId), String.valueOf(investRepayId), currentInvestRepay.getStatus().name()));
            return false;
        }

        return true;
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
            logger.warn(MessageFormat.format("[Normal Repay {0}] invest fee callback system bill is exist", String.valueOf(loanRepayId)));
            return callbackRequest.getResponseData();
        }

        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);

        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(currentLoanRepay.getLoanId());

        //出借人实收利息总和
        long interestWithoutFee = 0;
        for (InvestModel investModel : successInvests) {
            //出借人当期还款计划
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepay.getPeriod());
            interestWithoutFee += investRepayModel.getActualInterest() - investRepayModel.getActualFee();
        }

        //平台利息管理费总和
        long feeAmount = currentLoanRepay.getActualInterest() - interestWithoutFee;

        SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_IN,
                loanRepayId,
                feeAmount,
                SystemBillBusinessType.INVEST_FEE,
                MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), String.valueOf(currentLoanRepay.getLoanId()), String.valueOf(loanRepayId)));
        mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.SUCCESS.name());

        logger.info(MessageFormat.format("[Normal Repay {0}] invest fee callback to update system bill amount({1})",
                String.valueOf(loanRepayId), String.valueOf(feeAmount)));

        return callbackRequest.getResponseData();
    }

    /**
     * 出借人收到返款处理
     *
     * @param currentInvestRepay 出借人还款计划
     * @throws AmountTransferException
     */
    private void processInvestRepay(long loanRepayId, InvestRepayModel currentInvestRepay) throws AmountTransferException {
        long investRepayId = currentInvestRepay.getId();
        InvestModel investModel = investMapper.findById(currentInvestRepay.getInvestId());
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        // interest user bill
        long paybackAmount = currentInvestRepay.getCorpus() + currentInvestRepay.getActualInterest();
        AmountTransferMessage inAtm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, investModel.getLoginName(),
                investRepayId,
                paybackAmount,
                currentInvestRepay.getActualRepayDate().before(currentInvestRepay.getRepayDate()) ? UserBillBusinessType.NORMAL_REPAY : UserBillBusinessType.OVERDUE_REPAY,
                null, null);

        // fee user bill
        AmountTransferMessage outAtm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, investModel.getLoginName(),
                investRepayId,
                currentInvestRepay.getActualFee(),
                UserBillBusinessType.INVEST_FEE, null, null);

        inAtm.setNext(outAtm);

        logger.info(MessageFormat.format("[Normal Repay {0}] send message to update user account. invest repay({1}), payback amount({2}), fee amount({3})",
                String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId()), String.valueOf(paybackAmount), String.valueOf(currentInvestRepay.getActualFee())));

        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, inAtm);

        //update invest repay
        currentInvestRepay.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(currentInvestRepay);

        logger.info(MessageFormat.format("[Normal Repay {0}] invest repay({1}) update status to COMPLETE",
                String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId())));

        // update all overdue invest repay
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
        investRepayModels.stream().filter(investRepayModel -> investRepayModel.getStatus() == RepayStatus.OVERDUE).forEach(investRepayModel -> {
            investRepayModel.setStatus(RepayStatus.COMPLETE);
            investRepayModel.setActualRepayDate(currentInvestRepay.getActualRepayDate());
            investRepayMapper.update(investRepayModel);
            logger.info(MessageFormat.format("[Normal Repay {0}] invest repay({1}) update overdue invest repay({2}) status to COMPLETE",
                    String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId()), String.valueOf(investRepayModel.getId())));
        });

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.hset(redisKey, String.valueOf(investRepayId), SyncRequestStatus.SUCCESS.name());

        //Title:您出借的{0}已回款{1}元，请前往账户查收！
        //Content:尊敬的用户，您出借的{0}项目已回款，期待已久的收益已奔向您的账户，快来查看吧。
        long repayAmount = currentInvestRepay.getRepayAmount();
        CouponRepayModel couponRepayModel = couponRepayMapper.findCouponRepayByInvestIdAndPeriod(currentInvestRepay.getInvestId(), currentInvestRepay.getPeriod());
        if (couponRepayModel != null) {
            repayAmount += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
        }
        if (currentInvestRepay.getPeriod() == loanModel.getPeriods()) {
            InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(currentInvestRepay.getInvestId());
            if (investExtraRateModel != null) {
                repayAmount += investExtraRateModel.getExpectedInterest() - investExtraRateModel.getExpectedFee();
            }
        }
        String title = MessageFormat.format(MessageEventType.REPAY_SUCCESS.getTitleTemplate(), loanModel.getName(), AmountConverter.convertCentToString(repayAmount));
        String content = MessageFormat.format(MessageEventType.REPAY_SUCCESS.getContentTemplate(), loanModel.getName());
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.REPAY_SUCCESS,
                Lists.newArrayList(investModel.getLoginName()), title, content, investRepayId));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(investModel.getLoginName()), PushSource.ALL, PushType.REPAY_SUCCESS, title, AppUrl.MESSAGE_CENTER_LIST));

        mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(investModel.getLoginName(), WeChatMessageType.NORMAL_REPAY_SUCCESS, currentInvestRepay.getId()));
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
        logger.info(MessageFormat.format("[Normal Repay {0}] invest fee redis status is {1}", String.valueOf(currentLoanRepayModel.getId()), syncRequestStatus.name()));
        if (syncRequestStatus == SyncRequestStatus.FAILURE) {
            isSuccess = false;
        }

        return isSuccess;
    }

    private void fatalLog(String errMsg, Throwable e) {
        logger.fatal(errMsg, e);
        sendSmsErrNotify(MessageFormat.format("{0},{1}", environment, errMsg));
    }

    private void sendSmsErrNotify(String errMsg) {
        logger.info("sent normal repay fatal sms message");
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("正常还款业务错误。详细信息：{0}", errMsg));
    }


}