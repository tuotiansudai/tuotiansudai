package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BankCardRequestDto;
import com.tuotiansudai.api.dto.v1_0.BankLimitRequestDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/get/bank-limit", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponseDto getRechargeLimit(@RequestBody BankLimitRequestDto bankLimitRequestDto) {
        return mobileAppRechargeService.getBankLimit(bankLimitRequestDto);
    }
}
