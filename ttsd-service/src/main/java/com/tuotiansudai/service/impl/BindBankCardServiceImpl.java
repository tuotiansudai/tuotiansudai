package com.tuotiansudai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.log.repository.model.UserOpType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.InvestRepayService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BindBankCardServiceImpl implements BindBankCardService {

    static Logger logger = Logger.getLogger(BindBankCardServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestRepayService investRepayService;

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        if (accountModel == null) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setMessage("您尚未进行实名认证");
            payFormDataDto.setStatus(false);
            baseDto.setData(payFormDataDto);
            return baseDto;
        }

        BaseDto<PayFormDataDto> baseDto = payWrapperClient.bindBankCard(dto);

        // 发送用户行为日志 MQ消息
        sendBindCardOpLogMessage(dto);

        return baseDto;
    }

    private void sendBindCardOpLogMessage(BindBankCardDto dto) {

        UserOpLogModel logModel = new UserOpLogModel(idGenerator.generate(), dto.getLoginName(), UserOpType.BIND_CARD, dto.getIp(), dto.getDeviceId(), dto.getSource(), null);
        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(logModel));
        } catch (JsonProcessingException e) {
            logger.error("[MQ] 绑卡, send UserOperateLog fail.", e);
        }
    }

    @Override
    public BaseDto<PayFormDataDto> replaceBankCard(BindBankCardDto dto) {
        BaseDto<PayFormDataDto> retDto = payWrapperClient.replaceBankCard(dto);

        // 发送用户行为日志 MQ消息
        sendReplaceCardOpLogMessage(dto);
        return retDto;
    }

    private void sendReplaceCardOpLogMessage(BindBankCardDto dto) {
        UserOpLogModel logModel = new UserOpLogModel(idGenerator.generate(), dto.getLoginName(), UserOpType.REPLACE_CARD, dto.getIp(), dto.getDeviceId(), dto.getSource(), null);
        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(logModel));
        } catch (JsonProcessingException e) {
            logger.error("[MQ] 换卡, send UserOperateLog fail.", e);
        }
    }

    @Override
    public BankCardModel getPassedBankCard(String loginName) {
        return bankCardMapper.findPassedBankCardByLoginName(loginName);
    }

    @Override
    public boolean isReplacing(String loginName) {
        return CollectionUtils.isNotEmpty(bankCardMapper.findApplyBankCardByLoginName(loginName));
    }

    @Override
    public boolean isManual(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel.getFreeze() > 0) {
            return true;
        }

        if (accountModel.getBalance() > 0) {
            return true;
        }

        if (investRepayService.findSumRepayingCorpusByLoginName(loginName) > 0 || investRepayService.findSumRepayingInterestByLoginName(loginName) > 0) {
            return true;
        }

        return false;
    }

    @Override
    public BankCardModel getBankCardById(long id) {
        return bankCardMapper.findById(id);
    }
}
