package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.service.CreditLoanRepayNoPwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/credit-loan-repay")
public class CreditLoanNoPwdRepayController {

    @Autowired
    private CreditLoanRepayNoPwdService creditLoanRepayNoPwdService;


    @RequestMapping(value = "/no-pwd/{orderId}/mobile/{mobile}/amount/{amount}" , method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> creditLoanRepayNoPwd(@PathVariable long orderId,
                                 @PathVariable String mobile,
                                 @PathVariable long amount){

        return creditLoanRepayNoPwdService.creditLoanRepayNoPwd(orderId, mobile, amount);

    }

}
