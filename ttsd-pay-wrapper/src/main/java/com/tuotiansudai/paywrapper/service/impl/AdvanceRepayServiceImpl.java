package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.loanout.LoanService;
import com.tuotiansudai.paywrapper.repository.mapper.AdvanceRepayNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.AdvanceRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.AdvanceRepayService;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdvanceRepayServiceImpl implements AdvanceRepayService {

    private static Logger logger = Logger.getLogger(AdvanceRepayServiceImpl.class);

    protected final static String REPAY_ORDER_ID_SEPARATOR = "X";

    protected final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    private final static String REPAY_REDIS_KEY_TEMPLATE = "ADVANCE_REPAY:{0}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

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
    protected CouponRepayMapper couponRepayMapper;

    @Autowired
    protected AdvanceRepayNotifyMapper advanceRepayNotifyMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

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

        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel.getStatus() != LoanStatus.REPAYING) {
            logger.error(MessageFormat.format("[Advance Repay] loan({0}) status({1}) is not REPAYING", String.valueOf(loanId), loanModel.getStatus().name()));
            return baseDto;
        }

        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanId);

        Optional<LoanRepayModel> enabledLoanRepayOptional = loanRepayModels.stream().filter(loanRepayModel -> loanRepayModel.getStatus() == RepayStatus.REPAYING).findFirst();

        if (!enabledLoanRepayOptional.isPresent()) {
            logger.error(MessageFormat.format("[Advance Repay] There is no enabled loan repay (loanId = {0})", String.valueOf(loanId)));
            return baseDto;
        }

        LoanRepayModel enabledLoanRepay = enabledLoanRepayOptional.get();

        DateTime currentRepayDate = new DateTime();
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels);
        long actualInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvests, lastRepayDate, currentRepayDate);
        long corpus = loanRepayMapper.findLastLoanRepay(loanId).getCorpus();
        long repayAmount = corpus + actualInterest;

        logger.info(MessageFormat.format("[Advance Repay {0}] generate repay form data is {1} + {2}",
                String.valueOf(enabledLoanRepay.getId()), String.valueOf(corpus), String.valueOf(actualInterest)));

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newRepayRequest(String.valueOf(loanId),
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(enabledLoanRepay.getId()), String.valueOf(currentRepayDate.getMillis())),
                    accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId(),
                    String.valueOf(repayAmount),
                    true);
            baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("[Advance Repay {0}] generate loan repay form data is failed", String.valueOf(enabledLoanRepay.getId())), e);
            return baseDto;
        }

        enabledLoanRepay.setActualInterest(actualInterest);
        enabledLoanRepay.setRepayAmount(repayAmount);
        enabledLoanRepay.setActualRepayDate(currentRepayDate.toDate());
        enabledLoanRepay.setStatus(RepayStatus.WAIT_PAY);
        loanRepayMapper.update(enabledLoanRepay);

        logger.info(MessageFormat.format("[Advance Repay {0}] generate repay form data to update loan repay status to WAIT_PAY", String.valueOf(enabledLoanRepay.getId())));

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
            logger.error(MessageFormat.format("[Advance Repay] parse Loan repay callback is failed (queryString = {0})", originalQueryString));
            return null;
        }

        long loanRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);

        if (!callbackRequest.isSuccess()) {
            logger.error(MessageFormat.format("[Advance Repay {0}] loan repay callback is not success", callbackRequest.getOrderId()));
            return callbackRequest.getResponseData();
        }

        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);

        if (currentLoanRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[Advance Repay {0}] loan repay callback status is {1}", String.valueOf(loanRepayId), currentLoanRepay.getStatus()));
            return callbackRequest.getResponseData();
        }

        // clear redis
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.del(redisKey);

        LoanModel loanModel = loanMapper.findById(currentLoanRepay.getLoanId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(loanModel.getId());
        DateTime currentRepayDate = new DateTime(currentLoanRepay.getActualRepayDate());
        DateTime lastRepayDate = InterestCalculator.getLastSuccessRepayDate(loanModel, loanRepayModels);

        // update agent user bill
        AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, loanModel.getAgentLoginName(), loanRepayId, currentLoanRepay.getRepayAmount(), UserBillBusinessType.ADVANCE_REPAY, null, null);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
        logger.info(MessageFormat.format("[Advance Repay {0}] loan repay callback transfer out agent({1}) amount({2}) ",
                String.valueOf(loanRepayId), loanModel.getAgentLoginName(), String.valueOf(currentLoanRepay.getRepayAmount())));

        // update other loan repay status
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getStatus() != RepayStatus.COMPLETE) {
                loanRepayModel.setStatus(RepayStatus.COMPLETE);
                loanRepayModel.setActualRepayDate(currentRepayDate.toDate());
                loanRepayMapper.update(loanRepayModel);
                logger.info(MessageFormat.format("[Advance Repay {0}] loan repay callback update loan repay({1}) status to COMPLETE",
                        String.valueOf(loanRepayId), String.valueOf(loanRepayModel.getId())));
            }
        }

        // update invest repay actual interest fee status
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        for (InvestModel investModel : successInvests) {
            //投资人当期还款计划
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepay.getPeriod());
            if (RepayStatus.COMPLETE == investRepayModel.getStatus()) {
                logger.info(String.format("[Normal Repay %s] investRepay %s  status is COMPLETE", String.valueOf(currentLoanRepay.getRepayAmount()), String.valueOf(investRepayModel.getId())));
                continue;
            }
            //实际利息
            InvestModel transferInvestModel = investMapper.findById(investModel.getTransferInvestId());
            long actualInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, investModel.getAmount(),
                    transferInvestModel == null ? investModel.getTradingTime() : transferInvestModel.getTradingTime(), lastRepayDate, currentRepayDate);
            //实际手续费
            long actualFee = new BigDecimal(actualInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investModel.getInvestFeeRate())).longValue();
            //实收金额
            long repayAmount = investModel.getAmount() + actualInterest - actualFee;

            investRepayModel.setActualInterest(actualInterest);
            investRepayModel.setActualFee(actualFee);
            investRepayModel.setRepayAmount(repayAmount);
            investRepayModel.setActualRepayDate(currentRepayDate.toDate());
            investRepayModel.setStatus(RepayStatus.WAIT_PAY);
            investRepayMapper.update(investRepayModel);
            logger.info(MessageFormat.format("[Advance Repay {0}] update invest repay({1}) actual interest({2}) actual fee({3}) and status",
                    String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId()), String.valueOf(actualInterest), String.valueOf(actualFee)));

            redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()), SyncRequestStatus.READY.name());

            logger.info(MessageFormat.format("[Advance Repay {0}] put invest repay id({1}) into redis READY",
                    String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId())));
        }

        redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.READY.name());
        logger.info(MessageFormat.format("[Advance Repay {0}] put loan repay id into redis READY", String.valueOf(loanRepayId)));

        mqWrapperClient.publishMessage(MessageTopic.RepaySuccess, new RepaySuccessMessage(loanRepayId, true));
        logger.info(MessageFormat.format("[[Advance Repay {0}]: 提前还款成功,发送MQ消息", String.valueOf(loanRepayId)));

        return callbackRequest.getResponseData();
    }

    /**
     * 借款人还款后，返款投资人
     *
     * @param loanRepayId 标的还款计划id
     * @return 处理结果(true 所有投资人返款已成功发放)
     */
    @Override
    @Transactional
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

            if (RepayStatus.COMPLETE == investRepayModel.getStatus()) {
                logger.info(String.format("[Advance Repay %s] investRepay %s  status is COMPLETE", String.valueOf(currentLoanRepay.getRepayAmount()), String.valueOf(investRepayModel.getId())));
                continue;
            }
            interestWithoutFee += investRepayModel.getActualInterest() - investRepayModel.getActualFee();

            SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(investRepayModel.getId())));

            logger.info(MessageFormat.format("[Advance Repay {0}] invest payback({1}) redis status is {2} and payback amount is {3}",
                    String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId()), syncRequestStatus.name(), String.valueOf(investRepayModel.getRepayAmount())));

            if (Lists.newArrayList(SyncRequestStatus.READY, SyncRequestStatus.FAILURE).contains(syncRequestStatus)) {
                if (investRepayModel.getRepayAmount() > 0) {
                    try {
                        ProjectTransferRequestModel repayPaybackRequest = ProjectTransferRequestModel.newAdvanceRepayPaybackRequest(String.valueOf(loanId),
                                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investRepayModel.getId()), String.valueOf(new Date().getTime())),
                                accountMapper.findByLoginName(investModel.getLoginName()).getPayUserId(),
                                String.valueOf(investRepayModel.getRepayAmount()));

                        redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()), SyncRequestStatus.SENT.name());
                        logger.info(MessageFormat.format("[Advance Repay {0}] invest payback({1}) send payback request",
                                String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId())));

                        ProjectTransferResponseModel repayPaybackResponse = this.paySyncClient.send(ProjectTransferMapper.class, repayPaybackRequest, ProjectTransferResponseModel.class);

                        redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()),
                                repayPaybackResponse.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                        logger.info(MessageFormat.format("[Advance Repay {0}] invest payback({1}) payback response is {2}",
                                String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId()), String.valueOf(repayPaybackResponse.isSuccess())));
                    } catch (PayException e) {
                        logger.error(MessageFormat.format("[Advance Repay {0}] invest payback({1}) payback throw exception",
                                String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId())), e);
                    }

                } else {
                    try {
                        this.processInvestRepay(loanRepayId, investRepayModel);
                    } catch (Exception e) {
                        redisWrapperClient.hset(redisKey, String.valueOf(investRepayModel.getId()), SyncRequestStatus.FAILURE.name());
                        logger.error(MessageFormat.format("[Advance Repay {0}] invest payback({1}) payback throw exception",
                                String.valueOf(loanRepayId), String.valueOf(investRepayModel.getId())), e);
                    }
                }

            }
        }

        //平台利息管理费
        long feeAmount = currentLoanRepay.getActualInterest() - interestWithoutFee;

        SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(loanRepayId)));
        logger.info(MessageFormat.format("[Advance Repay {0}] invest fee redis status is {1} total amount is {2}",
                String.valueOf(loanRepayId), syncRequestStatus.name(), String.valueOf(feeAmount)));
        if (Lists.newArrayList(SyncRequestStatus.READY, SyncRequestStatus.FAILURE).contains(syncRequestStatus)) {
            if (feeAmount > 0) {
                try {
                    ProjectTransferRequestModel repayInvestFeeRequest = ProjectTransferRequestModel.newAdvanceRepayInvestFeeRequest(String.valueOf(loanId),
                            MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(loanRepayId), String.valueOf(new Date().getTime())),
                            String.valueOf(feeAmount));

                    redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.SENT.name());
                    logger.info(MessageFormat.format("[Advance Repay {0}] invest fee send payback request", String.valueOf(loanRepayId)));

                    ProjectTransferResponseModel repayInvestFeeResponse = this.paySyncClient.send(ProjectTransferMapper.class, repayInvestFeeRequest, ProjectTransferResponseModel.class);

                    redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId),
                            repayInvestFeeResponse.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                    logger.info(MessageFormat.format("[Advance Repay {0}] invest fee payback response is {1}",
                            String.valueOf(loanRepayId), String.valueOf(repayInvestFeeResponse.isSuccess())));
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Advance Repay {0}] invest fee payback amount({1}) throw exception", String.valueOf(loanRepayId), String.valueOf(feeAmount)), e);
                }
            } else {
                redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.SUCCESS.name());
                logger.info(MessageFormat.format("[Advance Repay {0}] invest fee is 0 set redis status to SUCCESS", String.valueOf(loanRepayId)));
            }
        }

        if (this.isPaybackInvestSuccess(currentLoanRepay, successInvests)) {
            BaseDto<PayDataDto> dto = loanService.updateLoanStatus(loanId, LoanStatus.COMPLETE);
            logger.info(MessageFormat.format("[Advance Repay {0}] update loan({1}) status to COMPLETE is {2}",
                    String.valueOf(loanRepayId), String.valueOf(loanId), String.valueOf(dto.getData().getStatus())));
            return dto.getData().getStatus();
        }

        return false;
    }

    @Override
    public String investPaybackCallback(Map<String, String> paramsMap, String originalQueryString) throws Exception {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                AdvanceRepayNotifyMapper.class,
                AdvanceRepayNotifyRequestModel.class);

        if (callbackRequest == null) {
            logger.error(MessageFormat.format("[Advance Repay] invest payback callback parse is failed (queryString = {0})", originalQueryString));
            return null;
        }

        mqWrapperClient.sendMessage(MessageQueue.RepaySuccessInvestRepayCallback, new RepaySuccessAsyncCallBackMessage(callbackRequest.getId(), true));
        logger.info(MessageFormat.format("[Advance Repay] 提前还款发放投资人收益回调消息发送成功,notifyRequestId:{0}", String.valueOf(callbackRequest.getId())));
        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncAdvanceRepayPaybackCallback(long notifyRequestId) {
        AdvanceRepayNotifyRequestModel model = advanceRepayNotifyMapper.findById(notifyRequestId);
        if (updateAdvanceRepayNotifyRequestStatus(model)) {
            try {
                if (!this.processOneInvestPaybackCallback(model)) {
                    fatalLog("advance repay callback, processOneInvestPaybackCallback fail. investRepayId:" + model.getOrderId(), null);
                }
            } catch (Exception e) {
                fatalLog("advance repay callback, processOneInvestPaybackCallback error. investRepayId:" + model.getOrderId(), e);
            }
        }

        BaseDto<PayDataDto> asyncAdvanceRepayNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncAdvanceRepayNotifyDto.setData(baseDataDto);

        return asyncAdvanceRepayNotifyDto;
    }

    private boolean updateAdvanceRepayNotifyRequestStatus(AdvanceRepayNotifyRequestModel model) {
        try {
            advanceRepayNotifyMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
        } catch (Exception e) {
            fatalLog("update_advance_repay_notify_status_fail, orderId:" + model.getOrderId() + ",id:" + model.getId(), null);
            return false;
        }
        return true;
    }

    private boolean processOneInvestPaybackCallback(AdvanceRepayNotifyRequestModel callbackRequestModel) {

        long investRepayId = Long.parseLong(callbackRequestModel.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);
        InvestRepayModel currentInvestRepay = investRepayMapper.findById(investRepayId);

        LoanRepayModel currentLoanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(investMapper.findById(currentInvestRepay.getInvestId()).getLoanId(), currentInvestRepay.getPeriod());
        long loanRepayId = currentLoanRepayModel.getId();
        if (!callbackRequestModel.isSuccess()) {
            logger.error(MessageFormat.format("[Advance Repay {0}] invest payback({1}) callback is not success",
                    String.valueOf(loanRepayId), String.valueOf(investRepayId)));
            return false;
        }

        if (currentInvestRepay.getStatus() != RepayStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[Advance Repay {0}] invest payback({1}) status({2}) is not WAIT_PAY",
                    String.valueOf(loanRepayId), String.valueOf(investRepayId), currentInvestRepay.getStatus().name()));
            return false;
        }

        try {
            this.processInvestRepay(loanRepayId, currentInvestRepay);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Advance Repay {0}] processInvestRepay fail ({1}) status({2})",
                    String.valueOf(loanRepayId), String.valueOf(investRepayId), currentInvestRepay.getStatus().name()));
            return false;
        }
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.hset(redisKey, String.valueOf(investRepayId), SyncRequestStatus.SUCCESS.name());
        return true;
    }


    @Override
    public String investFeeCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);
        if (callbackRequest == null) {
            logger.error(MessageFormat.format("[Advance Repay] invest fee callback parse is failed (queryString = {0})", originalQueryString));
            return null;
        }

        long loanRepayId = Long.parseLong(callbackRequest.getOrderId().split(REPAY_ORDER_ID_SEPARATOR)[0]);

        if (!callbackRequest.isSuccess()) {
            logger.error(MessageFormat.format("[Advance Repay {0}] invest fee callback is not success", String.valueOf(loanRepayId)));
            return callbackRequest.getResponseData();
        }

        if (systemBillMapper.findByOrderId(loanRepayId, SystemBillBusinessType.INVEST_FEE) != null) {
            logger.error(MessageFormat.format("[Advance Repay {0}] invest fee callback system bill is exist", String.valueOf(loanRepayId)));
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

        SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_IN,
                loanRepayId,
                feeAmount,
                SystemBillBusinessType.INVEST_FEE,
                MessageFormat.format(SystemBillDetailTemplate.INVEST_FEE_DETAIL_TEMPLATE.getTemplate(), String.valueOf(currentLoanRepay.getLoanId()), String.valueOf(loanRepayId)));
        mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);

        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        redisWrapperClient.hset(redisKey, String.valueOf(loanRepayId), SyncRequestStatus.SUCCESS.name());

        logger.info(MessageFormat.format("[Advance Repay {0}] invest fee callback to update system bill amount({1})", String.valueOf(loanRepayId), String.valueOf(feeAmount)));

        return callbackRequest.getResponseData();
    }

    /**
     * 投资人收到返款处理
     *
     * @param currentInvestRepay 投资人还款计划
     * @throws AmountTransferException
     */
    private void processInvestRepay(long loanRepayId, InvestRepayModel currentInvestRepay) throws Exception {
        long investRepayId = currentInvestRepay.getId();
        InvestModel investModel = investMapper.findById(currentInvestRepay.getInvestId());
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        // interest user bill
        long paybackAmount = investModel.getAmount() + currentInvestRepay.getActualInterest();
        AmountTransferMessage inAtm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, investModel.getLoginName(),
                investRepayId, paybackAmount, UserBillBusinessType.ADVANCE_REPAY, null, null);

        // fee user bill
        AmountTransferMessage outAtm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, investModel.getLoginName(),
                investRepayId, currentInvestRepay.getActualFee(), UserBillBusinessType.INVEST_FEE, null, null);

        inAtm.setNext(outAtm);

        logger.info(MessageFormat.format("[Advance Repay {0}] send amount transfer message to update user account. invest repay({1}), payback amount({2}), fee amount({3})",
                String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId()), String.valueOf(paybackAmount), String.valueOf(currentInvestRepay.getActualFee())));

        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, inAtm);

        //update invest repay
        currentInvestRepay.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(currentInvestRepay);

        logger.info(MessageFormat.format("[Advance Repay {0}] invest repay({1}) update status to COMPLETE",
                String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId())));

        // update other REPAYING invest repay
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
        investRepayModels.stream().filter(investRepayModel -> investRepayModel.getStatus() == RepayStatus.REPAYING).forEach(investRepayModel -> {
            investRepayModel.setStatus(RepayStatus.COMPLETE);
            investRepayModel.setActualRepayDate(currentInvestRepay.getActualRepayDate());
            investRepayMapper.update(investRepayModel);
            logger.info(MessageFormat.format("[Advance Repay {0}] invest repay({1}) update other REPAYING invest repay({2}) status to COMPLETE",
                    String.valueOf(loanRepayId), String.valueOf(currentInvestRepay.getId()), String.valueOf(investRepayModel.getId())));
        });

        //Title:您投资的{0}提前还款，{1}元已返还至您的账户！
        //Content:尊敬的用户，您在{0}投资的房产/车辆抵押借款因借款人放弃借款而提前终止，您的收益与本金已返还至您的账户，您可以【看看其他优质项目】
        String title = MessageFormat.format(MessageEventType.ADVANCED_REPAY.getTitleTemplate(), loanModel.getName(), AmountConverter.convertCentToString(currentInvestRepay.getRepayAmount()));
        String content = MessageFormat.format(MessageEventType.ADVANCED_REPAY.getContentTemplate(), loanModel.getName());
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.ADVANCED_REPAY,
                Lists.newArrayList(investModel.getLoginName()), title, content, investRepayId));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(investModel.getLoginName()), PushSource.ALL, PushType.ADVANCED_REPAY, title, AppUrl.MESSAGE_CENTER_LIST));

        mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(investModel.getLoginName(), WeChatMessageType.ADVANCE_REPAY_SUCCESS, currentInvestRepay.getId()));
    }

    private boolean isPaybackInvestSuccess(LoanRepayModel currentLoanRepayModel, List<InvestModel> successInvests) {
        logger.info(MessageFormat.format("[Advance Repay {0}] invest payback status summary", String.valueOf(currentLoanRepayModel.getId())));
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(currentLoanRepayModel.getId()));
        boolean isSuccess = true;
        for (InvestModel investModel : successInvests) {
            InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), currentLoanRepayModel.getPeriod());
            SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(investRepayModel.getId())));
            logger.info(MessageFormat.format("[Advance Repay {0}] invest payback({1}) redis status is {2}",
                    String.valueOf(currentLoanRepayModel.getId()), String.valueOf(investRepayModel.getId()), syncRequestStatus.name()));
            if (syncRequestStatus == SyncRequestStatus.FAILURE) {
                isSuccess = false;
            }
        }

        SyncRequestStatus syncRequestStatus = SyncRequestStatus.valueOf(redisWrapperClient.hget(redisKey, String.valueOf(currentLoanRepayModel.getId())));
        logger.info(MessageFormat.format("[Advance Repay {0}] invest fee redis status is {2}", String.valueOf(currentLoanRepayModel.getId()), syncRequestStatus.name()));
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
        logger.info("sent advance repay fatal sms message");
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(MessageFormat.format("提前还款业务错误。详细信息：{0}", errMsg));
        smsWrapperClient.sendFatalNotify(dto);
    }

}

