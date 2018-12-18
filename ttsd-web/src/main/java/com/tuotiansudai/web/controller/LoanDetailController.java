package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDetailDto;
import com.tuotiansudai.dto.UserCouponDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.riskestimation.Estimate;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanDetailService;
import com.tuotiansudai.service.RiskEstimateService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/loan")
public class LoanDetailController {

    @Autowired
    private LoanDetailService loanDetailService;

    @Autowired
    private CouponAlertService couponAlertService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private InvestService investService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private RiskEstimateService riskEstimateService;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Value("${risk.estimate.limit.key}")
    private String riskEstimateLimitKey;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getLoanDetail(@PathVariable long loanId, HttpServletRequest request) {
        LoanDetailDto loanDetail = loanDetailService.getLoanDetail(LoginUserInfo.getLoginName(), loanId);
        if (loanDetail == null) {
            return new ModelAndView("/error/404");
        }
        MembershipModel membershipModel = userMembershipEvaluator.evaluate(LoginUserInfo.getLoginName());
        ModelAndView modelAndView = new ModelAndView("/loan", "responsive", true);
        modelAndView.addObject("loan", loanDetail);
        modelAndView.addObject("coupons", userCouponService.getInvestUserCoupons(LoginUserInfo.getLoginName(), loanId));
        modelAndView.addObject("maxBenefitUserCoupon",
                this.userCouponService.getMaxBenefitUserCoupon(LoginUserInfo.getLoginName(), loanId, AmountConverter.convertStringToCent(loanDetail.getInvestor().getMaxAvailableInvestAmount())));
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE)));
        modelAndView.addObject("extraLoanRates", loanDetailService.getExtraLoanRate(loanId));

        modelAndView.addObject("interestPerTenThousands", investService.estimateInvestIncome(loanId,
                membershipModel == null ? defaultFee : membershipModel.getFee(),
                LoginUserInfo.getLoginName(), 1000000, new Date()));
        BankCardModel passedBankCard = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());
        modelAndView.addObject("hasBankCard", passedBankCard != null);
        double investFeeRate = ProductType.EXPERIENCE == loanDetail.getProductType() ? this.defaultFee : membershipPrivilegePurchaseService.obtainServiceFee(LoginUserInfo.getLoginName());
        int membershipLevel = 0;
        if (null != membershipModel) {
            membershipLevel = membershipModel.getLevel();
        }
        modelAndView.addObject("membershipLevel", membershipLevel);
        modelAndView.addObject("investFeeRate", investFeeRate);
        modelAndView.addObject("availableInvestMoney", investService.availableInvestMoney(LoginUserInfo.getLoginName()));
        Estimate estimate=riskEstimateService.getEstimate(LoginUserInfo.getLoginName());
        modelAndView.addObject("estimate", estimate);
        modelAndView.addObject("estimateLimit", estimate == null?0:AmountConverter.convertStringToCent(redisWrapperClient.hget(riskEstimateLimitKey,estimate.name())));
        return modelAndView;
    }

    @RequestMapping(value = "/{loanId:^\\d+$}/amount/{amount:^\\d+$}/max-benefit-user-coupon", method = RequestMethod.GET)
    @ResponseBody
    public String getMaxBenefitUserCoupon(@PathVariable long loanId, @PathVariable long amount) {
        UserCouponDto maxBenefitUserCoupon = userCouponService.getMaxBenefitUserCoupon(LoginUserInfo.getLoginName(), loanId, amount);
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