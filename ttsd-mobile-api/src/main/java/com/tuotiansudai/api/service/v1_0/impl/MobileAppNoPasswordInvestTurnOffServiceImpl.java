package com.tuotiansudai.api.service.v1_0.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOffRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOffService;
import com.tuotiansudai.api.util.AppVersionUtil;
import com.tuotiansudai.client.MQWrapperClient;
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
public class MobileAppNoPasswordInvestTurnOffServiceImpl implements MobileAppNoPasswordInvestTurnOffService {

    private static Logger logger = Logger.getLogger(MobileAppNoPasswordInvestTurnOffServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsCaptchaService smsCaptchaService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private MQWrapperClient mqWrapperClient;


    @Override
    public BaseResponseDto noPasswordInvestTurnOff(NoPasswordInvestTurnOffRequestDto noPasswordInvestTurnOffRequestDto, String ip) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String loginName = noPasswordInvestTurnOffRequestDto.getBaseParam().getUserId();

        if (AppVersionUtil.low == AppVersionUtil.compareVersion()) {
            UserModel userModel = userMapper.findByLoginName(loginName);
            boolean verifyCaptchaFailed = !smsCaptchaService.verifyMobileCaptcha(userModel.getMobile(), noPasswordInvestTurnOffRequestDto.getCaptcha(), CaptchaType.NO_PASSWORD_INVEST);
            if (verifyCaptchaFailed) {
                baseResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
                baseResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());

                // 发送用户行为日志 MQ
                sendUserOpLogMessage(noPasswordInvestTurnOffRequestDto, ip, false);
                return baseResponseDto;
            }
        }

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        accountModel.setNoPasswordInvest(false);
        accountMapper.update(accountModel);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());

        // 发送用户行为日志 MQ
        sendUserOpLogMessage(noPasswordInvestTurnOffRequestDto, ip, true);
        return baseResponseDto;
    }

    private void sendUserOpLogMessage(NoPasswordInvestTurnOffRequestDto dto, String ip, boolean success) {

        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setId(idGenerator.generate());
        logModel.setLoginName(dto.getBaseParam().getUserId());
        logModel.setIp(ip);
        logModel.setDeviceId(dto.getBaseParam().getDeviceId());
        String platform = dto.getBaseParam().getPlatform();
        logModel.setSource(platform == null ? null : Source.valueOf(platform.toUpperCase(Locale.ENGLISH)));
        logModel.setOpType(UserOpType.INVEST_NO_PASSWORD);
        logModel.setCreatedTime(new Date());
        logModel.setDescription("Turn Off. " + (success ? "Success" : "Fail"));

        try {
            String message = JsonConverter.writeValueAsString(logModel);
            mqWrapperClient.sendMessage(MessageQueue.UserOperateLog, message);
        } catch (JsonProcessingException e) {
            logger.error("[MQ] 关闭免密投资 send user op log fail.", e);
        }
    }

}
