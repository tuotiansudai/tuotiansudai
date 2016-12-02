package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPriceResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPurchaseRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPurchaseResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPurchaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "会员购买")
public class MobileAppMembershipPurchaseController extends MobileAppBaseController {

    @Autowired
    private MobileAppMembershipPurchaseService mobileAppMembershipPurchaseService;

    @RequestMapping(value = "/get/membership-price-list", method = RequestMethod.POST)
    @ApiOperation("价格列表")
    public BaseResponseDto<MembershipPriceResponseDto> getMembershipPrice() {
        return mobileAppMembershipPurchaseService.getMembershipPrice();
    }

    @RequestMapping(value = "/membership-purchase", method = RequestMethod.POST)
    @ApiOperation("购买会员")
    public BaseResponseDto<MembershipPurchaseResponseDataDto> purchase(@RequestBody MembershipPurchaseRequestDto requestDto) {
        return mobileAppMembershipPurchaseService.purchase(requestDto);
    }
}
