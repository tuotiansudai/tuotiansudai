package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.service.AnxinSignService;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/v1.0/anxin")
public class MobileAppAnxinSignController {

    @Autowired
    AnxinSignService anxinSignService;

    @ResponseBody
    @RequestMapping(value = "/sendCaptcha", method = RequestMethod.POST)
    public BaseResponseDto sendCaptcha(@RequestBody AnxinSignSendCaptchaRequestDto requestDto){
        String loginName = requestDto.getBaseParam().getUserId();
        boolean isVoice = requestDto.isVoice();

        BaseDto<BaseDataDto> retDto =  anxinSignService.sendCaptcha3101(loginName, isVoice);

        if(retDto.isSuccess()) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        }
        return new BaseResponseDto(ReturnMessage.FAIL.getCode(), retDto.getData().getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/verifyCaptcha", method = RequestMethod.POST)
    public BaseResponseDto verifyCaptcha(@RequestBody AnxinSignVerifyCaptchaRequestDto requestDto, HttpServletRequest request){

        String ip = RequestIPParser.parse(request);

        String loginName = requestDto.getBaseParam().getUserId();
        String captcha = requestDto.getCaptcha();
        boolean skipAuth = requestDto.isSkipAuth();

        BaseDto<BaseDataDto> retDto =  anxinSignService.verifyCaptcha3102(loginName, captcha, skipAuth, ip);

        if(retDto.isSuccess()) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        }
        return new BaseResponseDto(ReturnMessage.FAIL.getCode(), retDto.getData().getMessage());
    }

    @ResponseBody
    @RequestMapping(value = "/switchSkipAuth", method = RequestMethod.POST)
    public BaseResponseDto switchSkipAuth(@RequestBody AnxinSignSwitchSkipAuthRequestDto requestDto){

        String loginName = requestDto.getBaseParam().getUserId();
        boolean open = requestDto.isOpen();

        BaseDto<BaseDataDto> retDto =  anxinSignService.switchSkipAuth(loginName, open);

        if(retDto.isSuccess()) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        }
        return new BaseResponseDto(ReturnMessage.FAIL.getCode(), retDto.getData().getMessage());
    }


    @ResponseBody
    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public BaseResponseDto createAccount(@RequestBody BaseParamDto requestDto){

        String loginName = requestDto.getBaseParam().getUserId();

        BaseDto<BaseDataDto> retDto =  anxinSignService.createAccount3001(loginName);

        if(retDto.isSuccess()) {
            return new BaseResponseDto(ReturnMessage.SUCCESS);
        }
        return new BaseResponseDto(ReturnMessage.FAIL.getCode(), retDto.getData().getMessage());
    }


}
