package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.PersonalInfoRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppPersonalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppPersonalInfoController extends MobileAppBaseController {
    @Autowired
    private MobileAppPersonalInfoService mobileAppPersonalInfoService;

    @RequestMapping(value = "/get/user", method = RequestMethod.POST)
    public BaseResponseDto getPersonalInfoData(@RequestBody PersonalInfoRequestDto personalInfoRequestDto) {
        personalInfoRequestDto.getBaseParam().setUserId(getLoginName());
        personalInfoRequestDto.setUserName(getLoginName());
        return mobileAppPersonalInfoService.getPersonalInfoData(personalInfoRequestDto);
    }

}
