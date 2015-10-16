package com.ttsd.api.controller;

import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.core.annotations.Logger;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppRetrievePasswordService;
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
public class MobileAppRetrievePasswordController {
    @Logger
    Log log;

    @Resource(name = "mobileAppRetrievePasswordServiceImpl")
    private MobileAppRetrievePasswordService retrievePasswordService;

    /**
     * @function 找回密码
     * @param retrievePasswordRequestDto
     * @return BaseResponseDto
     */
    @RequestMapping(value = "/retrievepassword",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto retrievePassword(@RequestBody RetrievePasswordRequestDto retrievePasswordRequestDto){
        try {
           return retrievePasswordService.retrievePassword(retrievePasswordRequestDto);
        } catch (UserNotFoundException e) {
            BaseResponseDto dto = new BaseResponseDto();
            dto.setCode(ReturnMessage.USER_ID_NOT_EXIST.getCode());
            dto.setMessage(ReturnMessage.USER_ID_NOT_EXIST.getMsg());
            log.error(e.getLocalizedMessage(),e);
            return dto;
        }
    }

    /**
     * @function 校验手机验证码是否正确
     * @param retrievePasswordRequestDto
     * @return BaseResponseDto
     */
    @RequestMapping(value = "/validatecaptcha",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto validateAuthCode(@RequestBody RetrievePasswordRequestDto retrievePasswordRequestDto){
        return retrievePasswordService.validateAuthCode(retrievePasswordRequestDto);
    }

    /**
     * @function 发送手机验证码
     * @param retrievePasswordRequestDto
     * @param request
     * @return BaseResponseDto
     */
    @RequestMapping(value = "/retrievepassword/sendsms")
    @ResponseBody
    public BaseResponseDto sendSMS(@RequestBody RetrievePasswordRequestDto retrievePasswordRequestDto,HttpServletRequest request){
        return retrievePasswordService.sendSMS(retrievePasswordRequestDto,CommonUtils.getRemoteHost(request));
    }
}
