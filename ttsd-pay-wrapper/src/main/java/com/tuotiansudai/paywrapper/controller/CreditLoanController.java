package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.credit.CreditLoanActivateAccountService;
import com.tuotiansudai.paywrapper.credit.CreditLoanOutService;
import com.tuotiansudai.paywrapper.credit.CreditLoanRepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/credit-loan")
public class CreditLoanController {

    private static Logger logger = Logger.getLogger(CreditLoanController.class);

    private final CreditLoanOutService creditLoanOutService;

    private final CreditLoanRepayService creditLoanRepayService;

    private final CreditLoanActivateAccountService creditLoanActivateAccountService;

    @Autowired
    public CreditLoanController(CreditLoanOutService creditLoanOutService, CreditLoanRepayService creditLoanRepayService, CreditLoanActivateAccountService creditLoanActivateAccountService) {
        this.creditLoanOutService = creditLoanOutService;
        this.creditLoanRepayService = creditLoanRepayService;
        this.creditLoanActivateAccountService = creditLoanActivateAccountService;
    }

    @RequestMapping(value = "/loan-out/{orderId}/mobile/{mobile}/amount/{amount}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> loanOut(@PathVariable long orderId,
                                       @PathVariable String mobile,
                                       @PathVariable long amount) {
        return creditLoanOutService.loanOut(orderId, mobile, amount);
    }

    @RequestMapping(value = "/password-repay/{orderId}/mobile/{mobile}/amount/{amount}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> passwordRepay(@PathVariable long orderId,
                                                 @PathVariable String mobile,
                                                 @PathVariable long amount) {
        return creditLoanRepayService.passwordRepay(orderId, mobile, amount);
    }

    @RequestMapping(value = "/password/activate-account/{mobile}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> passwordActivateAccount(@PathVariable String mobile) {
        return creditLoanActivateAccountService.passwordActivateAccount(mobile);
    }

    @RequestMapping(value = "/no-password/activate-account/{mobile}", method = RequestMethod.POST)
    @ResponseBody
    public  BaseDto<PayDataDto> noPasswordActivateAccount(@PathVariable String mobile) {
        return creditLoanActivateAccountService.noPasswordActivateAccount(mobile);
    }
}
