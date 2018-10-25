package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOnRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOnService;
import com.tuotiansudai.api.util.AppVersionUtil;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.SmsCaptchaType;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.message.UserOpLogMessage;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class MobileAppNoPasswordInvestTurnOnServiceImpl implements MobileAppNoPasswordInvestTurnOnService {

    private static Logger logger = Logger.getLogger(MobileAppNoPasswordInvestTurnOnServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    @Transactional
    public BaseResponseDto noPasswordInvestTurnOn(NoPasswordInvestTurnOnRequestDto dto, String ip) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String loginName = dto.getBaseParam().getUserId();

        if (AppVersionUtil.low != AppVersionUtil.compareVersion()) {
            UserModel userModel = userMapper.findByLoginName(loginName);
            boolean verifyCaptchaFailed = !smsCaptchaService.verifyMobileCaptcha(userModel.getMobile(), dto.getCaptcha(), SmsCaptchaType.NO_PASSWORD_INVEST);
            if (verifyCaptchaFailed) {
                baseResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
                baseResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
                return baseResponseDto;
            }
        }

        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        accountModel.setNoPasswordInvest(true);
        accountMapper.update(accountModel);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());

        BaseParam baseParam = dto.getBaseParam();
        // 发送用户行为日志 MQ
        mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, new UserOpLogMessage(IdGenerator.generate(), baseParam.getUserId(), baseParam.getPhoneNum(), UserOpType.INVEST_NO_PASSWORD, ip, baseParam.getDeviceId(), baseParam.getPlatform() == null ? null : Source.valueOf(baseParam.getPlatform().toUpperCase(Locale.ENGLISH)), "Turn On"));
        return baseResponseDto;
    }
}
