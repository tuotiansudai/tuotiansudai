package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.ChristmasPrizeService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/activity/christmas")
public class ChristmasPrizeController {
    @Autowired
    private ChristmasPrizeService christmasPrizeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BindBankCardService bindBankCardService;

    private static final float CHRISTMAS_SUM_AMOUNT = 420000000;

    private static NumberFormat numberFormat = NumberFormat.getInstance();

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView christmas() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/christmas-day", "responsive", true);
        //投资满20000发一张优惠券
        christmasPrizeService.assignUserCoupon(loginName);
        Map param = christmasPrizeService.getActivityChristmasInvestAmountAndCount();
        long userInvestAmount = (long)param.get("investAmount");

        modelAndView.addObject("allInvestAmount", AmountConverter.convertCentToString(userInvestAmount).replaceAll("\\.00", ""));
        modelAndView.addObject("investScale", userInvestAmount >= CHRISTMAS_SUM_AMOUNT ? "100%" : numberFormat.format((float) userInvestAmount / CHRISTMAS_SUM_AMOUNT * 100) + "%");
        modelAndView.addObject("userCount", param.get("investCount"));
        modelAndView.addObject("isStart", christmasPrizeService.isStart());

        modelAndView.addObject("drawTime", christmasPrizeService.getDrawPrizeTime(LoginUserInfo.getMobile()));
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public DrawLotteryResultDto travelDrawPrize(@RequestParam(value = "mobile", required = false) String mobile) {
        return christmasPrizeService.drawLotteryPrize(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile());
    }

    @ResponseBody
    @RequestMapping(value = "/user-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByLoginName(@RequestParam(value = "mobile", required = false) String mobile) {
        return christmasPrizeService.findDrawLotteryPrizeRecordByMobile(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile());
    }

    @ResponseBody
    @RequestMapping(value = "/all-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByAll() {
        return christmasPrizeService.findDrawLotteryPrizeRecord(null);
    }

    private List<Integer> generateSteps(String loginName) {
        List<Integer> steps = Lists.newArrayList(1, 0, 0);
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
