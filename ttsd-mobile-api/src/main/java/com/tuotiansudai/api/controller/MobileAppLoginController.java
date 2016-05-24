package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.LoginRequestDto;
import com.tuotiansudai.api.dto.LoginResponseDataDto;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoginDto;
import com.tuotiansudai.dto.SignInDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MobileAppLoginController extends MobileAppBaseController{

    @Autowired
    private SignInClient signInClient;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponseDto<LoginResponseDataDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getJ_username();
        String password = loginRequestDto.getJ_password();
        String captcha = loginRequestDto.getCaptcha();
        SignInDto signInDto = new SignInDto(username, password, captcha);
        BaseDto<LoginDto> baseDto = signInClient.sendSignIn("", signInDto);
        BaseResponseDto<LoginResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        return baseResponseDto;
    }

    @RequestMapping(value = "/usermember/logout", method = RequestMethod.POST)
    public BaseResponseDto logout(HttpServletRequest httpServletRequest) {

    }

}
