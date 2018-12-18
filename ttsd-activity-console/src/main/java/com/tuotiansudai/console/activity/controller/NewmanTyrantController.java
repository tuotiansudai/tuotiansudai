package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.dto.NewmanTyrantPrizeDto;
import com.tuotiansudai.activity.repository.model.NewmanTyrantHistoryView;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.console.activity.service.ActivityConsoleNewmanTyrantService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/newman-tyrant")
public class NewmanTyrantController {
    @Autowired
    private ActivityConsoleNewmanTyrantService activityConsoleNewmanTyrantService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView newmanTyrant(@RequestParam(value = "tradingTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {

        ModelAndView modelAndView = new ModelAndView("/newman-tyrant");

        if (tradingTime == null) {
            tradingTime = new Date();
        }


        List<NewmanTyrantView> newmanViews = activityConsoleNewmanTyrantService.obtainNewman(tradingTime);

        List<NewmanTyrantView> tyrantViews = activityConsoleNewmanTyrantService.obtainNewmanViaMiddleAutum(tradingTime);

        List<NewmanTyrantHistoryView> newmanTyrantHistoryViews = activityConsoleNewmanTyrantService.obtainNewmanTyrantHistoryRanking(tradingTime);
        long avgTyrantInvestAmount =tyrantViews.stream().mapToLong(heroRankingView -> heroRankingView.getSumAmount()).sum();
        avgTyrantInvestAmount = new BigDecimal(avgTyrantInvestAmount).divide(new BigDecimal(tyrantViews.size() == 0 ? 1 : tyrantViews.size()), 0, RoundingMode.DOWN).longValue();
        modelAndView.addObject("tradingTime", tradingTime);

        modelAndView.addObject("newmanViews", newmanViews);

        modelAndView.addObject("tyrantViews", tyrantViews);

        modelAndView.addObject("avgNewmanInvestAmount", newmanTyrantHistoryViews.size() > 0 ? newmanTyrantHistoryViews.get(0).getAvgNewmanInvestAmount() : 0);
        modelAndView.addObject("avgTyrantInvestAmount", avgTyrantInvestAmount);

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
