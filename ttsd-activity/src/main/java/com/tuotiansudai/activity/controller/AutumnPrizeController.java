package com.tuotiansudai.activity.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.service.AutumnPrizeService;
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
@RequestMapping(path = "/activity/autumn")
public class AutumnPrizeController {

    @Value(value = "${activity.autumn.invest.channel}")
    private String activityAutumnInvestChannelKey;

    @Autowired
    private AutumnPrizeService autumnPrizeService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BindBankCardService bindBankCardService;


    @RequestMapping(path = "/travel", method = RequestMethod.GET)
    public ModelAndView travelPrize() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/autumn-travel", "responsive", true);
        modelAndView.addObject("today", new Date());
        modelAndView.addObject("myInvestAmount", AmountConverter.convertCentToString(autumnPrizeService.getTodayInvestAmount(loginName, "travel")));
        modelAndView.addObject("travelPrize", autumnPrizeService.getTravelPrizeItems());
        modelAndView.addObject("userTravelPrize", autumnPrizeService.getTravelAwardItems(loginName));
        modelAndView.addObject("myTravelPrize", autumnPrizeService.getMyTravelAwardItems(LoginUserInfo.getMobile()));
        modelAndView.addObject("steps", generateSteps(loginName));
        return modelAndView;
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

    @RequestMapping(path = "/luxury", method = RequestMethod.GET)
    public ModelAndView luxuryPrize() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/activities/autumn-luxury", "responsive", true);
        modelAndView.addObject("today", new Date());
        modelAndView.addObject("myInvestAmount", AmountConverter.convertCentToString(autumnPrizeService.getTodayInvestAmount(loginName, "luxury")));
        modelAndView.addObject("luxuryPrize", autumnPrizeService.getLuxuryPrizeItems());
        modelAndView.addObject("userLuxuryPrize", autumnPrizeService.getLuxuryAwardItems(loginName));
        modelAndView.addObject("myLuxuryPrize", autumnPrizeService.getMyLuxuryAwardItems(LoginUserInfo.getMobile()));
        modelAndView.addObject("steps", generateSteps(loginName));
        return modelAndView;
    }

    @RequestMapping(path = "/travel/{id:^\\d+$}/detail", method = RequestMethod.GET)
    public ModelAndView travelPrizeDetail() {
        ModelAndView modelAndView = new ModelAndView("/activities/autumn-tour-detail", "responsive", true);

        return modelAndView;
    }

    @RequestMapping(path = "/travel/invest", method = RequestMethod.GET)
    public ModelAndView travelInvest(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String referer = request.getHeader(HttpHeaders.REFERER);
        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView(MessageFormat.format("redirect:/login?redirect={0}", referer));
        }

        redisWrapperClient.hset(this.activityAutumnInvestChannelKey, loginName, "travel", 3600 * 24 * 60);

        return new ModelAndView("redirect:/loan-list");
    }

    @RequestMapping(path = "/luxury/invest", method = RequestMethod.GET)
    public ModelAndView luxuryInvest(HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String referer = request.getHeader(HttpHeaders.REFERER);
        if (Strings.isNullOrEmpty(loginName)) {
            return new ModelAndView(MessageFormat.format("redirect:/login?redirect={0}", referer));
        }

        redisWrapperClient.hset(this.activityAutumnInvestChannelKey, loginName, "luxury", 3600 * 24 * 60);

        return new ModelAndView("redirect:/loan-list");
    }
}
