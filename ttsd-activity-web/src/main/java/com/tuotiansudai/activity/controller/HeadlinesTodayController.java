package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.HeadlinesTodayPrizeService;
import com.tuotiansudai.activity.service.NationalPrizeService;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/activity/headlines-today")
public class HeadlinesTodayController {
    @Autowired
    private HeadlinesTodayPrizeService headlinesTodayPrizeService;

    private static NumberFormat numberFormat = NumberFormat.getInstance();

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView headlinesToday() {
        ModelAndView modelAndView = new ModelAndView("/activities/headlines-today", "responsive", true);
        modelAndView.addObject("activityType","national");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public DrawLotteryResultDto headlinesTodayDrawPrize(@RequestParam(value = "mobile", required = false) String mobile) {
        return headlinesTodayPrizeService.drawLotteryPrize(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile());
    }

    @ResponseBody
    @RequestMapping(value = "/isAccount", method = RequestMethod.GET)
    public ModelAndView isAccount() {
        if (!StringUtils.isEmpty(LoginUserInfo.getLoginName())) {
            return null;
        } else {
            return new ModelAndView("/csrf");
        }
    }
}
