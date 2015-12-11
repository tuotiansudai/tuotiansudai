package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.web.freemarker.directive.AmountDirective;
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

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getUserCoupon() {
        String loginName = LoginUserInfo.getLoginName();
        List<UserCouponDto> userCouponDtos = userCouponService.getUserCouponDtoByLoginName(loginName);
        ModelAndView mv = new ModelAndView("/my-treasure");
        mv.addObject("coupons", userCouponDtos);
        mv.addObject("amount", new AmountDirective());
        return mv;
    }
}
