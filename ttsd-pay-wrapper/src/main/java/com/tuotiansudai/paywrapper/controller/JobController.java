package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.coupon.service.CouponLoanOutService;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.extrarate.service.LoanOutInvestCalculationService;
import com.tuotiansudai.paywrapper.service.*;
import com.tuotiansudai.paywrapper.service.impl.ReferrerRewardServiceImpl;
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
    private CouponLoanOutService couponLoanOutService;

    @Autowired
    private NormalRepayService normalRepayService;

    @Autowired
    private AdvanceRepayService advanceRepayService;

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private ExtraRateService extraRateService;

    @Autowired
    private ReferrerRewardService referrerRewardService;

    @Autowired
    private RepayGeneratorService repayGeneratorService;

    @Autowired
    private LoanOutInvestCalculationService loanOutInvestCalculationService;

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
    @RequestMapping(value = "/send-red-envelope-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> sendRedEnvelopeAfterLoanOut(@RequestBody long loanId) {
        boolean isSuccess = couponLoanOutService.sendRedEnvelope(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/async_extra_rate_invest_notify", method = RequestMethod.POST)
    public BaseDto<PayDataDto> asyncExtraRateInvestNotify() {
        return this.extraRateService.asyncExtraRateInvestCallback();
    }

    @ResponseBody
    @RequestMapping(value = "/referrer-reward-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> sendRewardReferrer(@RequestBody long loanId) {
        boolean isSuccess = referrerRewardService.rewardReferrer(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/create-anxin-contract-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> createAnXinContract(@RequestBody long loanId) {
        boolean isSuccess = loanService.createAnxinContractJob(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/generate-repay-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> generateRepay(@RequestBody long loanId) {
        boolean isSuccess = true;
        try {
            repayGeneratorService.generateRepay(loanId);
        }catch (Exception e){
            isSuccess = false;
        }
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/generate-coupon-repay-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> generateCouponRepay(@RequestBody long loanId) {
        boolean isSuccess = couponRepayService.generateCouponRepay(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/generate-extra-rate-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> generateExtraRate(@RequestBody long loanId) {
        boolean isSuccess = loanOutInvestCalculationService.rateIncreases(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/process-notify-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> processNotifyForLoanOut(@RequestBody long loanId) {
        boolean isSuccess = loanService.processNotifyForLoanOut(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/assign-achievement-coupon-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> assignInvestAchievementUserCoupon(@RequestBody long loanId) {
        boolean isSuccess = couponLoanOutService.assignInvestAchievementUserCoupon(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }


}
