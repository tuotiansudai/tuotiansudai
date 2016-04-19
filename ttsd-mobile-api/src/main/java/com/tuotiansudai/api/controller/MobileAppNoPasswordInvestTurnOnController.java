package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppNoPasswordInvestTurnOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppNoPasswordInvestTurnOnController extends MobileAppBaseController {
    @Autowired
    private MobileAppNoPasswordInvestTurnOnService mobileAppNoPasswordInvestTurnOnService;

    @RequestMapping(value = "/no-password-invest/turn-on", method = RequestMethod.POST)
    public BaseResponseDto noPasswordInvestTurnOn(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNoPasswordInvestTurnOnService.noPasswordInvestTurnOn(baseParamDto);
    }

}
