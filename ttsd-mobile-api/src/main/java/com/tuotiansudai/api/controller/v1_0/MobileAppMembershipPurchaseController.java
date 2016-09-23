package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPriceResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPurchaseRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppMembershipPurchaseController extends MobileAppBaseController {

    @Autowired
    private MobileAppMembershipPurchaseService mobileAppMembershipPurchaseService;

    @RequestMapping(value = "/get/membership-price-list", method = RequestMethod.POST)
    public BaseResponseDto<MembershipPriceResponseDto> getMembershipPrice() {
        return mobileAppMembershipPurchaseService.getMembershipPrice();
    }

    @RequestMapping(value = "/membership-purchase", method = RequestMethod.POST)
    public BaseResponseDto purchase(@RequestBody MembershipPurchaseRequestDto requestDto) {
        return mobileAppMembershipPurchaseService.purchase(requestDto);
    }
}
