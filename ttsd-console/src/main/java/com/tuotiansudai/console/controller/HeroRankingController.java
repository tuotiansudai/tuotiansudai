package com.tuotiansudai.console.controller;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.service.HeroRankingService;
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
public class HeroRankingController {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private HeroRankingService heroRankingService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @RequestMapping(value = "/activity-manage/hero-ranking", method = RequestMethod.GET)
    public ModelAndView heroRanking(@RequestParam(value = "tradingTime", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date tradingTime) {

        ModelAndView modelAndView = new ModelAndView("/hero-ranking");

        if (tradingTime == null) {
            tradingTime = new Date();
        }

        List<HeroRankingView> heroRankingViewReferrerList = investMapper.findHeroRankingByReferrer(tradingTime, 1, 10);

        List<HeroRankingView> heroRankingViewInvestList = heroRankingService.obtainHeroRanking(tradingTime);

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

    @RequestMapping(value = "/activity-manage/upload-image", method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
    @ResponseBody
    public MysteriousPrizeDto uploadMysteriousPrize(@RequestBody MysteriousPrizeDto mysteriousPrizeDto){
        String prizeDate = new DateTime(mysteriousPrizeDto.getPrizeDate()).withTimeAtStartOfDay().toString("yyyy-MM-dd");
        mysteriousPrizeDto.setPrizeDate(prizeDate);
        heroRankingService.saveMysteriousPrize(mysteriousPrizeDto);
        return mysteriousPrizeDto;
    }



}
