package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ReferrerManageService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/user-manage")
public class ReferrerManageController {

    @Autowired
    private ReferrerManageService referrerManageService;

    @RequestMapping(value = "/referrer", method = RequestMethod.GET)
    public ModelAndView referrerManage(@RequestParam(value = "referrerMobile", required = false) String referrerMobile,
                                       @RequestParam(value = "investMobile", required = false) String investMobile,
                                       @RequestParam(value = "investStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date investStartTime,
                                       @RequestParam(value = "investEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date investEndTime,
                                       @RequestParam(value = "level", required = false) Integer level,
                                       @RequestParam(value = "rewardStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date rewardStartTime,
                                       @RequestParam(value = "rewardEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date rewardEndTime,
                                       @RequestParam(value = "role", required = false) Role role,
                                       @RequestParam(value = "source", required = false) Source source,
                                       @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                       @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        DateTime investDateTime = new DateTime(investEndTime);
        DateTime rewardDateTime = new DateTime(rewardEndTime);
        ModelAndView modelAndView = new ModelAndView("/referrer-manage");
        List<ReferrerManageView> referrerManageViews = referrerManageService.findReferrerManage(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source, index, pageSize);
        int referrerManageCount = referrerManageService.findReferrerManageCount(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source);
        long investAmountSum = referrerManageService.findReferrerManageInvestAmountSum(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source);
        long rewardAmountSum = referrerManageService.findReferrerManageRewardAmountSum(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source);
        modelAndView.addObject("referrerMobile", referrerMobile);
        modelAndView.addObject("investMobile", investMobile);
        modelAndView.addObject("investStartTime", investStartTime);
        modelAndView.addObject("investEndTime", investEndTime);
        modelAndView.addObject("level", level);
        modelAndView.addObject("rewardStartTime", rewardStartTime);
        modelAndView.addObject("rewardEndTime", rewardEndTime);
        modelAndView.addObject("role", role);
        modelAndView.addObject("source", source);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("referrerManageViews", referrerManageViews);
        modelAndView.addObject("referrerManageCount", referrerManageCount);
        long totalPages = referrerManageCount / pageSize + (referrerManageCount % pageSize > 0 || referrerManageCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        Source[] sources = Source.values();
        modelAndView.addObject("sources", sources);
        modelAndView.addObject("investAmountSum", investAmountSum);
        modelAndView.addObject("rewardAmountSum", rewardAmountSum);
        return modelAndView;
    }
}

