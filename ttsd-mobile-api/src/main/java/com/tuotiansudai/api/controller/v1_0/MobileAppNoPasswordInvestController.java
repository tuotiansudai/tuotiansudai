package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.NoPasswordInvestResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppNoPasswordInvestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "查询是否开通免密出借功能")
public class MobileAppNoPasswordInvestController extends MobileAppBaseController {
    @Autowired
    private MobileAppNoPasswordInvestService mobileAppNoPasswordInvestService;

    @RequestMapping(value = "/get/no-password-invest", method = RequestMethod.POST)
    @ApiOperation("查询是否开通免密出借功能")
    public BaseResponseDto<NoPasswordInvestResponseDataDto> getNoPasswordInvestData(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppNoPasswordInvestService.getNoPasswordInvestData(baseParamDto);
    }

}
