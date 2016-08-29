package com.tuotiansudai.console.activity.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.LotteryPrize;
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
    public ModelAndView userLotteryList(@RequestParam(name = "mobile",required = false) String mobile,
                                        @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                        @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) throws IOException {
            ModelAndView modelAndView = new ModelAndView("/activity-time-list");
            int lotteryCount = userLotteryService.findUserLotteryTimeCountViews(mobile);
            modelAndView.addObject("lotteryCount", lotteryCount);
            modelAndView.addObject("lotteryList",userLotteryService.findUserLotteryTimeViews(mobile, (index - 1) * pageSize, pageSize));
            modelAndView.addObject("index",index);
            modelAndView.addObject("pageSize",pageSize);
            long totalPages = lotteryCount / pageSize + (lotteryCount % pageSize > 0 || lotteryCount == 0 ? 1 : 0);
            boolean hasPreviousPage = index > 1 && index <= totalPages;
            boolean hasNextPage = index < totalPages;
            modelAndView.addObject("hasPreviousPage", hasPreviousPage);
            modelAndView.addObject("hasNextPage", hasNextPage);
            modelAndView.addObject("mobile",mobile);
            return modelAndView;
    }


    @RequestMapping(value = "/user-prize-list", method = RequestMethod.GET)
    public ModelAndView userPrizeList(@RequestParam(name = "mobile",required = false) String mobile,
                                      @RequestParam(name = "selectPrize",required = false) LotteryPrize selectPrize,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                      @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize){
            ModelAndView modelAndView = new ModelAndView("/activity-prize-list");
            int lotteryCount = userLotteryService.findUserLotteryPrizeCountViews(mobile,selectPrize,startTime,endTime);
            modelAndView.addObject("lotteryCount", lotteryCount);
            modelAndView.addObject("prizeList",userLotteryService.findUserLotteryPrizeViews(mobile,selectPrize,startTime,endTime, (index - 1) * pageSize, pageSize));
            modelAndView.addObject("index",index);
            modelAndView.addObject("pageSize",pageSize);
            long totalPages = lotteryCount / pageSize + (lotteryCount % pageSize > 0 || lotteryCount == 0 ? 1 : 0);
            boolean hasPreviousPage = index > 1 && index <= totalPages;
            boolean hasNextPage = index < totalPages;
            modelAndView.addObject("hasPreviousPage", hasPreviousPage);
            modelAndView.addObject("hasNextPage", hasNextPage);
            modelAndView.addObject("mobile",mobile);
            modelAndView.addObject("lotteryPrizes",Lists.newArrayList(LotteryPrize.values()));
            modelAndView.addObject("selectPrize",selectPrize == null ? "" : selectPrize);
            modelAndView.addObject("startTime",startTime);
            modelAndView.addObject("endTime",endTime);
            return modelAndView;
    }
}
