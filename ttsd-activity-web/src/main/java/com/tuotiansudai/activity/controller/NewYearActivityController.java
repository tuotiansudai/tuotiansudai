package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.NewYearActivityService;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/activity/new-year")
public class NewYearActivityController {

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private NewYearActivityService newYearActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView newYearActivity() {
        return new ModelAndView("/activities/2018/new-year-increase-interest");
    }

    @RequestMapping(path = "/wechat", method = RequestMethod.GET)
    public ModelAndView newYearActivityWechat(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/new-year");
        }
        ModelAndView modelAndView = new ModelAndView("/wechat/new-year-increase-interest");
        modelAndView.addObject("duringActivities", newYearActivityService.duringActivities());
        return modelAndView;
    }

    @RequestMapping(path = "/draw", method = RequestMethod.GET)
    public ModelAndView newYearActivityDrawCoupon(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/new-year");
        }
        boolean duringActivities = newYearActivityService.duringActivities();
        if (!duringActivities) {
            return new ModelAndView("redirect:/activity/new-year/wechat");
        }

        String loginName = LoginUserInfo.getLoginName();
        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView("redirect:/we-chat/entry-point?redirect=/activity/new-year/draw");
        }
        if (!weChatService.isBound(loginName)) {
            return new ModelAndView("/error/404");
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/new-year-increase-interest");
        boolean drewCoupon = newYearActivityService.drewCoupon(loginName);
        modelAndView.addObject("duringActivities", duringActivities);
        modelAndView.addObject("drewCoupon", drewCoupon);
        if (!drewCoupon) {
            newYearActivityService.sendDrawCouponMessage(loginName);
            modelAndView.addObject("drawSuccess", true);
        }
        return modelAndView;
    }
}
