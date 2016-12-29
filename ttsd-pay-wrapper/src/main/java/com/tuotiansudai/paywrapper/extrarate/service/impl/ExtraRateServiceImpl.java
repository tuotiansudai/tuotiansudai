package com.tuotiansudai.paywrapper.extrarate.service.impl;


import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.extrarate.service.InvestRateService;
import com.tuotiansudai.paywrapper.repository.mapper.ExtraRateNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ExtraRateNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferWithNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExtraRateServiceImpl implements ExtraRateService {

    static Logger logger = Logger.getLogger(ExtraRateServiceImpl.class);

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private InvestRateService investRateService;

    @Autowired
    private ExtraRateNotifyRequestMapper extraRateNotifyRequestMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    private final static String REPAY_REDIS_KEY_TEMPLATE = "EXTRA_RATE_REPAY:{0}";

    @Override
    public void transferPurchase(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        InvestModel transferInvestModel = investMapper.findById(investModel.getTransferInvestId());
        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(transferInvestModel.getId());
        if (investExtraRateModel != null) {
            investExtraRateModel.setTransfer(true);
            investExtraRateMapper.update(investExtraRateModel);
        }
    }

    @Override
    public void normalRepay(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        boolean isLastPeriod = loanRepayMapper.findLastLoanRepay(loanId).getPeriod() == currentLoanRepay.getPeriod();
        if (isLastPeriod) {
            List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanId);
            for (InvestExtraRateModel investExtraRateModel : investExtraRateModels) {
                long actualInterest = investExtraRateModel.getExpectedInterest();
                long actualFee = investExtraRateModel.getExpectedFee();
                try {
                    this.sendExtraRateAmount(loanRepayId, investExtraRateModel, actualInterest, actualFee);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Normal Repay {0}] extra rate is failed, investId={0} loginName={1} amount={3}",
                            String.valueOf(loanRepayId),
                            String.valueOf(investExtraRateModel.getInvestId()),
                            investExtraRateModel.getLoginName(),
                            String.valueOf(investExtraRateModel.getAmount())), e);
                }
            }
        }
    }

    private void sendExtraRateAmount(long loanRepayId, InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee) throws Exception {
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
        AccountModel accountModel = accountMapper.findByLoginName(investModel.getLoginName());
        if (accountModel == null) {
            logger.info(MessageFormat.format("user has no account, investId={0} loginName={1} amount={3}",
                    String.valueOf(investModel.getId()),
                    investModel.getLoginName(),
                    String.valueOf(investModel.getAmount())));
            return;
        }
        long amount = actualInterest - actualFee;
        if (amount > 0) {

            investRateService.updateInvestExtraRate(investExtraRateModel, actualInterest, actualFee, amount);

            String orderId = investExtraRateModel.getInvestId() + "X" + System.currentTimeMillis();
            try {
                TransferWithNotifyRequestModel requestModel = TransferWithNotifyRequestModel.newExtraRateRequest(
                        String.valueOf(orderId),
                        accountModel.getPayUserId(),
                        String.valueOf(amount));

                String statusString = redisWrapperClient.hget(redisKey, String.valueOf(investExtraRateModel.getId()));
                if (Strings.isNullOrEmpty(statusString) || SyncRequestStatus.FAILURE.equals(SyncRequestStatus.valueOf(statusString))) {
                    redisWrapperClient.hset(redisKey, String.valueOf(investExtraRateModel.getId()), SyncRequestStatus.SENT.name());
                    logger.info(MessageFormat.format("[Extra Rate Repay loanRepay.id {0}] investExtraRateModel.id payback({1}) send payback request",
                            String.valueOf(loanRepayId), String.valueOf(investExtraRateModel.getId())));
                    TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                    boolean isSuccess = responseModel.isSuccess();
                    redisWrapperClient.hset(redisKey, String.valueOf(investExtraRateModel.getId()), isSuccess ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                    logger.info(MessageFormat.format("[Extra Rate Repay loanRepay.id {0}] investExtraRateModel.id payback({1}) payback response is {2}",
                            String.valueOf(loanRepayId), String.valueOf(investExtraRateModel.getId()), String.valueOf(isSuccess)));
                }
            } catch (PayException e) {
                redisWrapperClient.hset(redisKey, String.valueOf(investExtraRateModel.getId()), SyncRequestStatus.FAILURE.name());
                logger.error(MessageFormat.format("[Extra Rate Repay loanRepay.id {0}] investExtraRateModel.id payback({1}) payback throw exception",
                        String.valueOf(loanRepayId), String.valueOf(investExtraRateModel.getId())), e);
                fatalLog("extra rate sync send fail. orderId:" + orderId, e);
            }

        }

    }

    @Override
    public String extraRateInvestCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                ExtraRateNotifyRequestMapper.class,
                ExtraRateNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }
        mqWrapperClient.sendMessage(MessageQueue.ExtraRateRepayCallback, String.valueOf(callbackRequest.getId()));
        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncExtraRateInvestCallback(long notifyRequestId) {
        ExtraRateNotifyRequestModel model = extraRateNotifyRequestMapper.findById(notifyRequestId);
        if (updateExtraRateNotifyRequestStatus(model)) {
            try {
                this.processOneCallback(model);
            } catch (Exception e) {
                fatalLog("extra rate invest callback, processOneCallback error. orderId:" + model.getOrderId() + ", id:" + model.getId(), e);
                logger.error(MessageFormat.format("[Extra Rate investExtraRateModel.id payback({1}) payback throw exception",
                        String.valueOf(model.getOrderId().split("X")[0])), e);
            }
        }

        BaseDto<PayDataDto> asyncExtraRateNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncExtraRateNotifyDto.setData(baseDataDto);

        return asyncExtraRateNotifyDto;
    }

    private boolean updateExtraRateNotifyRequestStatus(ExtraRateNotifyRequestModel model) {
        try {
            extraRateNotifyRequestMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
        } catch (Exception e) {
            fatalLog("update_repay_extra_rate_invest_notify_status_fail, orderId:" + model.getOrderId() + ",id:" + model.getId(), e);
            return false;
        }
        return true;
    }

    @Override
    public void processOneCallback(ExtraRateNotifyRequestModel callbackRequestModel) throws Exception {
        String orderIdStr = callbackRequestModel.getOrderId().split("X")[0];
        long orderId = Long.parseLong(orderIdStr);
        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(orderId);
        redisWrapperClient.hset(MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(orderId)), String.valueOf(orderId), SyncRequestStatus.SUCCESS.name());
        if (callbackRequestModel.isSuccess())
            investRateService.updateExtraRateData(investExtraRateModel, investExtraRateModel.getActualInterest(), investExtraRateModel.getActualFee());
    }

    @Override
    public void advanceRepay(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanId);
        for (InvestExtraRateModel investExtraRateModel : investExtraRateModels) {
            InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
            long actualInterest = InterestCalculator.calculateExtraLoanRateInterest(loanModel, investExtraRateModel.getExtraRate(), investModel, new Date());
            long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(investModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();

            try {
                this.sendExtraRateAmount(loanRepayId, investExtraRateModel, actualInterest, actualFee);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Advance Repay {0}] extra rate is failed, investId={0} loginName={1} amount={3}",
                        String.valueOf(loanRepayId),
                        String.valueOf(investExtraRateModel.getInvestId()),
                        investExtraRateModel.getLoginName(),
                        String.valueOf(investExtraRateModel.getAmount())), e);
            }
        }
    }

    private void fatalLog(String errMsg, Throwable e) {
        logger.fatal(errMsg, e);
        sendSmsErrNotify(MessageFormat.format("{0},{1}", environment, errMsg));
    }

    private void sendSmsErrNotify(String errMsg) {
        logger.info("sent invest fatal sms message");
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(MessageFormat.format("阶梯加息还款业务错误。详细信息：{0}", errMsg));
        smsWrapperClient.sendFatalNotify(dto);
    }

}
