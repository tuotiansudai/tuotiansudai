package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.service.WeiXinReferrerActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/weixin")
public class WeiXinReferrerActivityController {

    @Autowired
    private WeiXinReferrerActivityService weiXinReferrerActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/rank-list");
        modelAndView.addObject("referrerCount", weiXinReferrerActivityService.getReferrerCount(loginName));
        modelAndView.addObject("redEnvelopAmount", weiXinReferrerActivityService.getReferrerRedEnvelop(loginName));
        return modelAndView;
    }
}
