package com.tuotiansudai.api.controller.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v2_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppInvestListsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "V2.0我的出借")
public class MobileAppInvestListsController extends MobileAppBaseController{

    @Autowired
    private MobileAppInvestListsService mobileAppInvestListsService;

    @RequestMapping(value = "/get/user-invests", method = RequestMethod.POST)
    @ApiOperation("我的出借")
    public BaseResponseDto<UserInvestListResponseDataDto> queryUserInvestLists(@RequestBody UserInvestListRequestDto userInvestListRequestDto) {
        userInvestListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppInvestListsService.generateUserInvestList(userInvestListRequestDto);
    }

}
