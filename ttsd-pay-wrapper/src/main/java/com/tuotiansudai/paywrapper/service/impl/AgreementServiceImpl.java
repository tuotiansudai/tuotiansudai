package com.tuotiansudai.paywrapper.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.AgreementBusinessType;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.AgreementNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.PtpMerBindAgreementRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.AgreementNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.PtpMerBindAgreementRequestModel;
import com.tuotiansudai.paywrapper.service.AgreementService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class AgreementServiceImpl implements AgreementService {

    static Logger logger = Logger.getLogger(AgreementServiceImpl.class);

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> agreement(AgreementDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        PtpMerBindAgreementRequestModel ptpMerBindAgreementRequestModel = new PtpMerBindAgreementRequestModel(accountModel.getPayUserId(), dto);

        // 发送用户行为日志 MQ消息
        sendUserOpLogMessageQueue(dto);

        try {
            return payAsyncClient.generateFormData(PtpMerBindAgreementRequestMapper.class, ptpMerBindAgreementRequestModel);
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }


    private void sendUserOpLogMessageQueue(AgreementDto dto){

        UserOpType opType;

        if (dto.isAutoInvest() || dto.isNoPasswordInvest()) {
            opType = UserOpType.NO_PASSWORD_AGREEMENT; // 开通免密支付协议
        } else if (dto.isFastPay()) {
            opType = UserOpType.FAST_PAY_AGREEMENT; // 开通快捷支付协议
        } else {
            return;
        }

        String loginName = dto.getLoginName();
        String deviceId = dto.getDeviceId();
        Source source = dto.getSource();
        String ip = dto.getIp();

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setLoginName(loginName);
        logModel.setIp(ip);
        logModel.setDeviceId(deviceId);
        logModel.setSource(source);
        logModel.setOpType(opType);
        logModel.setCreatedTime(new Date());

        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(logModel));
        } catch (JsonProcessingException e) {
            logger.error("[MQ] agreement, send UserOperateLog fail.", e);
        }
    }

    @Override
    public String agreementCallback(Map<String, String> paramsMap, String queryString, AgreementBusinessType agreementBusinessType) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, queryString, AgreementNotifyMapper.class, AgreementNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }

        AgreementNotifyRequestModel agreementNotifyRequestModel = (AgreementNotifyRequestModel) callbackRequest;
        AccountModel accountModel = accountMapper.findByPayUserId(agreementNotifyRequestModel.getUserId());
        if (agreementNotifyRequestModel.isSuccess() && accountModel != null) {
            ((AgreementService) AopContext.currentProxy()).postAgreementCallback(accountModel.getLoginName(), agreementBusinessType);
        } else {
            logger.error(MessageFormat.format("Agreement callback is not success (userId = {0})", agreementNotifyRequestModel.getUserId()));
        }

        return callbackRequest.getResponseData();
    }

    @Transactional
    @Override
    public void postAgreementCallback(String loginName, AgreementBusinessType agreementBusinessType) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);

        accountModel.setNoPasswordInvest(AgreementBusinessType.NO_PASSWORD_INVEST == agreementBusinessType || accountModel.isNoPasswordInvest());
        accountModel.setAutoInvest(Lists.newArrayList(AgreementBusinessType.NO_PASSWORD_INVEST, AgreementBusinessType.AUTO_INVEST).contains(agreementBusinessType) || accountModel.isAutoInvest());
        accountModel.setAutoRepay(AgreementBusinessType.AUTO_REPAY == agreementBusinessType || accountModel.isAutoRepay());
        accountMapper.update(accountModel);

        if (AgreementBusinessType.FAST_PAY == agreementBusinessType) {
            BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(loginName);
            bankCardModel.setIsFastPayOn(true);
            bankCardMapper.update(bankCardModel);
        }

        if (AgreementBusinessType.NO_PASSWORD_INVEST == agreementBusinessType) {
            mqWrapperClient.sendMessage(MessageQueue.TurnOnNoPasswordInvest_CompletePointTask, loginName);
        }
    }
}
