package com.tuotiansudai.api.controller.v3_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.dto.v3_0.LoanListResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppLoanListV3Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "V3.0首页")
public class MobileAppHomeV3Controller extends MobileAppBaseController {

    static Logger logger = Logger.getLogger(MobileAppHomeV3Controller.class);

    @Autowired
    private MobileAppLoanListV3Service mobileAppLoanListV3Service;

    @RequestMapping(value = "/get/index", method = RequestMethod.POST)
    @ApiOperation("首页")
    public BaseResponseDto<LoanListResponseDataDto> index(@RequestBody BaseParamDto baseParamDto) {
        return mobileAppLoanListV3Service.generateIndexLoan(baseParamDto, getLoginName());
    }
}
