package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.util.RequestIPParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/v1.0/anxin")
@Api(description = "安心签")
public class MobileAppAnxinSignController {

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @ResponseBody
    @RequestMapping(value = "/sendCaptcha", method = RequestMethod.POST)
    @ApiOperation("发送短信验证码")
    public BaseResponseDto sendCaptcha(@RequestBody AnxinSignSendCaptchaRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        boolean isVoice = requestDto.isVoice();

        BaseDto<AnxinDataDto> retDto = anxinWrapperClient.sendCaptcha(new AnxinSendCaptchaDto(loginName, isVoice));

        if (retDto.isSuccess()) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        }
        return new BaseResponseDto(ReturnMessage.FAIL.getCode(), retDto.getData().getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/verifyCaptcha", method = RequestMethod.POST)
    @ApiOperation("验证短信验证码")
    public BaseResponseDto verifyCaptcha(@RequestBody AnxinSignVerifyCaptchaRequestDto requestDto, HttpServletRequest request) {

        String ip = RequestIPParser.parse(request);

        String loginName = requestDto.getBaseParam().getUserId();
        String captcha = requestDto.getCaptcha();
        boolean skipAuth = requestDto.isSkipAuth();

        BaseDto<AnxinDataDto> retDto = anxinWrapperClient.verifyCaptcha(new AnxinVerifyCaptchaDto(loginName, ip, captcha, skipAuth));

        if (retDto.isSuccess()) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        }
        return new BaseResponseDto(ReturnMessage.FAIL.getCode(), retDto.getData().getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/switchSkipAuth", method = RequestMethod.POST)
    @ApiOperation("开通免密")
    public BaseResponseDto switchSkipAuth(@RequestBody AnxinSignSwitchSkipAuthRequestDto requestDto) {

        String loginName = requestDto.getBaseParam().getUserId();
        boolean open = requestDto.isOpen();

        BaseDto<AnxinDataDto> retDto = anxinWrapperClient.switchSkipAuth(new AnxinSwitchSkipAuthDto(loginName, open));

        if (retDto.isSuccess()) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        }
        return new BaseResponseDto(ReturnMessage.FAIL.getCode(), retDto.getData().getMessage());
    }


    @ResponseBody
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    @ApiOperation("开通安心签用户")
    public BaseResponseDto createAccount(@RequestBody BaseParamDto requestDto) {

        String loginName = requestDto.getBaseParam().getUserId();

        BaseDto<AnxinDataDto> retDto = anxinWrapperClient.createAccount(loginName);

        if (retDto.isSuccess()) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        }
        return new BaseResponseDto(ReturnMessage.FAIL.getCode(), retDto.getData().getMessage());
    }


}
