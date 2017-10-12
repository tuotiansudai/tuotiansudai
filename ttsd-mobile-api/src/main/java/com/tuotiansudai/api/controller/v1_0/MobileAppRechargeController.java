package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(description = "银行卡充值")
public class MobileAppRechargeController extends MobileAppBaseController {
    @Autowired
    private MobileAppRechargeService mobileAppRechargeService;

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ApiOperation("快捷充值")
    public BaseResponseDto<BankCardResponseDto> recharge(@Valid @RequestBody BankCardRequestDto bankCardRequestDto) {
        bankCardRequestDto.getBaseParam().setUserId(getLoginName());
        bankCardRequestDto.setUserId(getLoginName());
        bankCardRequestDto.setIsOpenFastPayment(true);
        return mobileAppRechargeService.recharge(bankCardRequestDto);
    }

    @RequestMapping(value = "/get/bank-limit", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("银行卡限额")
    public BaseResponseDto<BankLimitResponseDataDto> getRechargeLimit(@RequestBody BankLimitRequestDto bankLimitRequestDto) {
        return mobileAppRechargeService.getBankLimit(bankLimitRequestDto);
    }
}
