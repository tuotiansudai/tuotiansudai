package com.tuotiansudai.console.activity.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import com.tuotiansudai.console.activity.service.UserLotteryService;
import com.tuotiansudai.console.activity.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class LotteryController {
    static Logger logger = Logger.getLogger(LotteryController.class);

    @Autowired
    private UserLotteryService userLotteryService;

    @RequestMapping(value = "/user-time-list", method = RequestMethod.GET)
    public ModelAndView userLotteryList(@RequestParam(name = "mobile",required = false) String mobile,
                                        @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                        @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                        @RequestParam(value = "export", required = false) String export,
                                        HttpServletResponse response) throws IOException {
        if (export != null && !export.equals("")) {
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.UserDrawTimeList.getDescription(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            response.setContentType("application/csv");
            List<UserLotteryTimeView> userLotteryTimeModels = userLotteryService.findUserLotteryTimeViews(mobile, 0, Integer.MAX_VALUE);
            List<List<String>> data = Lists.newArrayList();
            for(UserLotteryTimeView userLotteryTimeView : userLotteryTimeModels){
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(userLotteryTimeView.getMobile());
                dataModel.add(userLotteryTimeView.getUserName());
                dataModel.add(String.valueOf(userLotteryTimeView.getUseCount()));
                dataModel.add(String.valueOf(userLotteryTimeView.getUnUseCount()));
                data.add(dataModel);
            }

            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.UserDrawTimeList.getHeader(), data, response.getOutputStream());
            return null;
        }else{
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
    }


    @RequestMapping(value = "/user-prize-list", method = RequestMethod.GET)
    public ModelAndView userPrizeList(@RequestParam(name = "mobile",required = false) String mobile,
                                      @RequestParam(name = "selectPrize",required = false) LotteryPrize selectPrize,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                      @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                      @RequestParam(value = "export", required = false) String export,
                                      HttpServletResponse response) throws IOException {
        if (export != null && !export.equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            response.setCharacterEncoding("UTF-8");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.UserPrizeList.getDescription(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            response.setContentType("application/csv");
            List<UserLotteryPrizeView> userLotteryPrizeModels = userLotteryService.findUserLotteryPrizeViews(mobile, selectPrize, startTime,endTime,  0, Integer.MAX_VALUE);
            List<List<String>> data = Lists.newArrayList();
            for(UserLotteryPrizeView prize : userLotteryPrizeModels){
                List<String> dataModel = Lists.newArrayList();
                dataModel.add(simpleDateFormat.format(prize.getLotteryTime()));
                dataModel.add(prize.getMobile());
                dataModel.add(prize.getUserName());
                dataModel.add(prize.getPrize().getDescription());
                data.add(dataModel);
            }

            ExportCsvUtil.createCsvOutputStream(CsvHeaderType.UserPrizeList.getHeader(), data, response.getOutputStream());
            return null;
        }else{
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
}
