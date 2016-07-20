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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        String username = loginRequestDto.getJ_username() == null ? loginRequestDto.getMobile():loginRequestDto.getJ_username();
        String password = loginRequestDto.getJ_password() == null ? loginRequestDto.getPassword():loginRequestDto.getJ_password();
        String captcha = loginRequestDto.getCaptcha();
        String source = loginRequestDto.getJ_source() == null?loginRequestDto.getSource():loginRequestDto.getJ_source();
        String deviceId = loginRequestDto.getJ_deviceId() == null ? loginRequestDto.getDeviceId():loginRequestDto.getJ_deviceId();
        SignInDto signInDto = new SignInDto(username, password, captcha, source, deviceId);
        BaseDto<LoginDto> baseDto = signInClient.sendSignIn(null, signInDto);
        BaseResponseDto<LoginResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        LoginResponseDataDto loginResponseDataDto = new LoginResponseDataDto();
        if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
            loginResponseDataDto.setToken(mobileAppTokenProvider.refreshToken(username));
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            baseResponseDto.setData(loginResponseDataDto);
        } else {

            ReturnMessage errorMsg = ReturnMessage.LOGIN_FAILED;
            if (baseDto.getData().isLocked()) {
                errorMsg = ReturnMessage.USER_IS_DISABLED;
            } else if (baseDto.getData().isCaptchaNotMatch()) {
                errorMsg = ReturnMessage.IMAGE_CAPTCHA_IS_WRONG;
            }else if(baseDto.getData().isNeedImageCaptcha()){
                errorMsg = ReturnMessage.NEED_IMAGE_CAPTCHA;
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
