package com.tuotiansudai.api.controller.v3_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v3_0.MobileAppLoanListV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppHomeV3Controller extends MobileAppBaseController {
    @Autowired
    private MobileAppLoanListV3Service mobileAppLoanListV3Service;

    @RequestMapping(value = "/get/index", method = RequestMethod.POST)
    public BaseResponseDto index() {
        return mobileAppLoanListV3Service.generateIndexLoan(getLoginName());
    }
}
