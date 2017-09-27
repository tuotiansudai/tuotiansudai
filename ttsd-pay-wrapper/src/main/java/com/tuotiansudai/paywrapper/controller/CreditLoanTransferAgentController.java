package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.paywrapper.service.CreditLoanTransferAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CreditLoanTransferAgentController {

    @Autowired
    private CreditLoanTransferAgentService creditLoanOutService;

    @RequestMapping(value = "/credit-loan-transfer-agent", method = RequestMethod.POST)
    @ResponseBody
    public void creditLoanTransferAgent() {
        creditLoanOutService.creditLoanTransferAgent();
    }
}
