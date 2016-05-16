package com.tuotiansudai.api.controller.v2_0;

import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.dto.v2_0.BaseResponseDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanListV2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppHomeV2Controller extends MobileAppBaseController {

    @Autowired
    private MobileAppLoanListV2Service mobileAppLoanListV2Service;

    @RequestMapping(value = "/get/index", method = RequestMethod.POST)
    public BaseResponseDto index(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId("she11njiaojiao111");
        return mobileAppLoanListV2Service.generateIndexLoan(baseParamDto);
    }

}