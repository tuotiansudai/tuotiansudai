package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.InvestAchievementService;
import com.tuotiansudai.repository.model.LoanAchievementView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/activity-manage")
public class InvestAchievementController {

    static Logger logger = Logger.getLogger(InvestAchievementController.class);

    @Autowired
    private InvestAchievementService investAchievementService;

    @RequestMapping(value = "/invest-achievement", method = RequestMethod.GET)
    public ModelAndView investAchievement(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                          @RequestParam(value = "mobile", required = false) String mobile) {
        List<LoanAchievementView> loanAchievementViews = investAchievementService.findInvestAchievement(index, pageSize, mobile);
        ModelAndView modelAndView = new ModelAndView("/invest-achievement");
        modelAndView.addObject("loanAchievementViews", loanAchievementViews);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("mobile", mobile);
        long investAchievementCount = investAchievementService.findInvestAchievementCount(mobile);
        long totalPages = investAchievementCount / pageSize + (investAchievementCount % pageSize > 0 || investAchievementCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("investAchievementCount", investAchievementCount);
        return modelAndView;
    }

}
