package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.LoanService;
import org.apache.log4j.Logger;
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
@RequestMapping("/project-manage")
public class LoanListController {

    static Logger logger = Logger.getLogger(LoanListController.class);

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/loan-list", method = RequestMethod.GET)
    public ModelAndView ConsoleLoanList(@RequestParam(value = "status", required = false) LoanStatus status,
                                        @RequestParam(value = "loanId", required = false) Long loanId,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                        @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                        @RequestParam(value = "loanName", required = false) String loanName,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        int loanListCount = loanService.findLoanListCount(status, loanId, loanName,
                startTime == null ? new DateTime(0).toDate() : startTime,
                endTime == null ? new DateTime(9999, 12, 31, 0, 0, 0).toDate() : endTime);
        List<LoanListDto> loanListDtos = loanService.findLoanList(status, loanId, loanName,
                startTime == null ? new DateTime(0).toDate() : startTime,
                endTime == null ? new DateTime(9999, 12, 31, 0, 0, 0).toDate() : endTime,
                index, pageSize);
        ModelAndView modelAndView = new ModelAndView("/loan-list");
        modelAndView.addObject("loanListCount", loanListCount);
        modelAndView.addObject("loanListDtos", loanListDtos);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = loanListCount / pageSize + (loanListCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("status", status);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("loanName", loanName);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        return modelAndView;
    }

}
