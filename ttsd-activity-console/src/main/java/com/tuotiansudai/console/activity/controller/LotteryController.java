package com.tuotiansudai.console.activity.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.NationalPrize;
import com.tuotiansudai.activity.dto.PrizeType;
import com.tuotiansudai.console.activity.service.UserLotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class LotteryController {

    @Autowired
    private UserLotteryService userLotteryService;

    @RequestMapping(value = "/user-time-list", method = RequestMethod.GET)
    public ModelAndView userLotteryList(@RequestParam(name = "mobile", required = false) String mobile,
                                        @RequestParam(name = "prizeType", required = false) PrizeType prizeType,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) throws IOException {
        ModelAndView modelAndView = new ModelAndView("/activity-time-list");
        int lotteryCount = userLotteryService.findUserLotteryTimeCountViews(mobile);
        prizeType = prizeType == null ? PrizeType.AUTUMN_PRIZE : prizeType;
        modelAndView.addObject("lotteryCount", lotteryCount);
        modelAndView.addObject("lotteryList", userLotteryService.findUserLotteryTimeViews(mobile, prizeType, (index - 1) * pageSize, pageSize));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = lotteryCount / pageSize + (lotteryCount % pageSize > 0 || lotteryCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("prizeTypes", Lists.newArrayList(PrizeType.values()));
        modelAndView.addObject("selectPrize", prizeType == null ? "" : prizeType);
        return modelAndView;
    }


    @RequestMapping(value = "/user-prize-list", method = RequestMethod.GET)
    public ModelAndView userPrizeList(@RequestParam(name = "mobile", required = false) String mobile,
                                      @RequestParam(name = "selectPrize", required = false) LotteryPrize selectPrize,
                                      @RequestParam(name = "selectNational", required = false) LotteryPrize selectNational,
                                      @RequestParam(name = "prizeType", required = false) PrizeType prizeType,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/activity-prize-list");
        prizeType = prizeType == null ? PrizeType.AUTUMN_PRIZE : prizeType;
        int lotteryCount = userLotteryService.findUserLotteryPrizeCountViews(mobile, selectPrize == null ? selectNational.toString() : selectPrize.toString(), prizeType, startTime, endTime);
        modelAndView.addObject("lotteryCount", lotteryCount);
        modelAndView.addObject("prizeList", userLotteryService.findUserLotteryPrizeViews(mobile, selectPrize == null ? selectNational.toString() : selectPrize.toString(), prizeType,startTime, endTime, (index - 1) * pageSize, pageSize));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = lotteryCount / pageSize + (lotteryCount % pageSize > 0 || lotteryCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("lotteryPrizes", Lists.newArrayList(LotteryPrize.values()));
        modelAndView.addObject("selectPrize", selectPrize == null ? "" : selectPrize);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("selectPrizeType",prizeType);
        modelAndView.addObject("prizeTypes", Lists.newArrayList(PrizeType.values()));
        modelAndView.addObject("nationals", Lists.newArrayList(NationalPrize.values()));
        modelAndView.addObject("selectNational",selectNational == null ? "" : selectNational);
        return modelAndView;
    }
}
