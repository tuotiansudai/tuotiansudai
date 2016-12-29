package com.tuotiansudai.api.service.v1_0.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOnRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOnService;
import com.tuotiansudai.api.util.AppVersionUtil;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.log.repository.model.UserOpType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.SmsCaptchaService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private IdGenerator idGenerator;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public BaseResponseDto noPasswordInvestTurnOn(NoPasswordInvestTurnOnRequestDto noPasswordInvestTurnOnRequestDto, String ip) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String loginName = noPasswordInvestTurnOnRequestDto.getBaseParam().getUserId();

        if (AppVersionUtil.low != AppVersionUtil.compareVersion()) {
            UserModel userModel = userMapper.findByLoginName(loginName);
            boolean verifyCaptchaFailed = !smsCaptchaService.verifyMobileCaptcha(userModel.getMobile(), noPasswordInvestTurnOnRequestDto.getCaptcha(), CaptchaType.NO_PASSWORD_INVEST);
            if (verifyCaptchaFailed) {
                baseResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
                baseResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());
                return baseResponseDto;
            }
        }

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        accountModel.setNoPasswordInvest(true);
        accountMapper.update(accountModel);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());

        sendUserOpLogMessage(noPasswordInvestTurnOnRequestDto, ip);
        return baseResponseDto;
    }


    private void sendUserOpLogMessage(NoPasswordInvestTurnOnRequestDto dto, String ip) {

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setId(idGenerator.generate());
        logModel.setLoginName(dto.getBaseParam().getUserId());
        logModel.setIp(ip);
        logModel.setDeviceId(dto.getBaseParam().getDeviceId());
        String platform = dto.getBaseParam().getPlatform();
        logModel.setSource(platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.INVEST_NO_PASSWORD);
        logModel.setCreatedTime(new Date());
        logModel.setDescription("Turn On");

        try {
            String message = JsonConverter.writeValueAsString(logModel);
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, message);
        } catch (JsonProcessingException e) {
            logger.error("[MQ] 打开免密投资 send user op log fail.", e);
        }
    }

}
