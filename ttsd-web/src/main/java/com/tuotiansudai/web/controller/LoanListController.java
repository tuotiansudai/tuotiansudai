package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.LoanListWebDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.ProductLineType;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.web.freemarker.directive.AmountDirective;
import com.tuotiansudai.web.freemarker.directive.PercentFractionDirective;
import com.tuotiansudai.web.freemarker.directive.PercentIntegerDirective;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/loan-list")
public class LoanListController {

    static Logger logger = Logger.getLogger(LoanListController.class);

    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView webLoanList(@RequestParam(value = "productLineType", required = false) ProductLineType productLineType,
                                    @RequestParam(value = "status", required = false) LoanStatus status,
                                    @RequestParam(value = "periodsStart", defaultValue = "0", required = false) long periodsStart,
                                    @RequestParam(value = "periodsEnd", defaultValue = "0", required = false) long periodsEnd,
                                    @RequestParam(value = "rateStart", defaultValue = "0", required = false) double rateStart,
                                    @RequestParam(value = "rateEnd", defaultValue = "0", required = false) double rateEnd,
                                    @RequestParam(value = "currentPageNo", defaultValue = "1", required = false) int currentPageNo) {
        int loanListCountWeb = loanService.findLoanListCountWeb(productLineType, status, periodsStart, periodsEnd, rateStart, rateEnd);
        List<LoanListWebDto> loanListWebDtos = loanService.findLoanListWeb(productLineType, status, periodsStart, periodsEnd, rateStart, rateEnd, currentPageNo);
        ModelAndView modelAndView = new ModelAndView("/loan-list");
        modelAndView.addObject("loanListCountWeb", loanListCountWeb);
        modelAndView.addObject("loanListWebDtos", loanListWebDtos);
        modelAndView.addObject("currentPageNo", currentPageNo);
        modelAndView.addObject("periodsStart", periodsStart);
        modelAndView.addObject("periodsEnd", periodsEnd);
        modelAndView.addObject("rateStart", rateStart);
        modelAndView.addObject("rateEnd", rateEnd);
        modelAndView.addObject("productLineType", productLineType);
        modelAndView.addObject("status", status);
        long totalPages = loanListCountWeb / 10 + (loanListCountWeb % 10 > 0 ? 1 : 0);
        boolean hasPreviousPage = currentPageNo > 1 && currentPageNo <= totalPages;
        boolean hasNextPage = currentPageNo < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("percentFraction",new PercentFractionDirective());
        modelAndView.addObject("percentInteger",new PercentIntegerDirective());
        modelAndView.addObject("amount",new AmountDirective());
        return modelAndView;
    }

}
