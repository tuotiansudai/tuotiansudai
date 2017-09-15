package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.CreditLoanRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/credit-loan")
public class CreditLoanRechargeController {

    @Autowired
    private CreditLoanRechargeService creditLoanRecharge;


    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> creditLoanRecharge(@Valid @RequestBody CreditLoanRechargeDto dto) {
        return creditLoanRecharge.creditLoanRecharge(dto);
    }

    @RequestMapping(value = "/no-password-recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> creditLoanRechargeNoPwd(@Valid @RequestBody CreditLoanRechargeDto dto) {
        return creditLoanRecharge.creditLoanRechargeNoPwd(dto);
    }
}
