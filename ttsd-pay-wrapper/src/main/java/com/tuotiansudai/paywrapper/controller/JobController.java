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

    @Autowired
    private NormalRepayService normalRepayService;

    @Autowired
    private AdvanceRepayService advanceRepayService;

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private ExtraRateService extraRateService;

    @ResponseBody
    @RequestMapping(value = "/async_invest_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncInvestNotify(@RequestBody long notifyRequestId) {
        return this.investService.asyncInvestCallback(notifyRequestId);
    }

    @ResponseBody
    @RequestMapping(value = "/async_normal_repay_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncNormalRepayNotify(@RequestBody long notifyRequestId) {
        return this.normalRepayService.asyncNormalRepayPaybackCallback(notifyRequestId);
    }

    @ResponseBody
    @RequestMapping(value = "/async_advance_repay_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncAdvanceRepayNotify(@RequestBody long notifyRequestId) {
        return this.advanceRepayService.asyncAdvanceRepayPaybackCallback(notifyRequestId);
    }

    @ResponseBody
    @RequestMapping(value = "/async_coupon_repay_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncCouponRepayNotify() {
        return this.couponRepayService.asyncCouponRepayCallback();
    }

    @ResponseBody
    @RequestMapping(value = "/async_invest_transfer_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncInvestTransferNotify(@RequestBody long notifyRequestId) {
        return this.investTransferPurchaseService.asyncPurchaseCallback(notifyRequestId);
    }

    @RequestMapping(value = "/post_normal_repay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> postNormalRepay(@RequestBody long loanRepayId) {
        boolean isSuccess = normalRepayService.paybackInvest(loanRepayId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(isSuccess);
        dto.setData(dataDto);
        return dto;
    }

    @RequestMapping(value = "/post_advance_repay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> postAdvanceRepay(@RequestBody long loanRepayId) {
        boolean isSuccess = advanceRepayService.paybackInvest(loanRepayId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(isSuccess);
        dto.setData(dataDto);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/auto-loan-out-after-raising-complete", method = RequestMethod.POST)
    public BaseDto<PayDataDto> autoLoanOutAfterRaisingComplete(@RequestBody long loanId) {
        return loanService.loanOut(loanId);
    }

    @ResponseBody
    @RequestMapping(value = "/async_extra_rate_invest_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncExtraRateInvestNotify() {
        return this.extraRateService.asyncExtraRateInvestCallback();
    }

}
