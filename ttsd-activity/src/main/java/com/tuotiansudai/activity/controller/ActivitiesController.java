package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.util.LoginUserInfo;
import com.tuotiansudai.coupon.dto.CouponAlertDto;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/activity")
public class ActivitiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponAlertService couponAlertService;

    @RequestMapping(path = "/{item:^recruit|birth-month|rank-list-app|share-reward|app-download|landing-page|invest-achievement|loan-hike|luxury-activity$}", method = RequestMethod.GET)
    public ModelAndView activities(HttpServletRequest httpServletRequest, @PathVariable String item) {
        ModelAndView modelAndView = new ModelAndView("/activities/" + item, "responsive", true);
        String loginName = httpServletRequest.getParameter("loginName");

        if (!Strings.isNullOrEmpty(loginName) && userService.loginNameIsExist(loginName.trim())) {
            modelAndView.addObject("referrer", userMapper.findByLoginName(loginName).getMobile());
        }

        return modelAndView;
    }

    @RequestMapping(path = "/{item:^landing-page-app|landing-tour|landing-bus|landing-game$}", method = RequestMethod.GET)
    public ModelAndView promoteNewbie(@PathVariable String item) {
        ModelAndView modelAndView = new ModelAndView("/activities/" + item, "responsive", true);
        CouponAlertDto couponAlert = couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON));
        boolean newbieCouponAlert = couponAlert != null && couponAlert.getCouponIds().size() > 0;
        modelAndView.addObject("couponAlert", newbieCouponAlert);
        return modelAndView;
    }


}
