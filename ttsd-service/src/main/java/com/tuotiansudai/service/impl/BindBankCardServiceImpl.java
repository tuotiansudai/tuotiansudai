package com.tuotiansudai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.UserOpLogMessage;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserFundMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.UserFundView;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BindBankCardServiceImpl implements BindBankCardService {

    static Logger logger = Logger.getLogger(BindBankCardServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserFundMapper userFundMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserMapper userMapper;


    @Override
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>(payFormDataDto);
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        if (accountModel == null) {
            payFormDataDto.setMessage("您尚未进行实名认证");
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        BankCardModel passedBankCard = bankCardMapper.findPassedBankCardByLoginName(dto.getLoginName());

        if (passedBankCard != null) {
            payFormDataDto.setMessage("已绑定银行卡，请勿重复绑定");
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        baseDto = payWrapperClient.bindBankCard(dto);

        //发送用户行为日志 MQ
        String mobile= Optional.ofNullable(userMapper.findByLoginName(dto.getLoginName())).orElse(new UserModel()).getMobile();
        UserOpLogMessage userOpLogMessage=new UserOpLogMessage(IdGenerator.generate(),dto.getLoginName(),mobile,UserOpType.BIND_CARD,dto.getIp(),dto.getDeviceId(),dto.getSource(),null);
        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(userOpLogMessage));
        } catch (JsonProcessingException e) {
            logger.error("[BindBankCardService] " +"bindBankCard"+ ", send UserOperateLog fail.", e);
        }
        return baseDto;
    }

    @Override
    public BaseDto<PayFormDataDto> replaceBankCard(BindBankCardDto dto) {
        BaseDto<PayFormDataDto> retDto = payWrapperClient.replaceBankCard(dto);
        //发送用户行为日志 MQ
        String mobile= Optional.ofNullable(userMapper.findByLoginName(dto.getLoginName())).orElse(new UserModel()).getMobile();
        UserOpLogMessage userOpLogMessage=new UserOpLogMessage(IdGenerator.generate(),dto.getLoginName(),mobile,UserOpType.REPLACE_CARD,dto.getIp(),dto.getDeviceId(),dto.getSource(),null);
        try {
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, JsonConverter.writeValueAsString(userOpLogMessage));
        } catch (JsonProcessingException e) {
            logger.error("[BindBankCardService] " +"replaceBankCard"+ ", send UserOperateLog fail.", e);
        }
        return retDto;
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
        UserFundView userFundView = userFundMapper.findByLoginName(loginName);
        return accountModel.getFreeze() > 0 || accountModel.getBalance() > 0 || userFundView.getExpectedTotalCorpus() > 0 || userFundView.getExpectedTotalInterest() > 0;

    }

    @Override
    public BankCardModel getBankCardById(long id) {
        return bankCardMapper.findById(id);
    }
}
