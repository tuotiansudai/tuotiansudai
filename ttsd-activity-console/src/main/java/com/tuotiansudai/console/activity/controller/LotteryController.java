package com.tuotiansudai.console.activity.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.AutumnExportDto;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.console.activity.service.ExportService;
import com.tuotiansudai.console.activity.service.UserLotteryService;
import com.tuotiansudai.util.CsvHeaderType;
import com.tuotiansudai.util.ExportCsvUtil;
import com.tuotiansudai.util.PaginationUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class LotteryController {

    static Logger logger = Logger.getLogger(LotteryController.class);

    @Autowired
    private UserLotteryService userLotteryService;

    @Autowired
    private ExportService exportService;

    @RequestMapping(value = "/user-time-list", method = RequestMethod.GET)
    public ModelAndView userLotteryList(@RequestParam(name = "mobile", required = false) String mobile,
                                        @RequestParam(name = "prizeType",  defaultValue = "AUTUMN_PRIZE",required = false) ActivityCategory prizeType,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) throws IOException {
        ModelAndView modelAndView = new ModelAndView("/activity-time-list");
        int lotteryCount = userLotteryService.findUserLotteryTimeCountViews(mobile);
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
        modelAndView.addObject("prizeTypes", getActivityCategory());
        modelAndView.addObject("selectPrize", prizeType == null ? "" : prizeType);
        return modelAndView;
    }


    @RequestMapping(value = "/user-prize-list", method = RequestMethod.GET)
    public ModelAndView userPrizeList(@RequestParam(name = "mobile", required = false) String mobile,
                                      @RequestParam(name = "selectPrize", required = false) LotteryPrize lotteryPrize,
                                      @RequestParam(name = "prizeType", required = false ,defaultValue = "AUTUMN_PRIZE") ActivityCategory activityCategory,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                      @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/activity-prize-list");
        int lotteryCount = userLotteryService.findUserLotteryPrizeCountViews(mobile, lotteryPrize, activityCategory, startTime, endTime);
        modelAndView.addObject("lotteryCount", lotteryCount);
        modelAndView.addObject("prizeList", userLotteryService.findUserLotteryPrizeViews(mobile, lotteryPrize, activityCategory, startTime, endTime, (index - 1) * pageSize, pageSize));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = PaginationUtil.calculateMaxPage(lotteryCount,pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("selectPrize", lotteryPrize == null ? "" : lotteryPrize);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("selectPrizeType", activityCategory == null ? "" : activityCategory);
        modelAndView.addObject("prizeTypes", Lists.newArrayList(ActivityCategory.values()));
        modelAndView.addObject("lotteryPrizes", getActivityPrize(activityCategory));
        return modelAndView;
    }


    @ResponseBody
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public List<LotteryPrizeView> getActivityPrize(@RequestParam(name = "activityCategory", required = false) ActivityCategory activityCategory){
        List list = Lists.newArrayList();
        LotteryPrize[] lotteryPrizes = LotteryPrize.values();
        for(LotteryPrize lotteryPrize : lotteryPrizes) {
            if (activityCategory.equals(lotteryPrize.getActivityCategory())) {
                list.add(new LotteryPrizeView(lotteryPrize.name(), lotteryPrize.getDescription()));
            }
        }
        return list;
    }


    @RequestMapping(value = "/export-prize", method = RequestMethod.GET)
    public void autumnActivityListExport(@RequestParam(name = "mobile", required = false) String mobile,
                                         @RequestParam(name = "selectPrize", required = false) LotteryPrize lotteryPrize,
                                         @RequestParam(name = "prizeType", required = false ,defaultValue = "AUTUMN_PRIZE") ActivityCategory activityCategory,
                                         @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                         @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                         HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(CsvHeaderType.LotteryPrizeHeader.getDescription() + new DateTime().toString("yyyyMMddHHmmSS") + ".csv", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("[活动导出]export prize error",e);
        }
        response.setContentType("application/csv");

        List<List<String>> csvData = userLotteryService.buildAutumnList(mobile, lotteryPrize, activityCategory, startTime, endTime);
        ExportCsvUtil.createCsvOutputStream(CsvHeaderType.LotteryPrizeHeader, csvData, response.getOutputStream());
    }

    private List<ActivityCategory> getActivityCategory(){
        List<ActivityCategory> activityList = Lists.newArrayList();
        Lists.newArrayList(ActivityCategory.values()).forEach(activityCategory -> {
            if(activityCategory.getConsumeCategory() != null && activityCategory.getConsumeCategory().equals(ConsumeCategory.TASK_COUNT)){
                activityList.add(activityCategory);
            }
        });
        return activityList;
    }
}
