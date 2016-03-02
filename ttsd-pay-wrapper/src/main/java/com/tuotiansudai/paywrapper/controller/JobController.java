package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.coupon.service.CouponLoanOutService;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.paywrapper.service.RepayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping(value = "/job")
public class JobController {

    static Logger logger = Logger.getLogger(JobController.class);

    @Autowired
    private LoanService loanService;

    @Autowired
    private InvestService investService;

    @Autowired
    private CouponLoanOutService couponLoanOutService;

    @Resource(name = "normalRepayServiceImpl")
    private RepayService normalRepayService;

    @Resource(name = "advanceRepayServiceImpl")
    private RepayService advanceRepayService;

    @ResponseBody
    @RequestMapping(value = "/async_invest_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncInvestNotify() {
        return this.investService.asyncInvestCallback();
    }

    @RequestMapping(value = "/post_normal_repay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> postNormalRepay(@RequestBody long loanRepayId) {
        boolean isSuccess = normalRepayService.postRepayCallback(loanRepayId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(isSuccess);
        dto.setData(dataDto);
        return dto;
    }

    @RequestMapping(value = "/post_advance_repay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> postAdvanceRepay(@RequestBody long loanRepayId) {
        boolean isSuccess = advanceRepayService.postRepayCallback(loanRepayId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(isSuccess);
        dto.setData(dataDto);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/loan-out-success-notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> loanOut(@RequestBody long loanId) {
        boolean isSuccess = loanService.postLoanOut(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/auto-loan-out-after-raising-complete", method = RequestMethod.POST)
    public BaseDto<PayDataDto> autoLoanOutAfterRaisingComplete(@RequestBody long loanId) {
        return loanService.loanOut(loanId);
    }

    @ResponseBody
    @RequestMapping(value = "/sed-red-envelope-after-loan-out", method = RequestMethod.POST)
    public void sendRedEnvelopeAfterLoanOut(@RequestBody long loanId) {
        couponLoanOutService.sendRedEnvelope(loanId);
    }
}