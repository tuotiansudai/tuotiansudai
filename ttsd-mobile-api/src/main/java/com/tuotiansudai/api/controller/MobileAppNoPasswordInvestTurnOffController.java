package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NoPasswordInvestTurnOffRequestDto;
import com.tuotiansudai.api.service.MobileAppNoPasswordInvestTurnOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppNoPasswordInvestTurnOffController extends MobileAppBaseController {
    @Autowired
    private MobileAppNoPasswordInvestTurnOffService mobileAppNoPasswordInvestTurnOffService;

    @RequestMapping(value = "/no-password-invest/turn-off", method = RequestMethod.POST)
    public BaseResponseDto noPasswordInvestTurnOff(@RequestBody NoPasswordInvestTurnOffRequestDto noPasswordInvestTurnOffRequestDto) {
        noPasswordInvestTurnOffRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNoPasswordInvestTurnOffService.noPasswordInvestTurnOff(noPasswordInvestTurnOffRequestDto);
    }

}
