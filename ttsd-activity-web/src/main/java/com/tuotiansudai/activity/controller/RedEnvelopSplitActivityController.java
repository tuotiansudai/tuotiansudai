package com.tuotiansudai.activity.controller;


import com.tuotiansudai.activity.service.RedEnvelopSplitActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/red-envelop-split")
public class RedEnvelopSplitActivityController {

    @Autowired
    private RedEnvelopSplitActivityService redEnvelopSplitActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/red-envelop-split", "responsive", true);
        modelAndView.addObject("referrerCount", redEnvelopSplitActivityService.getReferrerCount(loginName));
        modelAndView.addObject("redEnvelopAmount", redEnvelopSplitActivityService.getReferrerRedEnvelop(loginName));
        modelAndView.addObject("referrerUrl", redEnvelopSplitActivityService.getShareReferrerUrl(loginName));
        return modelAndView;
    }

    @RequestMapping(value = "/referrer", method = RequestMethod.GET)
    public ModelAndView referrer() {
        ModelAndView modelAndView = new ModelAndView("/activities/red-envelop-referrer", "responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/before-register", method = RequestMethod.POST)
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView("/activities/red-envelop-referrer", "responsive", true);
        modelAndView.addObject("registerStatus", "1");
        return modelAndView;
    }
}
