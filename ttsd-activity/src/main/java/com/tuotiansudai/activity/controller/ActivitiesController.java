package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import coupon.dto.CouponAlertDto;
import coupon.service.CouponAlertService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/activity")
public class ActivitiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private CouponAlertService couponAlertService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(path = "/{item:^recruit|integral-draw|birth-month|rank-list-app|share-reward|app-download|landing-page|invest-achievement|loan-hike$}", method = RequestMethod.GET)
    public ModelAndView activities(@PathVariable String item) {
        ModelAndView modelAndView = new ModelAndView("/activities/" + item, "responsive", true);
        String loginName = LoginUserInfo.getLoginName();

        if (!Strings.isNullOrEmpty(loginName) && userService.loginNameIsExist(loginName.trim())) {
            modelAndView.addObject("referrer", userService.getMobile(loginName));
        }

        modelAndView.addObject("isLogin", null != loginName);
        AccountModel accountModel = accountService.findByLoginName(loginName);
        modelAndView.addObject("noAccount", null == accountModel);
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

    @RequestMapping(value = "/isLogin", method = RequestMethod.GET)
    public ModelAndView isLogin() {
        if(!StringUtils.isEmpty(LoginUserInfo.getLoginName())) {
            return null;
        } else {
            return new ModelAndView("/csrf");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/get-realRealName", method = RequestMethod.GET)
    public String markRemind(@RequestParam(value = "mobile") String mobile) {
        return accountService.getRealNameByMobile(mobile);
    }
}
