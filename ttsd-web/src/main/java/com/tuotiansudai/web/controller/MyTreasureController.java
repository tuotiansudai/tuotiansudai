package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.UserCouponView;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/my-treasure")
public class MyTreasureController {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponAlertService couponAlertService;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getUserCoupon() {
        String loginName = LoginUserInfo.getLoginName();

        List<UserCouponView> unusedUserCoupons = userCouponService.getUnusedUserCoupons(loginName);
        List<UserCouponView> useRecords = userCouponService.findUseRecords(loginName);
        List<UserCouponView> expiredUserCoupons = userCouponService.getExpiredUserCoupons(loginName);

        ModelAndView modelAndView = new ModelAndView("/my-treasure");
        modelAndView.addObject("unusedCoupons", unusedUserCoupons);
        modelAndView.addObject("useRecords", useRecords);
        modelAndView.addObject("expiredCoupons", expiredUserCoupons);
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE)));
        return modelAndView;
    }

    @RequestMapping(value = "/{exchangeCode}/exchange", method = RequestMethod.POST)
    @ResponseBody
    public BaseDataDto exchange(@PathVariable String exchangeCode) {
        return exchangeCodeService.exchange(LoginUserInfo.getLoginName(), exchangeCode);
    }

    @RequestMapping(value = "/coupon-exchange", method = RequestMethod.GET)
    public ModelAndView exchange() {
        return new ModelAndView("/coupon-exchange");
    }
}
