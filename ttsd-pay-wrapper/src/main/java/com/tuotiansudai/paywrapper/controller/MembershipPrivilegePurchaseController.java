package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.membership.dto.MembershipPrivilegePurchaseDto;
import com.tuotiansudai.paywrapper.service.MembershipPrivilegePurchasePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MembershipPrivilegePurchaseController{

    @Autowired
    private MembershipPrivilegePurchasePayService membershipPrivilegePurchasePayService;

    @RequestMapping(value = "/membership-privilege-purchase", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> membershipPurchase(@RequestBody MembershipPrivilegePurchaseDto dto) {
        return membershipPrivilegePurchasePayService.purchase(dto);
    }
}
