package com.tuotiansudai.paywrapper.controller;


import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.loanout.AchievementCouponService;
import com.tuotiansudai.paywrapper.loanout.CouponLoanOutService;
import com.tuotiansudai.paywrapper.loanout.CouponRepayService;
import com.tuotiansudai.paywrapper.loanout.LoanOutInvestCalculationService;
import com.tuotiansudai.paywrapper.loanout.LoanService;
import com.tuotiansudai.paywrapper.loanout.ReferrerRewardService;
import com.tuotiansudai.paywrapper.loanout.RepayGeneratorService;
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
    private LoanService loanService;

    @Autowired
    private ReferrerRewardService referrerRewardService;

    @Autowired
    private RepayGeneratorService repayGeneratorService;

    @Autowired
    private CouponRepayService couponRepayService;

    @Autowired
    private AnxinSignService anxinSignService;

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
    @RequestMapping(value = "/create-anxin-contract-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> createAnXinContract(@RequestBody long loanId) {
        BaseDto baseDto;
        try{
            baseDto = anxinSignService.createLoanContracts(loanId, false);
        }catch (Exception e){
            baseDto = new BaseDto(false);
        }
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(baseDto.isSuccess());
        return dto;
    }

    @ResponseBody
    @RequestMapping(value = "/query-anxin-contract-after-loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> queryAnXinContract(@RequestBody long loanId) {
        boolean result = anxinSignService.queryContract(loanId);
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(result);
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
    public BaseDto<PayDataDto> transferRedEnvelopForCallBack(@RequestBody long userCouponId) {
        boolean isSuccess = true;
        try {
            couponLoanOutService.sendRedEnvelopTransferInBalanceCallBack(userCouponId);
        } catch (AmountTransferException e) {
            isSuccess = false;
        }
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        dto.setData(dataDto);
        dataDto.setStatus(isSuccess);
        return dto;
    }

}
