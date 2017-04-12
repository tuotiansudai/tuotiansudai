package com.tuotiansudai.activity.controller;

import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.service.NewmanTyrantService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.repository.model.NewmanTyrantHistoryView;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/activity/newman-tyrant")
public class NewmanTyrantController {
    @Autowired
    private NewmanTyrantService newmanTyrantService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView newmanTyrant(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        tradingTime = tradingTime == null ? new Date() : tradingTime;
        ModelAndView modelAndView = new ModelAndView("/activities/hero-standings", "responsive", true);
        String loginName = LoginUserInfo.getLoginName();

        List<HeroRankingView> newmanViews = newmanTyrantService.obtainNewman(tradingTime);
        List<HeroRankingView> tyrantViews = newmanTyrantService.obtainTyrant(tradingTime);
        List<HeroRankingView> heroRankingViews = CollectionUtils.isEmpty(newmanViews) ? tyrantViews : newmanViews;
        int investRanking = CollectionUtils.isNotEmpty(heroRankingViews) ?
                Iterators.indexOf(heroRankingViews.iterator(), input -> loginName.equalsIgnoreCase(input.getLoginName())) + 1 : 0;
        long investAmount = investRanking > 0 ? heroRankingViews.get(investRanking - 1).getSumAmount() : 0;

        List<NewmanTyrantHistoryView> newmanTyrantHistoryViews = newmanTyrantService.obtainNewmanTyrantHistoryRanking(tradingTime);

        modelAndView.addObject("prizeDto", newmanTyrantService.obtainPrizeDto(new DateTime().toString("yyyy-MM-dd")));
        modelAndView.addObject("investRanking", String.valueOf(investRanking));
        modelAndView.addObject("investAmount", String.valueOf(investAmount));
        modelAndView.addObject("currentTime", new DateTime().withTimeAtStartOfDay().toDate());
        modelAndView.addObject("yesterdayTime", DateUtils.addDays(new DateTime().withTimeAtStartOfDay().toDate(), -1));
        modelAndView.addObject("avgNewmanInvestAmount", String.valueOf(newmanTyrantHistoryViews.size() > 0 ? newmanTyrantHistoryViews.get(0).getAvgNewmanInvestAmount() : 0));
        modelAndView.addObject("avgTyrantInvestAmount", String.valueOf(newmanTyrantHistoryViews.size() > 0 ? newmanTyrantHistoryViews.get(0).getAvgTyrantInvestAmount() : 0));
        return modelAndView;
    }

    @RequestMapping(value = "/newman/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<HeroRankingView> obtainNewman(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        final String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<HeroRankingView> baseListDataDto = new BasePaginationDataDto<>();
        List<HeroRankingView> heroRankingViews = newmanTyrantService.obtainNewman(tradingTime);

        heroRankingViews.stream().forEach(heroRankingView -> heroRankingView.setLoginName(newmanTyrantService.encryptMobileForWeb(loginName, heroRankingView.getLoginName())));
        baseListDataDto.setRecords(heroRankingViews);

        baseListDataDto.setStatus(true);
        return baseListDataDto;
    }

    @RequestMapping(value = "/tyrant/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BasePaginationDataDto<HeroRankingView> obtainTyrant(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        final String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<HeroRankingView> baseListDataDto = new BasePaginationDataDto<>();
        List<HeroRankingView> heroRankingViews = newmanTyrantService.obtainTyrant(tradingTime);

        heroRankingViews.stream().forEach(heroRankingView -> heroRankingView.setLoginName(newmanTyrantService.encryptMobileForWeb(loginName, heroRankingView.getLoginName())));
        baseListDataDto.setRecords(heroRankingViews);

        baseListDataDto.setStatus(true);
        return baseListDataDto;
    }
}
