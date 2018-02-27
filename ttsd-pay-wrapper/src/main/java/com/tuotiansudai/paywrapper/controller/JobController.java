package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.loanout.CouponRepayService;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.loanout.LoanService;
import com.tuotiansudai.paywrapper.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/job")
public class JobController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private InvestService investService;

    @Autowired
    InvestTransferPurchaseService investTransferPurchaseService;

    @ResponseBody
    @RequestMapping(value = "/async_invest_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncInvestNotify(@RequestBody long orderId) {
        return this.investService.asyncInvestCallback(orderId);
    }

    @ResponseBody
    @RequestMapping(value = "/async_invest_transfer_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncInvestTransferNotify(@RequestBody long notifyRequestId) {
        return this.investTransferPurchaseService.asyncPurchaseCallback(notifyRequestId);
    }

    @ResponseBody
    @RequestMapping(value = "/auto-loan-out-after-raising-complete", method = RequestMethod.POST)
    public BaseDto<PayDataDto> autoLoanOutAfterRaisingComplete(@RequestBody long loanId) {
        return loanService.loanOut(loanId);
    }

}
