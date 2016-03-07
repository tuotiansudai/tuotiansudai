package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.repository.model.CouponUseRecordView;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/my-treasure")
public class MyTreasureController {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponAlertService couponAlertService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getUserCoupon() {
        String loginName = LoginUserInfo.getLoginName();

        List<CouponUseRecordView> unusedUserCoupons = userCouponService.getUnusedUserCoupons(loginName);
        List<CouponUseRecordView> useRecords = userCouponService.findUseRecords(loginName);
        List<CouponUseRecordView> expiredUserCoupons = userCouponService.getExpiredUserCoupons(loginName);

        ModelAndView modelAndView = new ModelAndView("/my-treasure");
        modelAndView.addObject("unusedCoupons", unusedUserCoupons);
        modelAndView.addObject("useRecords", useRecords);
        modelAndView.addObject("expiredCoupons", expiredUserCoupons);
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName()));
        return modelAndView;
    }
}
