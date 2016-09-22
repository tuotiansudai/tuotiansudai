package com.tuotiansudai.console.activity.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.ActivityCategory;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.PrizeType;
import com.tuotiansudai.console.activity.service.UserLotteryService;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class LotteryController {

    @Autowired
    private UserLotteryService userLotteryService;

    @RequestMapping(value = "/user-time-list", method = RequestMethod.GET)
    public ModelAndView userLotteryList(@RequestParam(name = "mobile", required = false) String mobile,
                                        @RequestParam(name = "prizeType", required = false) ActivityCategory prizeType,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) throws IOException {
        ModelAndView modelAndView = new ModelAndView("/activity-time-list");
        int lotteryCount = userLotteryService.findUserLotteryTimeCountViews(mobile);
        prizeType = prizeType == null ? ActivityCategory.AUTUMN_PRIZE : prizeType;
        modelAndView.addObject("lotteryCount", lotteryCount);
        modelAndView.addObject("lotteryList", userLotteryService.findUserLotteryTimeViews(mobile, prizeType, (index - 1) * pageSize, pageSize));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = PaginationUtil.calculateMaxPage(lotteryCount,pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("prizeTypes", Lists.newArrayList(ActivityCategory.values()));
        modelAndView.addObject("selectPrize", prizeType == null ? "" : prizeType);
        return modelAndView;
    }


    @RequestMapping(value = "/user-prize-list", method = RequestMethod.GET)
    public ModelAndView userPrizeList(@RequestParam(name = "mobile", required = false) String mobile,
                                      @RequestParam(name = "selectPrize", required = false) LotteryPrize autumnPrize,
                                      @RequestParam(name = "selectNational", required = false) LotteryPrize NationalPrize,
                                      @RequestParam(name = "prizeType", required = false ,defaultValue = "AUTUMN_PRIZE") ActivityCategory prizeType,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/activity-prize-list");
        LotteryPrize lotteryPrize = prizeType.equals(ActivityCategory.AUTUMN_PRIZE) ? autumnPrize : NationalPrize;
        int lotteryCount = userLotteryService.findUserLotteryPrizeCountViews(mobile, lotteryPrize, prizeType, startTime, endTime);
        modelAndView.addObject("lotteryCount", lotteryCount);
        modelAndView.addObject("prizeList", userLotteryService.findUserLotteryPrizeViews(mobile, lotteryPrize, prizeType, startTime, endTime, (index - 1) * pageSize, pageSize));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = PaginationUtil.calculateMaxPage(lotteryCount,pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("selectPrize", autumnPrize == null ? "" : autumnPrize);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("selectPrizeType", prizeType == null ? "" : prizeType);
        modelAndView.addObject("prizeTypes", Lists.newArrayList(ActivityCategory.values()));
        modelAndView.addObject("lotteryPrizes", getActivityPrize(ActivityCategory.AUTUMN_PRIZE));
        modelAndView.addObject("nationalPrizes", getActivityPrize(ActivityCategory.NATIONAL_PRIZE));
        return modelAndView;
    }

    private List getActivityPrize(ActivityCategory activityCategory){
        List list = Lists.newArrayList();
        LotteryPrize[] lotteryPrizes = LotteryPrize.values();
        for(LotteryPrize lotteryPrize : lotteryPrizes){
            if(activityCategory.equals(lotteryPrize.getActivityCategory())){
                list.add(lotteryPrize);
            }
        }
        return list;
    }
}
