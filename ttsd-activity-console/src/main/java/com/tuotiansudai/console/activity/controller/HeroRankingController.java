package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.console.activity.service.HeroRankingService;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.repository.model.ActivityCategory;
import com.tuotiansudai.repository.model.HeroRankingView;
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
@RequestMapping(value = "/activity-console/activity-manage")
public class HeroRankingController {

    @Autowired
    private HeroRankingService heroRankingService;

    @RequestMapping(value = "/hero-ranking", method = RequestMethod.GET)
    public ModelAndView heroRanking(@RequestParam(value = "tradingTime", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date tradingTime) {

        ModelAndView modelAndView = new ModelAndView("/hero-ranking");

        if (tradingTime == null) {
            tradingTime = new Date();
        }

        List<HeroRankingView> heroRankingViewReferrerList = heroRankingService.obtainHeroRankingReferrer(ActivityCategory.NEW_HERO_RANKING,tradingTime);

        List<HeroRankingView> heroRankingViewInvestList = heroRankingService.obtainHeroRanking(ActivityCategory.NEW_HERO_RANKING,tradingTime);

        long avgInvestAmount = 0;

        for (HeroRankingView heroRankingView : heroRankingViewInvestList) {
            avgInvestAmount += heroRankingView.getSumAmount();
        }

        modelAndView.addObject("tradingTime", tradingTime);

        modelAndView.addObject("heroRankingViewReferrerList", heroRankingViewReferrerList);

        modelAndView.addObject("heroRankingViewInvestList", heroRankingViewInvestList);

        modelAndView.addObject("avgInvestAmount", new BigDecimal(avgInvestAmount).divide(new BigDecimal(10)).setScale(0, RoundingMode.DOWN));

        modelAndView.addObject("todayMysteriousPrizeDto",heroRankingService.obtainMysteriousPrizeDto(new DateTime().withTimeAtStartOfDay().toString("yyyy-MM-dd")));
        modelAndView.addObject("tomorrowMysteriousPrizeDto",heroRankingService.obtainMysteriousPrizeDto(new DateTime().plusDays(1).withTimeAtStartOfDay().toString("yyyy-MM-dd")));
        return modelAndView;
    }

    @RequestMapping(value = "/upload-image", method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
    @ResponseBody
    public MysteriousPrizeDto uploadMysteriousPrize(@RequestBody MysteriousPrizeDto mysteriousPrizeDto){
        heroRankingService.saveMysteriousPrize(mysteriousPrizeDto);
        return mysteriousPrizeDto;
    }
}
