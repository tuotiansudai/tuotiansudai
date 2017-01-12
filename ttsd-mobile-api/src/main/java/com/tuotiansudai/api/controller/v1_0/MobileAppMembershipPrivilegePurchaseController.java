package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPrivilegePurchaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "增值特权购买")
public class MobileAppMembershipPrivilegePurchaseController extends MobileAppBaseController{
    @Autowired
    private MobileAppMembershipPrivilegePurchaseService mobileAppMembershipPrivilegePurchaseService;

    @RequestMapping(value = "/get/membership-privilege-price", method = RequestMethod.POST)
    @ApiOperation("增值特权购买价格列表")
    public BaseResponseDto<MembershipPrivilegePriceResponseDto> obtainMembershipPrivilegePrices(@RequestBody BaseParamDto baseParam){
        BaseResponseDto<MembershipPrivilegePriceResponseDto> baseResponseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        baseResponseDto.setData(mobileAppMembershipPrivilegePurchaseService.obtainMembershipPrivilegePrices());
        return baseResponseDto;
    }

    @RequestMapping(value = "/get/membership-privilege-purchase", method = RequestMethod.POST)
    @ApiOperation("增值特权购买")
    public BaseResponseDto<MembershipPrivilegePurchaseResponseDataDto> purchase(@RequestBody MembershipPrivilegePurchaseRequestDto requestDto) {
        requestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppMembershipPrivilegePurchaseService.purchase(requestDto);
    }


}
