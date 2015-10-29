package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PersonalInfoRequestDto;
import com.tuotiansudai.api.service.MobileAppPersonalInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MobileAppPersonalInfoController {
    @Resource
    private MobileAppPersonalInfoService mobileAppPersonalInfoService;

    @RequestMapping(value = "/get/user", method = RequestMethod.POST)
    public BaseResponseDto getPersonalInfoData(@RequestBody PersonalInfoRequestDto personalInfoRequestDto) {
        return mobileAppPersonalInfoService.getPersonalInfoData(personalInfoRequestDto);
    }

}
