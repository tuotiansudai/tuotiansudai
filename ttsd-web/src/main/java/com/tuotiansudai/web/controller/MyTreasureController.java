package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/my-treasure")
public class MyTreasureController {

    @Autowired
    private UserCouponService userCouponService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getUserCoupon() {
        String loginName = LoginUserInfo.getLoginName();

        List<UserCouponDto> moneyCoupons = userCouponService.getUserCoupons(loginName, Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.INVEST_COUPON));
        List<UserCouponDto> interestCoupons = userCouponService.getUserCoupons(loginName, Lists.newArrayList(CouponType.INTEREST_COUPON));
        List<UserCouponDto> redEnvelopes = userCouponService.getUserCoupons(loginName, Lists.newArrayList(CouponType.RED_ENVELOPE));

        ModelAndView modelAndView = new ModelAndView("/my-treasure");
        modelAndView.addObject("moneyCoupons", moneyCoupons);
        modelAndView.addObject("interestCoupons", interestCoupons);
        modelAndView.addObject("redEnvelopes", redEnvelopes);
        return modelAndView;
    }
}
