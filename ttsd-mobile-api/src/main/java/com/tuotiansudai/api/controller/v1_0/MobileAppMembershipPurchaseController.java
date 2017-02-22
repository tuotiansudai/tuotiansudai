package com.tuotiansudai.api.controller.v1_0;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPriceResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "会员购买")
public class MobileAppMembershipPurchaseController extends MobileAppBaseController {

    @RequestMapping(value = "/get/membership-price-list", method = RequestMethod.POST)
    @ApiOperation("价格列表")
    public BaseResponseDto<MembershipPriceResponseDto> getMembershipPrice() {
        MembershipPriceResponseDto membershipPriceResponseDto = new MembershipPriceResponseDto(Lists.newArrayList());
        BaseResponseDto<MembershipPriceResponseDto> baseResponseDto = new BaseResponseDto(ReturnMessage.SUCCESS.getCode(),ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(membershipPriceResponseDto);
        return baseResponseDto;
    }
}