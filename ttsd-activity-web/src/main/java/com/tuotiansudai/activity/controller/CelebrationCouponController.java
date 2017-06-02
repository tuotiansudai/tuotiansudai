package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.CelebrationCouponService;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity/celebration-coupon")
public class CelebrationCouponController {

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private CelebrationCouponService celebrationCouponService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView celebrationCouponHome() {

        boolean duringActivities = celebrationCouponService.duringActivities();
        ModelAndView modelAndView = new ModelAndView("/wechat/celebration-draw-coupon");

        modelAndView.addObject("duringActivities", duringActivities);

        return modelAndView;
    }

    @RequestMapping(path = "/draw", method = RequestMethod.GET)
    public ModelAndView celebrationDrawCouponHome() {
        ModelAndView mv = new ModelAndView();
        String loginName = LoginUserInfo.getLoginName();
        //test
        loginName = "shenjiaojiao";

        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView("/wechat/celebration-draw-coupon-unbound");
        }

        if (!weChatService.isBound(loginName)) {
            return new ModelAndView("/error/404");
        }
        boolean drewCoupon = celebrationCouponService.drewCoupon(loginName);
        if (drewCoupon) {
            mv.addObject("drewCoupon", drewCoupon);
            mv.setViewName("/wechat/celebration-draw-coupon");
        } else {
            celebrationCouponService.sendDrawCouponMessage(loginName);
            mv.setViewName("/celebration-draw-coupon-success");
        }

        return mv;
    }


}
