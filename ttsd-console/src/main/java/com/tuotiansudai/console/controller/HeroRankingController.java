package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.service.HeroRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Controller
public class HeroRankingController {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private HeroRankingService heroRankingService;

    @RequestMapping(value = "/activity-manage/hero-ranking", method = RequestMethod.GET)
    public ModelAndView heroRanking(@RequestParam(value = "tradingTime") Date tradingTime) {

        ModelAndView modelAndView = new ModelAndView("/hero-ranking");

        List<HeroRankingView> heroRankingViewReferrerList = investMapper.findHeroRankingByReferrer(tradingTime, 1, 10);

        List<HeroRankingView> heroRankingViewInvestList = heroRankingService.obtainHeroRanking(tradingTime);

        long avgInvestAmount = 0;

        for (HeroRankingView heroRankingView : heroRankingViewInvestList) {
            avgInvestAmount += heroRankingView.getSumAmount();
        }

        modelAndView.addObject("heroRankingViewReferrerList", heroRankingViewReferrerList);

        modelAndView.addObject("heroRankingViewInvestList", heroRankingViewInvestList);

        modelAndView.addObject("avgInvestAmount", new BigDecimal(avgInvestAmount).divide(new BigDecimal(10)).setScale(0, RoundingMode.DOWN));

        return modelAndView;
    }

}
