package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.dto.CouponAlertDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.MobileEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/activity")
public class ActivitiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private CouponAlertService couponAlertService;

    @RequestMapping(path = "/{item:^landing-page|invest-achievement|landing-anxin|point-update|sign-check|wx-register|divide-money|minsheng|guarantee|midsummer|lottery-intro|icp-intro|double-landing|depository|fudian-bank|information-safety$}", method = RequestMethod.GET)
    public ModelAndView activities(@PathVariable String item, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/activities/" + item, "responsive", true);

        // 微信抽奖活动，没有响应式
        if ("lottery-intro".equals(item)) {
            modelAndView.addObject("responsive", null);
        }

        String loginName = LoginUserInfo.getLoginName();

        if (!Strings.isNullOrEmpty(loginName) && userService.loginNameIsExist(loginName.trim())) {
            modelAndView.addObject("referrer", userService.getMobile(loginName));
        }
        modelAndView.addObject("appVersion", request.getHeader("appVersion"));
        modelAndView.addObject("isLogin", null != loginName);
        //AccountModel accountModel = accountService.findByLoginName(loginName);
        //modelAndView.addObject("noAccount", null == accountModel);
        return modelAndView;
    }

    @RequestMapping(path = "/channel/htracking")
    public ModelAndView channels() {
        return new ModelAndView("/wechat/channel-htracking");
    }

    @RequestMapping(path = "/channel/htracking/success")
    public ModelAndView channelsRegisterSuccess() {
        return new ModelAndView("/wechat/channel-htracking-success");
    }

    @RequestMapping(path = "/{item:^landing-page-app|landing-tour|landing-bus|landing-game$}", method = RequestMethod.GET)
    public ModelAndView promoteNewbie(@PathVariable String item) {
        ModelAndView modelAndView = new ModelAndView("/activities/" + item, "responsive", true);
        CouponAlertDto couponAlert = couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON));
        boolean newbieCouponAlert = couponAlert != null && couponAlert.getCouponIds().size() > 0;
        modelAndView.addObject("couponAlert", newbieCouponAlert);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/get-realRealName", method = RequestMethod.GET)
    public String markRemind(@RequestParam(value = "mobile") String mobile) {
        UserModel userModel = userService.findByMobile(mobile);
        return userModel != null && !Strings.isNullOrEmpty(userModel.getUserName()) ? "*" + userModel.getUserName().substring(1, userModel.getUserName().length()) : MobileEncoder.encode(mobile);
    }
}
