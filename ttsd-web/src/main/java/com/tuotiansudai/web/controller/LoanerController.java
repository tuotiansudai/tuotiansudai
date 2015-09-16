package com.tuotiansudai.web.controller;


import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(path = "/loaner")
public class LoanerController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loanManagement(@RequestParam(name = "index") int index,
                                       @RequestParam(name = "pageSize") int pageSize,
                                       @RequestParam(name = "startDate") Date startDate,
                                       @RequestParam(name = "endDate") Date endDate,
                                       @RequestParam(name = "status") LoanStatus loanStatus) {
        return null;
    }

    @RequestMapping(path = "/loan-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ModelAndView loanData(@RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                 @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                 @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                                 @RequestParam(name = "endDate", required = false) Date endDate,
                                 @RequestParam(name = "status", required = false) LoanStatus status) {

        loanService.getLoanerLoanData(index, pageSize, startDate, endDate, startDate, status);
        return null;
    }
}
