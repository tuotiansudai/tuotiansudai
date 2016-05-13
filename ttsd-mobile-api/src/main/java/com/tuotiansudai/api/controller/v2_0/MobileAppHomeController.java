package com.tuotiansudai.api.controller.v2_0;

import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("com.tuotiansudai.api.controller.v2_0.MobileAppHomeController")
public class MobileAppHomeController extends MobileAppBaseController {

    @Autowired
    private MobileAppLoanListService mobileAppLoanListService;

    @RequestMapping(value = "/get/index", method = RequestMethod.POST)
    public BaseResponseDto index(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppLoanListService.generateIndexLoan(baseParamDto);
    }

}