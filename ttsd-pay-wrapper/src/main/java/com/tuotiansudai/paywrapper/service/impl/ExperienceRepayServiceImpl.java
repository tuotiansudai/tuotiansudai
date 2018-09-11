package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.ExperienceInterestNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ExperienceInterestNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.ExperienceRepayService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class ExperienceRepayServiceImpl implements ExperienceRepayService {

    private static Logger logger = LoggerFactory.getLogger(ExperienceRepayServiceImpl.class);

    private final static String EXPERIENCE_INTEREST_REDIS_KEY = "SEND_EXPERIENCE_INTEREST";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean repay(long investId) {

        InvestModel investModel = investMapper.findById(investId);
        if (investModel == null) {
            logger.warn("[Experience Repay] {} investId  is not exist ", investId);
            return false;
        }
        if (investModel.getLoanId() != 1) {
            logger.warn("[Experience Repay] {} investId  is not NEWBIE ", investId);
            return false;
        }
        String loginName = investModel.getLoginName();
        if (investModel.getStatus() != InvestStatus.SUCCESS) {
            logger.warn("[Experience Repay {}] invest is not existed or status is not SUCCESS", investModel.getId());
            return false;
        }

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        if (loanModel == null || loanModel.getProductType() != ProductType.EXPERIENCE) {
            logger.warn("[Experience Repay {}] loan is not existed or loan is not EXPERIENCE", investModel.getId());
            return false;
        }

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null) {
            logger.warn("[Experience Repay {}] user {} has no account", investModel.getId(), loginName);
            return false;
        }

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), 1);
        if (investRepayModel == null || investRepayModel.getStatus() != RepayStatus.REPAYING) {
            logger.info("[Experience Repay {}] invest repay is not existed or status is not REPAYING", investModel.getId());
            return false;
        }

        long repayAmount = investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
        investRepayModel.setActualInterest(investRepayModel.getExpectedInterest());
        investRepayModel.setActualFee(investRepayModel.getExpectedFee());
        investRepayModel.setRepayAmount(repayAmount);
        investRepayModel.setActualRepayDate(new Date());
        investRepayMapper.update(investRepayModel);

        TransferRequestModel requestModel = TransferRequestModel.experienceInterestRequest(
                MessageFormat.format("{0}X{1}", String.valueOf(investRepayModel.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                accountModel.getPayAccountId(),
                String.valueOf(repayAmount));

        if (Strings.isNullOrEmpty(redisWrapperClient.hget(EXPERIENCE_INTEREST_REDIS_KEY, String.valueOf(investId)))) {
            redisWrapperClient.hset(EXPERIENCE_INTEREST_REDIS_KEY, String.valueOf(investId), SyncRequestStatus.READY.name());
        }

        String requestStatus = redisWrapperClient.hget(EXPERIENCE_INTEREST_REDIS_KEY, String.valueOf(investId));
        if (SyncRequestStatus.READY.name().equalsIgnoreCase(requestStatus) || SyncRequestStatus.FAILURE.name().equalsIgnoreCase(requestStatus)) {
            if (repayAmount > 0) {
                try {
                    redisWrapperClient.hset(EXPERIENCE_INTEREST_REDIS_KEY, String.valueOf(investId), SyncRequestStatus.SENT.name());
                    TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                    boolean isSuccess = responseModel.isSuccess();
                    redisWrapperClient.hset(EXPERIENCE_INTEREST_REDIS_KEY, String.valueOf(investId), isSuccess ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                    logger.info("[Experience Repay {}] invest repay is success", investModel.getId());
                    return isSuccess;
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Experience Repay {0}] invest repay is failed", investModel.getId()), e);
                }
            }
        }

        return true;
    }

    @Override
    @Transactional
    public String repayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                ExperienceInterestNotifyRequestMapper.class,
                ExperienceInterestNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        if (callbackRequest.isSuccess()) {
            mqWrapperClient.sendMessage(MessageQueue.ExperienceRepayCallback, String.valueOf(callbackRequest.getOrderId().split("X")[0]));
        }

        return callbackRequest.getResponseData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> postCallback(long investRepayId) {
        InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);

        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);

        if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
            logger.warn(MessageFormat.format("[Experience Repay {0}] status is COMPLETE, ignore", String.valueOf(investRepayId)));
            return new BaseDto<>(baseDataDto);
        }


        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        redisWrapperClient.hset(EXPERIENCE_INTEREST_REDIS_KEY, String.valueOf(investModel.getId()), SyncRequestStatus.SUCCESS.name());

        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(investRepayModel);

        AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, investModel.getLoginName(),
                investRepayModel.getId(), investRepayModel.getRepayAmount(), UserBillBusinessType.EXPERIENCE_INTEREST);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_EXPERIENCE_REPAY_NOTIFY_TEMPLATE,
                Lists.newArrayList(userMapper.findByLoginName(investModel.getLoginName()).getMobile()),
                Lists.newArrayList(AmountConverter.convertCentToString(investRepayModel.getRepayAmount()))));

        String detail = MessageFormat.format(SystemBillDetailTemplate.EXPERIENCE_INTEREST_DETAIL_TEMPLATE.getTemplate(),
                investModel.getLoginName(), String.valueOf(investRepayModel.getRepayAmount()));

        SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_OUT, investRepayModel.getId(), investRepayModel.getRepayAmount(), SystemBillBusinessType.EXPERIENCE_INTEREST, detail);
        mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);

        return new BaseDto<>(baseDataDto);
    }
}
