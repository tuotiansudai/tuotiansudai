package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.activity.repository.model.NewmanTyrantHistoryView;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.console.activity.service.ActivityConsoleCelebrationHeroRankingService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/newman-tyrant")
public class CelebrationHeroRankingController {
    @Autowired
    private ActivityConsoleCelebrationHeroRankingService activityConsoleCelebrationHeroRankingService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView newmanTyrant(@RequestParam(value = "tradingTime", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date tradingTime) {

        ModelAndView modelAndView = new ModelAndView("/newman-tyrant");

        if (tradingTime == null) {
            tradingTime = new Date();
        }

        List<NewmanTyrantView> heroRankingViews = activityConsoleCelebrationHeroRankingService.obtainHero(tradingTime);

        List<NewmanTyrantHistoryView> heroRankingHistoryViews = activityConsoleCelebrationHeroRankingService.obtainNewmanTyrantHistoryRanking(tradingTime);

        modelAndView.addObject("tradingTime", tradingTime);
        modelAndView.addObject("tyrantViews", heroRankingViews);
        modelAndView.addObject("avgTyrantInvestAmount", heroRankingHistoryViews.size() > 0 ? heroRankingHistoryViews.get(0).getAvgTyrantInvestAmount() : 0);
        modelAndView.addObject("todayDto", activityConsoleCelebrationHeroRankingService.obtainPrizeDto(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        modelAndView.addObject("tomorrowDto", activityConsoleCelebrationHeroRankingService.obtainPrizeDto(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        List<NewmanTyrantView> newmanTyrantViews=new ArrayList<NewmanTyrantView>();
        modelAndView.addObject("newmanViews", newmanTyrantViews);
        modelAndView.addObject("avgNewmanInvestAmount",0);

        return modelAndView;

    }

    @RequestMapping(value = "/upload-image", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public NewmanTyrantPrizeDto uploadMysteriousPrize(@RequestBody NewmanTyrantPrizeDto newmanTyrantPrizeDto,
                                                    @RequestParam boolean today) {
        newmanTyrantPrizeDto.setPrizeDate(new Date());
        if (!today) {
            newmanTyrantPrizeDto.setPrizeDate(new DateTime().plusDays(1).toDate());
        }
        activityConsoleCelebrationHeroRankingService.savePrize(newmanTyrantPrizeDto);
        return newmanTyrantPrizeDto;
    }


}
