package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.constants.PayReturnCode;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.dto.ResetUmpayPasswordDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class AccountServiceImpl implements AccountService {

    static Logger logger = Logger.getLogger(AccountServiceImpl.class);

    /**
     * 第一次调用超时后，最多允许重试的次数。
     */
    private static final int MAX_REGISTER_RETRY_TIMES = 2;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public BaseDto<PayDataDto> registerAccount(RegisterAccountDto dto) {
        BaseDto<PayDataDto> baseDto = registerAccountRetryOnTimeout(dto, 0);
        if (baseDto.getData().getStatus()) {
            //Title:恭喜您认证成功
            //Content:尊敬的{0}女士/先生，恭喜您认证成功，您的支付密码已经由联动优势发送至注册手机号码中,马上【绑定银行卡】开启赚钱之旅吧！
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.REGISTER_ACCOUNT_SUCCESS,
                    Lists.newArrayList(dto.getLoginName()),
                    MessageEventType.REGISTER_ACCOUNT_SUCCESS.getTitleTemplate(),
                    MessageFormat.format(MessageEventType.REGISTER_ACCOUNT_SUCCESS.getContentTemplate(), dto.getUserName()),
                    null
            ));

            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(dto.getLoginName()),
                    PushSource.ALL,
                    PushType.REGISTER_ACCOUNT_SUCCESS,
                    MessageEventType.REGISTER_ACCOUNT_SUCCESS.getTitleTemplate(),
                    AppUrl.MESSAGE_CENTER_LIST));
        }

        return baseDto;
    }

    public AccountModel findByLoginName(String loginName) {
        return accountMapper.findByLoginName(loginName);
    }

    public long getBalance(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel != null ? accountModel.getBalance() : 0;
    }

    @Override
    public long getFreeze(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        return accountModel != null ? accountModel.getFreeze() : 0;
    }

    @Override
    public boolean resetUmpayPassword(String loginName, String identityNumber) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        if (userModel == null || !userModel.getIdentityNumber().equals(identityNumber)) {
            return false;
        }
        ResetUmpayPasswordDto resetUmpayPasswordDto = new ResetUmpayPasswordDto(loginName, identityNumber);
        return payWrapperClient.resetUmpayPassword(resetUmpayPasswordDto);
    }

    private BaseDto<PayDataDto> registerAccountRetryOnTimeout(RegisterAccountDto dto, int retry_times) {
        BaseDto<PayDataDto> baseDto = payWrapperClient.register(dto);
        PayDataDto payData = baseDto.getData();
        if (!payData.getStatus()
                && PayReturnCode.ERROR_TIMEOUT.getValue().equalsIgnoreCase(payData.getCode())
                && retry_times < MAX_REGISTER_RETRY_TIMES) {
            return registerAccountRetryOnTimeout(dto, retry_times + 1);
        }
        if (!payData.getStatus()
                && PayReturnCode.ERROR_TIMEOUT.getValue().equalsIgnoreCase(payData.getCode())
                && retry_times == MAX_REGISTER_RETRY_TIMES) {
            logger.error(String.format("account register timeout %d times, mobile: %s", retry_times + 1, dto.getMobile()));
        }
        return baseDto;
    }
}
