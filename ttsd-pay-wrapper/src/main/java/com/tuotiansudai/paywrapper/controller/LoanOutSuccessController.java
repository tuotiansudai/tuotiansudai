package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.loanout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/loan-out")
public class LoanOutSuccessController {

    @Autowired
    private CouponLoanOutService couponLoanOutService;

    @Autowired
    private ReferrerRewardService referrerRewardService;

    @Autowired
    private RepayGeneratorService repayGeneratorService;

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private LoanOutInvestCalculationService loanOutInvestCalculationService;

    @Autowired
    private AchievementCouponService achievementCouponService;

    @ResponseBody
    @RequestMapping(value = "/assign-achievement-coupon-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> assignInvestAchievementUserCoupon(@RequestBody long loanId) {
        boolean isSuccess = achievementCouponService.assignInvestAchievementUserCoupon(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/generate-repay-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> generateRepay(@RequestBody long loanId) {
        PayDataDto dataDto = new PayDataDto();
        dataDto.setStatus(true);
        BaseDto<PayDataDto> dto = new BaseDto<>(dataDto);
        try {
            repayGeneratorService.generateRepay(loanId);
        } catch (Exception e) {
            dataDto.setStatus(false);
            dataDto.setMessage(e.getLocalizedMessage());
        }

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
    @RequestMapping(value = "/transfer-referrer-reward-callback", method = RequestMethod.POST)
    public BaseDto<PayDataDto> transferReferrerRewardCallBack(@RequestBody long investReferrerRewardId) {
        boolean isSuccess = true;
        try {
            referrerRewardService.transferReferrerCallBack(investReferrerRewardId);
        } catch (AmountTransferException e) {
            isSuccess = false;
        }
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/transfer-red-envelop-callback", method = RequestMethod.POST)
    public BaseDto<PayDataDto> transferRedEnvelopForCallback(@RequestBody long userCouponId) {
        boolean success = couponLoanOutService.sendRedEnvelopTransferInBalanceCallBack(userCouponId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(success);
        return dto;
    }

}
