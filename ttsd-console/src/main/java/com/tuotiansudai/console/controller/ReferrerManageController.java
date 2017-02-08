package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleReferrerManageService;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRewardStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.PaginationUtil;
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
    private ConsoleReferrerManageService consoleReferrerManageService;

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
                                       @RequestParam(value = "referrerRewardStatus", required = false) ReferrerRewardStatus referrerRewardStatus,
                                       @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        DateTime investDateTime = new DateTime(investEndTime);
        DateTime rewardDateTime = new DateTime(rewardEndTime);
        ModelAndView modelAndView = new ModelAndView("/referrer-manage");
        List<ReferrerManageView> referrerManageViews = consoleReferrerManageService.findReferrerManage(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source, referrerRewardStatus, index, pageSize);
        int referrerManageCount = consoleReferrerManageService.findReferrerManageCount(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source, referrerRewardStatus);
        long investAmountSum = consoleReferrerManageService.findReferrerManageInvestAmountSum(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source);
        long rewardAmountSum = consoleReferrerManageService.findReferrerManageRewardAmountSum(referrerMobile, investMobile, investStartTime, investEndTime != null ? investDateTime.plusDays(1).toDate() : investEndTime, level, rewardStartTime, rewardEndTime != null ? rewardDateTime.plusDays(1).toDate() : rewardEndTime, role, source);
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
        long totalPages = PaginationUtil.calculateMaxPage(referrerManageCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        Source[] sources = Source.values();
        modelAndView.addObject("sources", sources);
        modelAndView.addObject("referrerRewardStatuses", ReferrerRewardStatus.values());
        modelAndView.addObject("referrerRewardStatus", referrerRewardStatus);

        modelAndView.addObject("investAmountSum", investAmountSum);
        modelAndView.addObject("rewardAmountSum", rewardAmountSum);
        return modelAndView;
    }
}

