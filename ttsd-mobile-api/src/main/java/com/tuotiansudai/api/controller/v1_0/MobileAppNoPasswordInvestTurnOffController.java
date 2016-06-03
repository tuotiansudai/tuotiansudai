package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOffRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOffService;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MobileAppNoPasswordInvestTurnOffController extends MobileAppBaseController {
    @Autowired
    private MobileAppNoPasswordInvestTurnOffService mobileAppNoPasswordInvestTurnOffService;

    @RequestMapping(value = "/no-password-invest/turn-off", method = RequestMethod.POST)
    public BaseResponseDto noPasswordInvestTurnOff(@RequestBody NoPasswordInvestTurnOffRequestDto noPasswordInvestTurnOffRequestDto, HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        noPasswordInvestTurnOffRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNoPasswordInvestTurnOffService.noPasswordInvestTurnOff(noPasswordInvestTurnOffRequestDto, ip);
    }

}
