package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.LoanListWebDto;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.LoanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
@Controller
@RequestMapping("/loanList")
public class LoanListController {

    static Logger logger = Logger.getLogger(LoanListController.class);

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/web",method = RequestMethod.GET)
    public ModelAndView webLoanList(@RequestParam(value = "activityType",required = false) ActivityType activityType,
                                     @RequestParam(value = "status",required = false) LoanStatus status,
                                     @RequestParam(value = "periodsStart",defaultValue = "0",required = false) long periodsStart,
                                     @RequestParam(value = "periodsEnd",defaultValue = "0",required = false) long periodsEnd,
                                     @RequestParam(value = "rateStart",defaultValue = "0",required = false) double rateStart,
                                     @RequestParam(value = "rateEnd",defaultValue = "0",required = false) double rateEnd,
                                     @RequestParam(value = "currentPageNo",defaultValue = "1",required = false) int currentPageNo){
        int loanListCountWeb = loanService.findLoanListCountWeb(activityType, status, periodsStart, periodsEnd, rateStart, rateEnd);
        List<LoanListWebDto> loanListWebDtos = loanService.findLoanListWeb(activityType, status, periodsStart, periodsEnd, rateStart, rateEnd, currentPageNo);
        ModelAndView modelAndView = new ModelAndView("/loan-list");
        modelAndView.addObject("loanListCountWeb",loanListCountWeb);
        modelAndView.addObject("loanListWebDtos",loanListWebDtos);
        modelAndView.addObject("currentPageNo",currentPageNo);
        modelAndView.addObject("periodsStart",periodsStart);
        modelAndView.addObject("periodsEnd",periodsEnd);
        modelAndView.addObject("rateStart",rateStart);
        modelAndView.addObject("rateEnd",rateEnd);
        long totalPages = loanListCountWeb / 10 + (loanListCountWeb % 10 > 0 ? 1 : 0);
        boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
        boolean hasNextPage = currentPageNo < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);
        return modelAndView;
    }

}
