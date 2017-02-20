package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.ExperienceInterestNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ExperienceInterestNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferWithNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.ExperienceRepayService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ExperienceRepayServiceImpl implements ExperienceRepayService {

    private static Logger logger = LoggerFactory.getLogger(ExperienceRepayServiceImpl.class);

    private final static String EXPERIENCE_INTEREST_REDIS_KEY = "SEND_EXPERIENCE_INTEREST";

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public boolean repay(String loginName) {
        List<InvestModel> investModels = investMapper.findByLoanIdAndLoginName(1, loginName);
        if (investModels.size() != 1) {
            logger.error("[Experience Repay] {} invest size is not 1", loginName);
            return false;
        }

        InvestModel investModel = investModels.get(0);
        if (investModel.getStatus() != InvestStatus.SUCCESS) {
            logger.error("[Experience Repay {}] invest is not existed or status is not SUCCESS", investModel.getId());
            return false;
        }

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        if (loanModel == null || loanModel.getProductType() != ProductType.EXPERIENCE) {
            logger.error("[Experience Repay {}] loan is not existed or loan is not EXPERIENCE", investModel.getId());
            return false;
        }

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null) {
            logger.error("[Experience Repay {}] user {} has no account", investModel.getId(), loginName);
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

        TransferWithNotifyRequestModel requestModel = TransferWithNotifyRequestModel.experienceInterestRequest(
                MessageFormat.format("{0}X{1}", String.valueOf(investRepayModel.getId()), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                accountModel.getPayAccountId(),
                String.valueOf(repayAmount));

        if (Strings.isNullOrEmpty(redisWrapperClient.hget(EXPERIENCE_INTEREST_REDIS_KEY, loginName))) {
            redisWrapperClient.hset(EXPERIENCE_INTEREST_REDIS_KEY, loginName, SyncRequestStatus.READY.name());
        }

        String requestStatus = redisWrapperClient.hget(EXPERIENCE_INTEREST_REDIS_KEY, loginName);
        if (SyncRequestStatus.READY.name().equalsIgnoreCase(requestStatus) || SyncRequestStatus.FAILURE.name().equalsIgnoreCase(requestStatus)) {
            try {
                redisWrapperClient.hset(EXPERIENCE_INTEREST_REDIS_KEY, loginName, SyncRequestStatus.SENT.name());
                TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                boolean isSuccess = responseModel.isSuccess();
                redisWrapperClient.hset(EXPERIENCE_INTEREST_REDIS_KEY, loginName, isSuccess ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                logger.error("[Experience Repay {}] invest repay is failed", investModel.getId());
                return isSuccess;
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Experience Repay {0}] invest repay is failed", investModel.getId()), e);
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
    public BaseDto<PayDataDto> postCallback(long investRepayId) throws AmountTransferException {
        InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);
        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        redisWrapperClient.hset(EXPERIENCE_INTEREST_REDIS_KEY, investModel.getLoginName(), SyncRequestStatus.SUCCESS.name());

        investRepayModel.setStatus(RepayStatus.COMPLETE);
        investRepayMapper.update(investRepayModel);

        amountTransfer.transferInBalance(investModel.getLoginName(), investRepayModel.getId(), investRepayModel.getRepayAmount(), UserBillBusinessType.EXPERIENCE_INTEREST, null, null);

        String detail = MessageFormat.format(SystemBillDetailTemplate.EXPERIENCE_INTEREST_DETAIL_TEMPLATE.getTemplate(),
                investModel.getLoginName(), String.valueOf(investRepayModel.getRepayAmount()));

        systemBillService.transferOut(investRepayModel.getId(), investRepayModel.getRepayAmount(), SystemBillBusinessType.EXPERIENCE_INTEREST, detail);

        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        return new BaseDto<>(baseDataDto);
    }
}
