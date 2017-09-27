package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.credit.CreditLoanOutService;
import com.tuotiansudai.paywrapper.credit.CreditLoanRechargeService;
import com.tuotiansudai.paywrapper.credit.CreditLoanRepayService;
import com.tuotiansudai.paywrapper.credit.CreditLoanTransferAgentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping(value = "/credit-loan")
public class CreditLoanController {

    private static Logger logger = Logger.getLogger(CreditLoanController.class);

    private final CreditLoanOutService creditLoanOutService;

    private final CreditLoanRepayService creditLoanRepayService;

    private final CreditLoanRechargeService creditLoanRechargeService;

    private final CreditLoanTransferAgentService creditLoanTransferAgentService;

    @Autowired
    public CreditLoanController(CreditLoanOutService creditLoanOutService,
                                CreditLoanRepayService creditLoanRepayService,
                                CreditLoanRechargeService creditLoanRechargeService,
                                CreditLoanTransferAgentService creditLoanTransferAgentService) {
        this.creditLoanOutService = creditLoanOutService;
        this.creditLoanRepayService = creditLoanRepayService;
        this.creditLoanRechargeService = creditLoanRechargeService;
        this.creditLoanTransferAgentService = creditLoanTransferAgentService;
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

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> creditLoanRecharge(@Valid @RequestBody CreditLoanRechargeDto dto) {
        return creditLoanRechargeService.creditLoanRecharge(dto);
    }

    @RequestMapping(value = "/no-password-recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> creditLoanRechargeNoPwd(@Valid @RequestBody CreditLoanRechargeDto dto) {
        return creditLoanRechargeService.creditLoanRechargeNoPwd(dto);
    }

    @RequestMapping(value = "/transfer-agent", method = RequestMethod.POST)
    @ResponseBody
    public void creditLoanTransferAgent() {
        creditLoanTransferAgentService.creditLoanTransferAgent();
    }
}
