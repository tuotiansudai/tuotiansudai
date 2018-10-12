package com.tuotiansudai.api.controller.v3_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppInvestListsV3Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "V3.0我的出借")
public class MobileAppInvestListsV3Controller extends MobileAppBaseController{

    @Autowired
    private MobileAppInvestListsV3Service mobileAppInvestListsService;

    @RequestMapping(value = "/get/user-invests", method = RequestMethod.POST)
    @ApiOperation("我的出借")
    public BaseResponseDto<UserInvestListResponseDataDto> queryUserInvestLists(@RequestBody UserInvestListRequestDto userInvestListRequestDto) {
        userInvestListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppInvestListsService.generateUserInvestList(userInvestListRequestDto);
    }
}
