package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppExperienceInvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MobileAppExperienceInvestController extends MobileAppBaseController {

    @Autowired
    private MobileAppExperienceInvestService mobileAppExperienceInvestService;

    @RequestMapping(value = "/experience-invest", method = RequestMethod.POST)
    public BaseResponseDto experienceInvest(@RequestBody InvestRequestDto investRequestDto) {
        investRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppExperienceInvestService.experienceInvest(investRequestDto);
    }

}
