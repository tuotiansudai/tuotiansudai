package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.GrantWelfareActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/heavy-benefits")
public class GrantWelfareActivityController {

    @Autowired
    private GrantWelfareActivityService grantWelfareActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/heavy-benefits", "responsive", true);
        modelAndView.addObject("referrerCount", grantWelfareActivityService.findReferrerCountByLoginName(loginName));
        modelAndView.addObject("referrerSumInvestAmount", grantWelfareActivityService.findReferrerSumInvestAmountByLoginName(loginName));
        return modelAndView;
    }
}
