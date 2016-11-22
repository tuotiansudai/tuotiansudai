package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestTurnOnRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestTurnOnService;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MobileAppNoPasswordInvestTurnOnController extends MobileAppBaseController {
    @Autowired
    private MobileAppNoPasswordInvestTurnOnService mobileAppNoPasswordInvestTurnOnService;

    @RequestMapping(value = "/no-password-invest/turn-on", method = RequestMethod.POST)
    public BaseResponseDto noPasswordInvestTurnOn(@RequestBody NoPasswordInvestTurnOnRequestDto noPasswordInvestTurnOnRequestDto, HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        noPasswordInvestTurnOnRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNoPasswordInvestTurnOnService.noPasswordInvestTurnOn(noPasswordInvestTurnOnRequestDto, ip);
    }
}
