package com.ttsd.api.controller;

import com.ttsd.api.dto.PersonalInfoRequestDto;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.service.MobileAppPersonalInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobileAppPersonalInfoController {
    @Resource
    private MobileAppPersonalInfoService mobileAppPersonalInfoService;

    @ResponseBody
    @RequestMapping(value="/get/user",method = RequestMethod.POST)
    public BaseResponseDto getPersonalInfoData(@RequestBody PersonalInfoRequestDto personalInfoRequestDto){
       return mobileAppPersonalInfoService.getPersonalInfoData(personalInfoRequestDto);
    }

}
