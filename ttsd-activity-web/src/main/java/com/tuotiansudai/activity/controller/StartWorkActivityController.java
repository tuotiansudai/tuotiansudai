package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "/activity/start-work")
public class StartWorkActivityController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView newYearActivity() {

        return new ModelAndView("/activities/2018/start-work");
    }

    @RequestMapping(path = "/exchange", method = RequestMethod.GET)
    public Map<String, String> exchange() {


        return "";
    }

    @RequestMapping(path = "/wechat", method = RequestMethod.GET)
    public ModelAndView startWorkActivityWechat(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/start-work");
        }
        ModelAndView modelAndView = new ModelAndView("/wechat/new-year-increase-interest");
        return modelAndView;
    }

    @RequestMapping(path = "/draw", method = RequestMethod.GET)
    public ModelAndView newYearActivityDrawCoupon(HttpServletRequest request) {
        String openId = (String) request.getSession().getAttribute("weChatUserOpenid");
        if (Strings.isNullOrEmpty(openId)) {
            return new ModelAndView("redirect:/activity/start-work");
        }
//        String duringActivities = newYearActivityService.duringActivities();
//        if (!"START".equals(duringActivities)) {
//            return new ModelAndView("redirect:/activity/start-work/wechat");
//        }

//        String loginName = LoginUserInfo.getLoginName();
//        if (Strings.isNullOrEmpty(loginName)) {
//            return new ModelAndView("redirect:/we-chat/entry-point?redirect=/activity/start-work/draw");
//        }
//        if (!weChatService.isBound(loginName)) {
//            return new ModelAndView("/error/404");
//        }
//
//        ModelAndView modelAndView = new ModelAndView("/wechat/new-year-increase-interest");
//        boolean drewCoupon = newYearActivityService.drewCoupon(loginName);
//        modelAndView.addObject("activityStatus", duringActivities);
//        modelAndView.addObject("drewCoupon", drewCoupon);
//        if (!drewCoupon) {
//            newYearActivityService.sendDrawCouponMessage(loginName);
//            modelAndView.addObject("drawSuccess", true);
//        }
        return new ModelAndView("/wechat/new-year-increase-interest");
    }

}
