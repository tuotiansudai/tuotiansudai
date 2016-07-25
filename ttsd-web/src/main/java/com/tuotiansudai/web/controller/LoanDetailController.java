package com.tuotiansudai.web.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.ExtraLoanRateDto;
import com.tuotiansudai.dto.LoanDetailDto;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ExtraLoanRateModel;
import com.tuotiansudai.service.LoanDetailService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.web.config.security.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.List;


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
    private ExtraLoanRateMapper extraLoanRateMapper;

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getLoanDetail(@PathVariable long loanId) {
        LoanDetailDto dto = loanDetailService.getLoanDetail(LoginUserInfo.getLoginName(), loanId);
        if (dto == null) {
            return new ModelAndView("/error/404");
        }
        ModelAndView modelAndView = new ModelAndView("/loan", "responsive", true);
        modelAndView.addObject("loan", dto);
        modelAndView.addObject("coupons", userCouponService.getInvestUserCoupons(LoginUserInfo.getLoginName(), loanId));
        modelAndView.addObject("maxBenefitUserCoupon", this.userCouponService.getMaxBenefitUserCoupon(LoginUserInfo.getLoginName(),
                loanId,
                AmountConverter.convertStringToCent(dto.getMaxAvailableInvestAmount())));
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE)));
        List<ExtraLoanRateModel> extraLoanRateModels =  extraLoanRateMapper.findByLoanIdOrderByRate(dto.getId());
        List<ExtraLoanRateDto> extraLoanRateDtoList = Lists.newArrayList();
        double minRate = 0;
        double maxRate = 0;
        if(extraLoanRateModels.size() > 1){
            minRate = extraLoanRateModels.get(0).getRate();
            maxRate = extraLoanRateModels.get(extraLoanRateModels.size() - 1).getRate();
            for(ExtraLoanRateModel extraLoanRateModel : extraLoanRateModels){
                extraLoanRateDtoList.add(new ExtraLoanRateDto(extraLoanRateModel));
            }
        }
        modelAndView.addObject("minRate",minRate);
        modelAndView.addObject("maxRate",maxRate);
        modelAndView.addObject("extraLoanRateModels",extraLoanRateDtoList);
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

    @RequestMapping(value = "/{loanId:^\\d+$}/invests", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getInvestList(@PathVariable long loanId,
                                                        @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                        @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return loanDetailService.getInvests(LoginUserInfo.getLoginName(), loanId, index, pageSize);
    }
}