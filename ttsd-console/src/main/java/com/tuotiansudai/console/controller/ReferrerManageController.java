package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.Role;
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
public class ReferrerManageController {

    @Autowired
    private ReferrerManageService referrerManageService;

    @RequestMapping(value = "/referrerManage", method = RequestMethod.GET)
    public ModelAndView referrerManage(@RequestParam(value = "referrerLoginName",required = false) String referrerLoginName,
                                        @RequestParam(value = "investLoginName",required = false) String investLoginName,
                                        @RequestParam(value = "investStartTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date investStartTime,
                                        @RequestParam(value = "investEndTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date investEndTime,
                                        @RequestParam(value = "level",required = false) Integer level,
                                        @RequestParam(value = "rewardStartTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date rewardStartTime,
                                        @RequestParam(value = "rewardEndTime",required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date rewardEndTime,
                                        @RequestParam(value = "role",required = false) Role role,
                                        @RequestParam(value = "currentPageNo",defaultValue = "1",required = false) int currentPageNo,
                                        @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/referrer-manage");
        DateTime investDateTime = new DateTime(investEndTime);
        DateTime rewardDateTime = new DateTime(rewardEndTime);
        List<ReferrerManageView> referrerManageViews = referrerManageService.findReferrerManage(referrerLoginName,investLoginName,investStartTime,investEndTime!=null?investDateTime.plusDays(1).toDate():investEndTime,level,rewardStartTime,rewardEndTime!=null?rewardDateTime.plusDays(1).toDate():rewardEndTime,role,currentPageNo,pageSize);
        int referrerManageCount = referrerManageService.findReferrerManageCount(referrerLoginName, investLoginName, investStartTime, investEndTime!=null?investDateTime.plusDays(1).toDate():investEndTime, level, rewardStartTime, rewardEndTime!=null?rewardDateTime.plusDays(1).toDate():rewardEndTime, role);
        modelAndView.addObject("referrerLoginName",referrerLoginName);
        modelAndView.addObject("investLoginName",investLoginName);
        modelAndView.addObject("investStartTime",investStartTime);
        modelAndView.addObject("investEndTime",investEndTime);
        modelAndView.addObject("level",level);
        modelAndView.addObject("rewardStartTime",rewardStartTime);
        modelAndView.addObject("rewardEndTime",rewardEndTime);
        modelAndView.addObject("role",role);
        modelAndView.addObject("currentPageNo",currentPageNo);
        modelAndView.addObject("pageSize",pageSize);
        modelAndView.addObject("referrerManageViews",referrerManageViews);
        modelAndView.addObject("referrerManageCount",referrerManageCount);
        long totalPages = referrerManageCount / pageSize + (referrerManageCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
        boolean hasNextPage = currentPageNo < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);
        return modelAndView;
    }

}
