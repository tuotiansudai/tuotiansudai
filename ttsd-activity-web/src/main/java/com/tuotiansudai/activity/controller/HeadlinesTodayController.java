package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.service.HeadlinesTodayPrizeService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

public class HeadlinesTodayController {
    @Autowired
    private HeadlinesTodayPrizeService headlinesTodayPrizeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView headlinesToday(@RequestParam(value = "fromRedirect", required = false, defaultValue = "") String fromRedirect) {
        ModelAndView modelAndView = new ModelAndView("/activities/today-headlines", "responsive", true);
        modelAndView.addObject("userStatus", headlinesTodayPrizeService.userStatus(LoginUserInfo.getMobile(), fromRedirect));
        modelAndView.addObject("loginMobile", LoginUserInfo.getMobile());
        modelAndView.addObject("lotteryTime", headlinesTodayPrizeService.getDrawPrizeTime(LoginUserInfo.getMobile()));
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public DrawLotteryResultDto headlinesTodayDrawPrize(@RequestParam(value = "mobile", required = false) String mobile) {
        return headlinesTodayPrizeService.drawLotteryPrize(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile());
    }

}
