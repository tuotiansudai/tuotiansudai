package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.PersonalInfoRequestDto;
import com.tuotiansudai.api.service.MobileAppPersonalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppAutoInvestPlanInfoController extends MobileAppBaseController {


    @RequestMapping(value = "/get/auto-invest-plan", method = RequestMethod.POST)
    public BaseResponseDto getPersonalInfoData(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());

        return null;

    }

}
