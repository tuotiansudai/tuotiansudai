package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppNoPasswordInvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppNoPasswordInvestController extends MobileAppBaseController {
    @Autowired
    private MobileAppNoPasswordInvestService mobileAppNoPasswordInvestService;

    @RequestMapping(value = "/get/no-password-invest", method = RequestMethod.POST)
    public BaseResponseDto getNoPasswordInvestData(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNoPasswordInvestService.getNoPasswordInvestData(baseParamDto);
    }

}
