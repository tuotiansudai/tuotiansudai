package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.paywrapper.service.CreditLoanRechargeService;
import com.tuotiansudai.paywrapper.service.SystemRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class CreditLoanRechargeController {

    @Autowired
    private CreditLoanRechargeService creditLoanRecharge;


    @RequestMapping(value = "/credit-loan-recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> creditLoanRecharge(@Valid @RequestBody InvestDto dto) {
        return creditLoanRecharge.creditLoanRecharge(dto);
    }

    @RequestMapping(value = "/no-password-credit-loan-recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> creditLoanRechargeNoPwd(@Valid @RequestBody InvestDto dto) {
        return creditLoanRecharge.creditLoanRechargeNoPwd(dto);
    }
}
