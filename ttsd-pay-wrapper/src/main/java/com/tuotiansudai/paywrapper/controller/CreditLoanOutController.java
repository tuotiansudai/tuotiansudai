package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.paywrapper.service.CreditLoanOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CreditLoanOutController {

    @Autowired
    private CreditLoanOutService creditLoanOutService;

    @RequestMapping(value = "/credit-loan-out", method = RequestMethod.POST)
    @ResponseBody
    public void creditLoanRecharge() {
        creditLoanOutService.creditLoanOut();
    }
}
