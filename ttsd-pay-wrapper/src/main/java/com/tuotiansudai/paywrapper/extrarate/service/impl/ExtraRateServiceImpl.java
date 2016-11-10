package com.tuotiansudai.paywrapper.extrarate.service.impl;


import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.job.ExtraRateInvestCallbackJob;
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
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
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

    @Value("${common.environment}")
    private Environment environment;

    @Value(value = "${pay.extra.rate.invest.notify.process.batch.size}")
    private int extraRateProcessListSize;

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
                    this.sendExtraRateAmount(investExtraRateModel, actualInterest, actualFee);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Normal Repay {0}] extra rate is failed, investId={0} loginName={1} amount={3}",
                            String.valueOf(loanRepayId),
                            String.valueOf(investExtraRateModel.getInvestId()),
                            investExtraRateModel.getLoginName(),
                            String.valueOf(investExtraRateModel.getAmount())), e);
                    continue;
                }
            }
        }
    }

    private void sendExtraRateAmount(InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee) throws Exception {
        InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
        AccountModel accountModel = accountMapper.findByLoginName(investModel.getLoginName());
        if (accountModel == null) {
            logger.debug(MessageFormat.format("user has no account, investId={0} loginName={1} amount={3}",
                    String.valueOf(investModel.getId()),
                    investModel.getLoginName(),
                    String.valueOf(investModel.getAmount())));
            return;
        }
        long amount = actualInterest - actualFee;
        if (amount > 0) {
            investRateService.updateExtraRateData(investExtraRateModel, actualInterest, actualFee);
            String orderId = investExtraRateModel.getInvestId() + "X" + System.currentTimeMillis();
            try {
                TransferRequestModel requestModel = TransferRequestModel.newExtraRateRequest(
                        String.valueOf(orderId),
                        accountModel.getPayUserId(),
                        String.valueOf(amount),
                        "extra_rate_notify");

                paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
            } catch (PayException e) {
                fatalLog("extra rate sync send fail. orderId:" +orderId, e);
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
        redisWrapperClient.incr(ExtraRateInvestCallbackJob.REPAY_EXTRA_RATE_JOB_TRIGGER_KEY);
        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncExtraRateInvestCallback() {
        List<ExtraRateNotifyRequestModel> todoList = extraRateNotifyRequestMapper.getTodoList(extraRateProcessListSize);
        for (ExtraRateNotifyRequestModel model : todoList) {
            if (updateExtraRateNotifyRequestStatus(model)) {
                try {
                   this.processOneCallback(model);
                } catch (Exception e) {
                    fatalLog("extra rate invest callback, processOneCallback error. orderId:" + model.getOrderId(), e);
                }
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
            redisWrapperClient.decr(ExtraRateInvestCallbackJob.REPAY_EXTRA_RATE_JOB_TRIGGER_KEY);
            extraRateNotifyRequestMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
        } catch (Exception e) {
            fatalLog("update_repay_extra_rate_invest_notify_status_fail, orderId:" + model.getOrderId() + ",id:" + model.getId(), e);
            return false;
        }
        return true;
    }

    @Override
    public void processOneCallback(ExtraRateNotifyRequestModel callbackRequestModel) throws Exception  {
        String orderIdStr = callbackRequestModel.getOrderId().split("X")[0];
        long orderId = Long.parseLong(orderIdStr);
        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(orderId);
        long amount = investExtraRateModel.getActualInterest() - investExtraRateModel.getActualFee();
        if (callbackRequestModel.isSuccess() || amount == 0)
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
                this.sendExtraRateAmount(investExtraRateModel, actualInterest, actualFee);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Advance Repay {0}] extra rate is failed, investId={0} loginName={1} amount={3}",
                        String.valueOf(loanRepayId),
                        String.valueOf(investExtraRateModel.getInvestId()),
                        investExtraRateModel.getLoginName(),
                        String.valueOf(investExtraRateModel.getAmount())), e);
                continue;
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
