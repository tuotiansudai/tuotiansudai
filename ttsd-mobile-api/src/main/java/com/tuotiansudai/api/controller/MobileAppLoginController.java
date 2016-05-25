package com.tuotiansudai.api.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.security.MobileAppTokenProvider;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.SignInDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MobileAppLoginController{

    @Autowired
    private SignInClient signInClient;

    @Autowired
    private MobileAppTokenProvider mobileAppTokenProvider;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto<LoginResponseDataDto> login(@Valid @ModelAttribute LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getJ_username();
        String password = loginRequestDto.getJ_password();
        String captcha = loginRequestDto.getCaptcha();
        String source = loginRequestDto.getJ_source();
        String deviceId = loginRequestDto.getJ_deviceId();
        SignInDto signInDto = new SignInDto(username, password, captcha, source, deviceId);
        BaseDto<LoginDto> baseDto = signInClient.sendSignIn(null, signInDto);
        BaseResponseDto<LoginResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        LoginResponseDataDto loginResponseDataDto = new LoginResponseDataDto();
        if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
            loginResponseDataDto.setToken(mobileAppTokenProvider.refreshToken(username, null));
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        } else {
            loginResponseDataDto.setToken("");
            ReturnMessage errorMsg = ReturnMessage.LOGIN_FAILED;
            if (baseDto.getData().isLocked()) {
                errorMsg = ReturnMessage.USER_IS_DISABLED;
            } else if (baseDto.getData().isCaptchaNotMatch()) {
                errorMsg = ReturnMessage.IMAGE_CAPTCHA_IS_WRONG;
            }
            baseResponseDto.setCode(errorMsg.getCode());
            baseResponseDto.setMessage(errorMsg.getMsg());
        }
        baseResponseDto.setData(loginResponseDataDto);
        return baseResponseDto;
    }

    @RequestMapping(value = "/usermember/logout", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponseDto logout(@RequestBody BaseParamDto baseParamDto) {
        String token = baseParamDto.getBaseParam().getToken();
        if (!Strings.isNullOrEmpty(token)) {
            mobileAppTokenProvider.deleteToken(token);
        }
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

}
