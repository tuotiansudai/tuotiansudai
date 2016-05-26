package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOnService;
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
