package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.NationalPrizeService;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/activity/national")
public class NationalPrizeController {

    @Autowired
    private NationalPrizeService nationalPrizeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BindBankCardService bindBankCardService;

    private static final float NATIONAL_SUM_AMOUNT = 194900000;

    private static NumberFormat numberFormat = NumberFormat.getInstance();

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/national-day", "responsive", true);
        long userInvestAmount = nationalPrizeService.getAllActivityInvestAmount();
        modelAndView.addObject("myPoint",nationalPrizeService.getMyActivityPoint(loginName));
        modelAndView.addObject("allInvestAmount",AmountConverter.convertCentToString(userInvestAmount).replaceAll("\\.00", ""));
        modelAndView.addObject("investScale",numberFormat.format((float) userInvestAmount / NATIONAL_SUM_AMOUNT * 100));
        modelAndView.addObject("userCount",nationalPrizeService.getAllActivityUserCount());
        modelAndView.addObject("drawTime", nationalPrizeService.getDrawPrizeTime(LoginUserInfo.getMobile()));
        modelAndView.addObject("steps", generateSteps(loginName));
        modelAndView.addObject("activityType","national");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public DrawLotteryResultDto travelDrawPrize(@RequestParam(value = "mobile", required = false) String mobile) {
        return nationalPrizeService.drawLotteryPrize(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile());
    }

    @ResponseBody
    @RequestMapping(value = "/user-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByLoginName(@RequestParam(value = "mobile", required = false) String mobile) {
        return nationalPrizeService.findDrawLotteryPrizeRecordByMobile(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile());
    }

    @ResponseBody
    @RequestMapping(value = "/all-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeLuxuryRecordByAll() {
        return nationalPrizeService.findDrawLotteryPrizeRecord(null);
    }

    private List<Integer> generateSteps(String loginName) {
        List<Integer> steps = Lists.newArrayList(1, 0, 0, 0, 0);
        if (Strings.isNullOrEmpty(loginName)) {
            return steps;
        }
        steps.set(0, 2);
        if (accountService.findByLoginName(loginName) == null) {
            steps.set(1, 1);
            return steps;
        }
        steps.set(1, 2);
        steps.set(2, 1);
        steps.set(3, 1);
        steps.set(4, 1);
        if (bindBankCardService.getPassedBankCard(loginName) != null) {
            steps.set(2, 2);
        }
        return steps;
    }
}
