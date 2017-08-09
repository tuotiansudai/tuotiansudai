package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOffRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOffService;
import com.tuotiansudai.api.util.AppVersionUtil;
import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.service.UserOpLogService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.SmsCaptchaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserOpLogService userOpLogService;


    @Override
    public BaseResponseDto noPasswordInvestTurnOff(NoPasswordInvestTurnOffRequestDto dto, String ip) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        String loginName = dto.getBaseParam().getUserId();
        BaseParam baseParam = dto.getBaseParam();

        if (AppVersionUtil.low == AppVersionUtil.compareVersion()) {
            UserModel userModel = userMapper.findByLoginName(loginName);
            boolean verifyCaptchaFailed = !smsCaptchaService.verifyMobileCaptcha(userModel.getMobile(), dto.getCaptcha(), CaptchaType.NO_PASSWORD_INVEST);
            if (verifyCaptchaFailed) {
                baseResponseDto.setCode(ReturnMessage.SMS_CAPTCHA_ERROR.getCode());
                baseResponseDto.setMessage(ReturnMessage.SMS_CAPTCHA_ERROR.getMsg());

                // 发送用户行为日志 MQ
                userOpLogService.sendUserOpLogMQ(loginName, ip, baseParam.getPlatform(), baseParam.getDeviceId(),
                        UserOpType.INVEST_NO_PASSWORD, "Turn Off. Fail");
                return baseResponseDto;
            }
        }

        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        accountModel.setNoPasswordInvest(false);
        accountMapper.update(accountModel);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());

        // 发送用户行为日志 MQ
        userOpLogService.sendUserOpLogMQ(loginName, ip, baseParam.getPlatform(), baseParam.getDeviceId(),
                UserOpType.INVEST_NO_PASSWORD, "Turn Off. Success");
        return baseResponseDto;
    }
}
