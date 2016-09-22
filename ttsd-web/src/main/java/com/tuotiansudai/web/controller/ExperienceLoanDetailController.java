package com.tuotiansudai.web.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.ExperienceLoanDto;
import com.tuotiansudai.service.ExperienceLoanDetailService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/loan")
public class ExperienceLoanDetailController {

    @Autowired
    private ExperienceLoanDetailService ExperienceLoanDetailService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponAlertService couponAlertService;

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public ModelAndView getLoanDetail() {
        ExperienceLoanDto experienceLoanDto = ExperienceLoanDetailService.findExperienceLoanDtoDetail(1, LoginUserInfo.getLoginName());
        ModelAndView modelAndView = new ModelAndView("/experience-loan", "responsive", true);
        modelAndView.addObject("loan", experienceLoanDto);
        modelAndView.addObject("coupon", userCouponService.getExperienceInvestUserCoupon(LoginUserInfo.getLoginName()));
        modelAndView.addObject("couponAlert", couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON)));
        return modelAndView;
    }

}
