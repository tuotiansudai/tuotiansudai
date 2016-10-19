package com.tuotiansudai.web.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDetailDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanDetailService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import coupon.dto.UserCouponDto;
import coupon.service.CouponAlertService;
import coupon.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;


@Controller
@RequestMapping(value = "/loan")
public class LoanDetailController {

    @Autowired
    private LoanDetailService loanDetailService;

    @Autowired
    private CouponAlertService couponAlertService;

    @Autowired
    private InvestService investService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getLoanDetail(@PathVariable long loanId) {
        LoanDetailDto loanDetail = loanDetailService.getLoanDetail(LoginUserInfo.getLoginName(), loanId);
        if (loanDetail == null) {
            return new ModelAndView("/error/404");
        }
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(LoginUserInfo.getLoginName());
        ModelAndView modelAndView = new ModelAndView("/loan", "responsive", true);
        modelAndView.addObject("loan", loanDetail);
        modelAndView.addObject("coupons", userCouponService.getInvestUserCoupons(LoginUserInfo.getLoginName(), loanId));
        modelAndView.addObject("maxBenefitUserCoupon",
                investService.getMaxBenefitUserCoupon(LoginUserInfo.getLoginName(), loanId, AmountConverter.convertStringToCent(loanDetail.getInvestor().getMaxAvailableInvestAmount())));
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE)));
        modelAndView.addObject("extraLoanRates", loanDetailService.getExtraLoanRate(loanId));
        boolean membershipPreferenceValid = false;
        int membershipLevel = 0;
        if (null != membershipModel) {
            membershipLevel = membershipModel.getLevel();
            membershipPreferenceValid = membershipModel.getFee() < defaultFee;
        }
        modelAndView.addObject("membershipLevel", membershipLevel);
        modelAndView.addObject("membershipPreferenceValid", membershipPreferenceValid);
        return modelAndView;
    }

    @RequestMapping(value = "/{loanId:^\\d+$}/amount/{amount:^\\d+$}/max-benefit-user-coupon", method = RequestMethod.GET)
    @ResponseBody
    public String getMaxBenefitUserCoupon(@PathVariable long loanId, @PathVariable long amount) {
        UserCouponDto maxBenefitUserCoupon = investService.getMaxBenefitUserCoupon(LoginUserInfo.getLoginName(), loanId, amount);
        if (maxBenefitUserCoupon != null) {
            return String.valueOf(maxBenefitUserCoupon.getId());
        }
        return "";
    }

    @RequestMapping(value = "/{loanId:^(?!1$)\\d+$}/invests", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getInvestList(@PathVariable long loanId,
                                                        @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index) {
        return loanDetailService.getInvests(LoginUserInfo.getLoginName(), loanId, index, 10);
    }
}