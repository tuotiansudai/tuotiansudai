package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.dto.MembershipPurchaseDto;
import com.tuotiansudai.paywrapper.service.MembershipPurchasePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MembershipPurchaseController {

    @Autowired
    private MembershipPurchasePayService membershipPurchasePayService;

    @RequestMapping(value = "/membership-purchase", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> membershipPurchase(@RequestBody MembershipPurchaseDto dto) {
        return membershipPurchasePayService.purchase(dto);
    }
}
