package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.service.TravelPrizeService;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(path = "/activity/autumn/travel")
public class TravelPrizeController {

    @Value(value = "${activity.autumn.invest.channel}")
    private String activityAutumnInvestChannelKey;

    @Autowired
    private TravelPrizeService travelPrizeService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BindBankCardService bindBankCardService;


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/autumn-travel", "responsive", true);
        modelAndView.addObject("today", new Date());
        modelAndView.addObject("amount", AmountConverter.convertCentToString(travelPrizeService.getTodayTravelInvestAmount(loginName)));
        modelAndView.addObject("travelPrize", travelPrizeService.getTravelPrizeItems());
        modelAndView.addObject("userTravelPrize", travelPrizeService.getTravelAwardItems(loginName));
        modelAndView.addObject("myTravelPrize", travelPrizeService.getMyTravelAwardItems(LoginUserInfo.getMobile()));
        List<Integer> steps = Lists.newArrayList(1, 0, 0, 0, 0);
        modelAndView.addObject("steps", steps);
        if (Strings.isNullOrEmpty(loginName)) {
            return modelAndView;
        }
        steps.set(0, 2);
        if (accountService.findByLoginName(loginName) == null) {
            steps.set(1, 1);
            return modelAndView;
        }
        steps.set(1, 2);
        steps.set(2, 1);
        steps.set(3, 1);
        steps.set(4, 1);
        if (bindBankCardService.getPassedBankCard(loginName) != null) {
           steps.set(2, 2);
        }

        return modelAndView;
    }

    @RequestMapping(path = "/{id:^\\d+$}/detail", method = RequestMethod.GET)
    public ModelAndView travelPrizeDetail() {
        ModelAndView modelAndView = new ModelAndView("/activities/autumn-tour-detail", "responsive", true);

        return modelAndView;
    }

    @RequestMapping(path = "/invest", method = RequestMethod.GET)
    public ModelAndView travelInvest(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String referer = request.getHeader(HttpHeaders.REFERER);
        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView(MessageFormat.format("redirect:/login?redirect={0}", referer));
        }

        redisWrapperClient.hset(this.activityAutumnInvestChannelKey, loginName, "travel", 3600 * 24 * 60);

        return new ModelAndView("redirect:/loan-list");
    }
}
