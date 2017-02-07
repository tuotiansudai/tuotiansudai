package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ExperienceInterestNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ExperienceInterestNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferWithNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.ExperienceRepayService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class ExperienceRepayServiceImpl implements ExperienceRepayService {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ExperienceRepayServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private ExperienceInterestNotifyRequestMapper experienceInterestNotifyRequestMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;


    private final static String EXPERIENCE_INTEREST_REDIS_KEY_TEMPLATE = "SEND_EXPERIENCE_INTEREST";

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    @Transactional
    public boolean repay(long investId) {
        InvestModel investModel = investMapper.findById(investId);

        if (investModel == null || investModel.getStatus() != InvestStatus.SUCCESS) {
            logger.error("[Experience Repay {}] invest is not existed or status is not SUCCESS", investId);
            return false;
        }

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        if (loanModel == null || loanModel.getProductType() != ProductType.EXPERIENCE) {
            logger.error("[Experience Repay {}] loan is not existed or loan is not EXPERIENCE", investId);
            return false;
        }

        AccountModel accountModel = accountMapper.findByLoginName(investModel.getLoginName());
        if (accountModel == null) {
            logger.error("[Experience Repay {}] user {} has no account", investId, investModel.getLoginName());
            return false;
        }

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, 1);
        if (investRepayModel == null || investRepayModel.getStatus() != RepayStatus.REPAYING) {
            logger.info("[Experience Repay {}] invest repay is not existed or status is not REPAYING");
            return false;
        }

        long repayAmount = investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();

        String redisKey = MessageFormat.format(EXPERIENCE_INTEREST_REDIS_KEY_TEMPLATE, investModel.getLoginName());
        try {
            TransferWithNotifyRequestModel requestModel = TransferWithNotifyRequestModel.experienceInterestRequest(
                    String.valueOf(investRepayModel.getId()),
                    accountModel.getPayUserId(),
                    accountModel.getPayAccountId(),
                    String.valueOf(investRepayModel.getRepayAmount()));
            String statusString = redisWrapperClient.hget(redisKey, investModel.getLoginName());
            if (Strings.isNullOrEmpty(statusString)
                    || SyncRequestStatus.FAILURE.equals(SyncRequestStatus.valueOf(statusString.split("|")[0]))) {

                redisWrapperClient.hset(redisKey, investModel.getLoginName(), String.format("%s|%s", SyncRequestStatus.SENT.name(), String.valueOf(investRepayModel.getId())));
                TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                boolean isSuccess = responseModel.isSuccess();
                redisWrapperClient.hset(redisKey, investModel.getLoginName(), String.format("%s|%s", isSuccess ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name(), String.valueOf(investRepayModel.getId())));
                logger.info(String.format("[experience interest:] send experience interest investId:%s response is s%", String.valueOf(investRepayModel.getId()), String.valueOf(isSuccess)));
                return true;
            }

        } catch (PayException e) {
            redisWrapperClient.hset(redisKey, investModel.getLoginName(), String.format("%s|%s", SyncRequestStatus.FAILURE.name(), String.valueOf(investRepayModel.getId())));
            logger.error(String.format("[experience interest:] send experience interest investId:s% payback throw exception", investRepayModel.getId()));
            fatalLog("experience interest sync send fail. orderId:" + investRepayModel.getId(), e);
        }

        return false;
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
        mqWrapperClient.sendMessage(MessageQueue.ExperienceInterestCallback, String.valueOf(callbackRequest.getId()));
        return callbackRequest.getResponseData();
    }

    @Override
    @Transactional
    public BaseDto<PayDataDto> asyncExperienceInterestNotify(long notifyRequestId) {
        ExperienceInterestNotifyRequestModel model = experienceInterestNotifyRequestMapper.findById(notifyRequestId);
        if(updateExperienceInterestNotifyRequestStatus(model)){
            long investRepayId = Long.parseLong(model.getOrderId());
            InvestRepayModel investRepayModel = investRepayMapper.findById(investRepayId);
            InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
            if(model.isSuccess()){
                try {
                    amountTransfer.transferInBalance(investModel.getLoginName(), investRepayModel.getId(), investRepayModel.getRepayAmount(), UserBillBusinessType.EXPERIENCE_INTEREST, null, null);
                } catch (AmountTransferException e) {
                    e.printStackTrace();
                }
                String detail = MessageFormat.format(SystemBillDetailTemplate.EXPERIENCE_INTEREST_DETAIL_TEMPLATE.getTemplate(),
                        investModel.getLoginName(), String.valueOf(investModel.getId()));
                systemBillService.transferOut(investRepayModel.getId(), investRepayModel.getRepayAmount(), SystemBillBusinessType.EXPERIENCE_INTEREST, detail);
            }
        }

        BaseDto<PayDataDto> asyncNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncNotifyDto.setData(baseDataDto);

        return asyncNotifyDto;
    }

    private boolean updateExperienceInterestNotifyRequestStatus(ExperienceInterestNotifyRequestModel model){
        try {
            experienceInterestNotifyRequestMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
        } catch (Exception e) {
            fatalLog("update_experience_interest_notify_status_fail, orderId:" + model.getOrderId() + ",id:" + model.getId(), e);
            return false;
        }
        return true;
    }
}
