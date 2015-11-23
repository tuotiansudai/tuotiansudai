package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BankCardRequestDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.MobileAppRechargeService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MobileAppRechargeController extends MobileAppBaseController {
    @Autowired
    private MobileAppRechargeService mobileAppRechargeService;

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public BaseResponseDto recharge(@RequestBody BankCardRequestDto bankCardRequestDto) {
        bankCardRequestDto.getBaseParam().setUserId(getLoginName());
        bankCardRequestDto.setUserId(getLoginName());
        bankCardRequestDto.setIsOpenFastPayment(true);
        return mobileAppRechargeService.recharge(bankCardRequestDto);
    }
}
