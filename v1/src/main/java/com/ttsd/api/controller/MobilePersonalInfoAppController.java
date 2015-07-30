package com.ttsd.api.controller;

import com.ttsd.api.dto.PersonalInfoRequestDto;
import com.ttsd.api.dto.PersonalInfoResponseDto;
import com.ttsd.api.service.MobilePersonalInfoAppService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class MobilePersonalInfoAppController {
    @Resource
    private MobilePersonalInfoAppService mobilePersonalInfoAppService;

    @ResponseBody
    @RequestMapping(value="/accesspersonalinfo",method = RequestMethod.POST)
    public PersonalInfoResponseDto login(@RequestBody PersonalInfoRequestDto personalInfoRequestDto){
       return mobilePersonalInfoAppService.getPersonalInfoData(personalInfoRequestDto);
    }

}
