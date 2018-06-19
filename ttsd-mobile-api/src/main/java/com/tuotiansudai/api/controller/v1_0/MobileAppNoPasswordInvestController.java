package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestService;
import com.tuotiansudai.spring.LoginUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "查询是否开通免密投资功能")
public class MobileAppNoPasswordInvestController extends MobileAppBaseController {
    @Autowired
    private MobileAppNoPasswordInvestService mobileAppNoPasswordInvestService;

    @RequestMapping(value = "/get/no-password-invest", method = RequestMethod.POST)
    @ApiOperation("查询是否开通免密投资功能")
    public BaseResponseDto<NoPasswordInvestResponseDataDto> getNoPasswordInvestData() {
        return mobileAppNoPasswordInvestService.getNoPasswordInvestData(LoginUserInfo.getLoginName());
    }

}
