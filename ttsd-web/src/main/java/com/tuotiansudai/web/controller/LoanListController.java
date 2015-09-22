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
    public ModelAndView webLoanList(@RequestParam("activityType") ActivityType activityType, @RequestParam("status") LoanStatus status,
                                     @RequestParam("periodsStart") long periodsStart, @RequestParam("periodsEnd") long periodsEnd,
                                     @RequestParam("rateStart") double rateStart, @RequestParam("rateEnd") double rateEnd,
                                     @RequestParam("currentPageNo") int currentPageNo){
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
