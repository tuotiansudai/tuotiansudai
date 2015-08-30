package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.LoanListWebDto;
import com.tuotiansudai.service.LoanService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/activityType/{activityType}/status/{status}/periodsStart/{periodsStart}/periodsEnd/{periodsEnd}/rateStart/{rateStart}/rateEnd/{rateEnd}/currentPageNo/{currentPageNo}", method = RequestMethod.GET)
    public ModelAndView loanListWeb(@PathVariable String activityType, @PathVariable String status, @PathVariable String periodsStart, @PathVariable String periodsEnd, @PathVariable String rateStart, @PathVariable String rateEnd, @PathVariable String currentPageNo) {
        int loanListCountWeb = loanService.findLoanListCountWeb(activityType, status, periodsStart, periodsEnd, rateStart, rateEnd);
        List<LoanListWebDto> LoanListWebDtos = loanService.findLoanListWeb(activityType, status, periodsStart, periodsEnd, rateStart, rateEnd, currentPageNo);
        ModelAndView modelAndView = new ModelAndView("/loanList");
        modelAndView.addObject("loanListCountWeb",loanListCountWeb);
        modelAndView.addObject("LoanListWebDtos",LoanListWebDtos);
        modelAndView.addObject("currentPageNo",currentPageNo);
        return modelAndView;
    }

}
