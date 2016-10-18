package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.Iphone7LotteryService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity/iphone7-lottery")
public class Iphone7ActivityController {

    @Autowired
    private Iphone7LotteryService iphone7LotteryService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/iphone7-lottery", "responsive", true);
        modelAndView.addObject("nextLotteryInvestAmount",iphone7LotteryService.nextLotteryInvestAmount());
        modelAndView.addObject("lotteryList", iphone7LotteryService.iphone7InvestLotteryWinnerViewList());
        modelAndView.addObject("myInvestList", iphone7LotteryService.myInvestLotteryList("gengbeijun",1,10).getData().getRecords());
        return modelAndView;
    }



}
