package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PersonalInfoRequestDto;
import com.tuotiansudai.api.service.MobileAppPersonalInfoService;
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
