package com.tuotiansudai.paywrapper.extrarate.service.impl;


import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.extrarate.service.InvestRateService;
import com.tuotiansudai.paywrapper.repository.mapper.ExtraRateNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ExtraRateTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.RedisWrapperClient;
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

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

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
    private InvestRateService investRateService;

    @Value("${common.environment}")
    private Environment environment;

    @Value(value = "${pay.extra.rate.invest.notify.process.batch.size}")
    private int extraRateProcessListSize;

    private final static String REPAY_REDIS_KEY_TEMPLATE = "EXTRA_RATE_REPAY:{0}";

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public void normalRepay(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        boolean isLastPeriod = loanRepayMapper.findLastLoanRepay(loanId).getPeriod() == currentLoanRepay.getPeriod();
        if (isLastPeriod) {
            List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanId);
            for (InvestExtraRateModel investExtraRateModel : investExtraRateModels) {
                if (RepayStatus.COMPLETE == investExtraRateModel.getStatus()) {
                    logger.info(MessageFormat.format("[Normal Repay {0}] investExtraRateId:{1} status is COMPLETE",
                            String.valueOf(loanRepayId), String.valueOf(investExtraRateModel.getId())));
                    continue;
                }
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
                    fatalLog(MessageFormat.format("[Normal Repay {0}] extra rate is failed, investId={0} loginName={1} amount={3}",
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

            String orderId = investExtraRateModel.getId() + "X" + System.currentTimeMillis();
            try {
                TransferRequestModel requestModel = TransferRequestModel.newExtraRateRequest(
                        String.valueOf(orderId),
                        accountModel.getPayUserId(),
                        accountModel.getPayAccountId(),
                        String.valueOf(amount));

                String statusString = redisWrapperClient.hget(redisKey, String.valueOf(investExtraRateModel.getId()));
                if (Strings.isNullOrEmpty(statusString) || SyncRequestStatus.FAILURE.equals(SyncRequestStatus.valueOf(statusString))) {
                    redisWrapperClient.hset(redisKey, String.valueOf(investExtraRateModel.getId()), SyncRequestStatus.SENT.name());
                    logger.info(MessageFormat.format("[Extra Rate Repay loanRepay.id {0}] investExtraRateModel.id payback({1}) send payback request",
                            String.valueOf(loanRepayId), String.valueOf(investExtraRateModel.getId())));
                    TransferResponseModel responseModel = paySyncClient.send(ExtraRateTransferMapper.class, requestModel, TransferResponseModel.class);
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
                TransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        String orderIdStr = callbackRequest.getOrderId().split("X")[0];

        redisWrapperClient.hset(MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, orderIdStr), orderIdStr,
                callbackRequest.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
        if (callbackRequest.isSuccess()) {
            mqWrapperClient.sendMessage(MessageQueue.RepaySuccessExtraRateRepayCallback, orderIdStr);
            logger.info(MessageFormat.format("extra_rate_invest_callback message send success  id:{0}", String.valueOf(callbackRequest.getId())));
        } else {
            logger.error(MessageFormat.format("extra_rate_invest_callback response is failed  id:{0}", String.valueOf(callbackRequest.getId())));
        }
        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncExtraRateInvestCallback(long investExtraRateId) {
        try {
            InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findById(investExtraRateId);
            investRateService.updateExtraRateData(investExtraRateModel, investExtraRateModel.getActualInterest(), investExtraRateModel.getActualFee());
        } catch (Exception e) {
            fatalLog("extra rate invest callback, processOneCallback error. orderId:" + String.valueOf(investExtraRateId), e);
            logger.error(MessageFormat.format("[Extra Rate investExtraRateModel.id payback({1}) payback throw exception",
                    String.valueOf(investExtraRateId)), e);
        }
        BaseDto<PayDataDto> asyncExtraRateNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncExtraRateNotifyDto.setData(baseDataDto);

        return asyncExtraRateNotifyDto;
    }

    @Override
    public void advanceRepay(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanId);
        for (InvestExtraRateModel investExtraRateModel : investExtraRateModels) {
            if (RepayStatus.COMPLETE == investExtraRateModel.getStatus()) {
                logger.info(MessageFormat.format("[Advance Repay {0}] investExtraRateId:{1} status is COMPLETE",
                        String.valueOf(loanRepayId), String.valueOf(investExtraRateModel.getId())));
                continue;
            }
            InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
            long actualInterest = InterestCalculator.calculateExtraLoanRateInterest(loanModel, investExtraRateModel.getExtraRate(), investModel, new Date());
            long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(investModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();

            try {
                this.sendExtraRateAmount(loanRepayId, investExtraRateModel, actualInterest, actualFee);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Advance Repay {0}] extra rate is failed, investId={1} loginName={2} amount={3}",
                        String.valueOf(loanRepayId),
                        String.valueOf(investExtraRateModel.getInvestId()),
                        investExtraRateModel.getLoginName(),
                        String.valueOf(investExtraRateModel.getAmount())), e);
                fatalLog(MessageFormat.format("[Advance Repay {0}] extra rate is failed, investId={1} loginName={2} amount={3}",
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
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("阶梯加息还款业务错误。详细信息：{0}", errMsg));
    }

}
