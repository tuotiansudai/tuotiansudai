package com.tuotiansudai.api.controller.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.LoanListResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppLoanListV2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "V2.0首页API")
public class MobileAppHomeV2Controller extends MobileAppBaseController {

    @Autowired
    private MobileAppLoanListV2Service mobileAppLoanListV2Service;

    @RequestMapping(value = "/get/index", method = RequestMethod.POST)
    @ApiOperation("首页")
    public BaseResponseDto<LoanListResponseDataDto> index() {
        return mobileAppLoanListV2Service.generateIndexLoan(getLoginName());
    }

}