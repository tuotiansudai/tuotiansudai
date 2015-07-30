package com.ttsd.api.controller;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.core.annotations.Logger;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.RetrievePasswordService;
import com.ttsd.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by tuotian on 15/7/29.
 */
@Controller
public class RetrievePasswordController {
    @Logger
    Log log;

    @Resource(name = "RetrievePasswordServiceImpl")
    private RetrievePasswordService retrievePasswordService;

    @RequestMapping(value = "/retrievepassword",method = RequestMethod.POST)
    @ResponseBody
    public RetrievePasswordResponseDto retrievePassword(@RequestBody RetrievePasswordRequestDto retrievePasswordRequestDto){
        try {
           return retrievePasswordService.retrievePassword(retrievePasswordRequestDto);
        } catch (UserNotFoundException e) {
            RetrievePasswordResponseDto retrievePasswordResponseDto = new RetrievePasswordResponseDto();
            retrievePasswordResponseDto.setCode(ReturnMessage.USER_ID_NOT_EXIST.getCode());
            retrievePasswordResponseDto.setMessage(ReturnMessage.USER_IS_NOT_EXIST.getMsg());
            log.error(e.getLocalizedMessage(),e);
            return retrievePasswordResponseDto;
        }
    }

    @RequestMapping(value = "/validatecaptcha",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto validateAuthCode(@RequestBody RetrievePasswordRequestDto retrievePasswordRequestDto){
        return retrievePasswordService.validateAuthCode(retrievePasswordRequestDto);
    }

    @RequestMapping(value = "/retrievepassword/sendsms")
    @ResponseBody
    public BaseResponseDto sendSMS(@RequestBody SendSmsRequestDto sendSmsRequestDto,HttpServletRequest request){
        return retrievePasswordService.sendSMS(sendSmsRequestDto,CommonUtils.getRemoteHost(request));
    }


    public void setRetrievePasswordService(RetrievePasswordService retrievePasswordService) {
        this.retrievePasswordService = retrievePasswordService;
    }
}
