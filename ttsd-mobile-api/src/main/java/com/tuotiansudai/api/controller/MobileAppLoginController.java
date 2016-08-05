package com.tuotiansudai.api.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.LoginRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoginResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.security.MobileAppTokenProvider;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.SignInDto;
import com.tuotiansudai.util.CaptchaHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class MobileAppLoginController{
    static Logger logger = Logger.getLogger(MobileAppLoginController.class);

    @Autowired
    private SignInClient signInClient;
    @Autowired
    private CaptchaHelper captchaHelper;
    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto<LoginResponseDataDto> login(@Valid @ModelAttribute LoginRequestDto loginRequestDto,HttpServletRequest httpServletRequest) {
        String username = loginRequestDto.getJ_username() == null ? loginRequestDto.getMobile():loginRequestDto.getJ_username();
        String password = loginRequestDto.getJ_password() == null ? loginRequestDto.getPassword():loginRequestDto.getJ_password();
        String captcha = loginRequestDto.getCaptcha();
        String source = loginRequestDto.getJ_source() == null?loginRequestDto.getSource():loginRequestDto.getJ_source();
        String deviceId = loginRequestDto.getJ_deviceId() == null ? loginRequestDto.getDeviceId():loginRequestDto.getJ_deviceId();
        if(captchaHelper.isNeedImageCaptcha(CaptchaHelper.LOGIN_CAPTCHA, httpServletRequest.getRemoteAddr()) && StringUtils.isEmpty(captcha) ){
            logger.debug("Authentication failed: need image captcha but image captcha is null");
           return new BaseResponseDto<>(ReturnMessage.NEED_IMAGE_CAPTCHA.getCode(),ReturnMessage.NEED_IMAGE_CAPTCHA.getMsg());
        }
        SignInDto signInDto = new SignInDto(username, password, captcha, source, deviceId);
        LoginDto baseDto = signInClient.sendSignIn(null, signInDto);
        BaseResponseDto<LoginResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        LoginResponseDataDto loginResponseDataDto = new LoginResponseDataDto();
        if (baseDto.getStatus()) {
            loginResponseDataDto.setToken(mobileAppTokenProvider.refreshToken(username));
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            baseResponseDto.setData(loginResponseDataDto);
        } else {

            ReturnMessage errorMsg = ReturnMessage.LOGIN_FAILED;
            if (baseDto.isLocked()) {
                errorMsg = ReturnMessage.USER_IS_DISABLED;
            } else if (baseDto.isCaptchaNotMatch()) {
                errorMsg = ReturnMessage.IMAGE_CAPTCHA_IS_WRONG;
            }
            baseResponseDto = mobileAppTokenProvider.generateResponseDto(errorMsg,username);
        }

        return baseResponseDto;
    }

    @RequestMapping(value = "/usermember/logout", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto logout(@RequestParam(value = "token") String token, HttpServletRequest httpServletRequest) {
        if (!Strings.isNullOrEmpty(token)) {
            mobileAppTokenProvider.deleteToken(httpServletRequest);
        }
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

}
