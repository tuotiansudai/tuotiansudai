package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.console.activity.service.ActivityConsoleNewmanTyrantService;
import com.tuotiansudai.repository.model.HeroRankingView;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/newman-tyrant")
public class NewmanTyrantController {
    @Autowired
    private ActivityConsoleNewmanTyrantService activityConsoleNewmanTyrantService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView newmanTyrant(@RequestParam(value = "tradingTime", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date tradingTime) {

        ModelAndView modelAndView = new ModelAndView("/newman-tyrant");

        if (tradingTime == null) {
            tradingTime = new Date();
        }

        List<HeroRankingView> newmanViews = activityConsoleNewmanTyrantService.obtainNewman(tradingTime);

        List<HeroRankingView> tyrantViews = activityConsoleNewmanTyrantService.obtainTyrant(tradingTime);

        long avgNewmanInvestAmount = newmanViews.stream().mapToLong(heroRankingView -> heroRankingView.getSumAmount()).sum();

        long avgTyrantInvestAmount = tyrantViews.stream().mapToLong(heroRankingView -> heroRankingView.getSumAmount()).sum();

        modelAndView.addObject("tradingTime", tradingTime);

        modelAndView.addObject("newmanViews", newmanViews);

        modelAndView.addObject("tyrantViews", tyrantViews);

        modelAndView.addObject("avgNewmanInvestAmount", new BigDecimal(avgNewmanInvestAmount).divide(new BigDecimal(newmanViews.size() == 0 ? 1:newmanViews.size()),0,RoundingMode.DOWN));

        modelAndView.addObject("avgTyrantInvestAmount", new BigDecimal(avgTyrantInvestAmount).divide(new BigDecimal(tyrantViews.size() == 0 ? 1:tyrantViews.size()),0,RoundingMode.DOWN));

        modelAndView.addObject("todayDto", activityConsoleNewmanTyrantService.obtainPrizeDto(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        modelAndView.addObject("tomorrowDto", activityConsoleNewmanTyrantService.obtainPrizeDto(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
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
        activityConsoleNewmanTyrantService.savePrize(newmanTyrantPrizeDto);
        return newmanTyrantPrizeDto;
    }


}
