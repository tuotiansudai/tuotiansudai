package com.tuotiansudai.web.controller;


import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user-coupon")
public class UserCouponController {

    @Autowired
    private UserCouponService userCouponService;

    @RequestMapping(value = "/assign-user-coupon", method = RequestMethod.POST)
    @ResponseBody
    public void assignUserCoupon() {
        userCouponService.assignUserCoupon(LoginUserInfo.getLoginName());
    }
}
