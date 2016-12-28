package com.tuotiansudai.activity.controller;


import com.tuotiansudai.enums.AppUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/weixin")
public class WeiXinReferrerActivityController {

    @RequestMapping(value = "/rank-list", method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        ModelAndView modelAndView = new ModelAndView("/activities/rank-list");
        return modelAndView;
    }
}
