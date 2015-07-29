package com.ttsd.api.controller;

import com.ttsd.api.dto.LogInRequestDto;
import com.ttsd.api.dto.LogInResponseDto;
import com.ttsd.api.service.MobileLogInAppService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileLogInAppController {
    @Resource
    private MobileLogInAppService mobileLogInAppService;

    @ResponseBody
    @RequestMapping(value="/LogIn",method = RequestMethod.POST)
    public LogInResponseDto login(@RequestBody LogInRequestDto logInRequestDto){
       return mobileLogInAppService.logIn(logInRequestDto);
    }

}
