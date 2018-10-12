package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestDetailRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "出借管理-个人出借详情")
public class MobileAppInvestDetailController extends MobileAppBaseController {

    @Autowired
    private MobileAppInvestDetailService mobileAppMyInvestDetailService;

    @RequestMapping(value = "/get/userinvest", method = RequestMethod.POST)
    @ApiOperation("出借管理-个人出借详情")
    public BaseResponseDto queryUserInvestList(@RequestBody InvestDetailRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppMyInvestDetailService.generateUserInvestDetail(requestDto);
    }
}
