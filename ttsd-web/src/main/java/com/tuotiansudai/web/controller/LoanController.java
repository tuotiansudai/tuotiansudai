package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/{loanId}", method = RequestMethod.GET)
    public ModelAndView getLoanDetail(@PathVariable String loanId) {
        BaseDto dto = loanService.getLoanDetail(loanId);
        return new ModelAndView("/loan-detail", "baseDto", dto);
    }

    @RequestMapping(value = "/{loanId}/amount/{amount:^\\d+\\.\\d{2}$}", method = RequestMethod.GET)
    public String getExpectedTotalIncome(@PathVariable long loanId, @PathVariable double amount) {
        String expectedTotalIncome = loanService.getExpectedTotalIncome(loanId, amount);
        return expectedTotalIncome;
    }

    @RequestMapping(value = "/{loanId}/index/{index}/pageSize/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto getInvestList(@PathVariable long loanId, @PathVariable int index, @PathVariable int pageSize) {
        BaseDto dto = loanService.getInvests(loanId, index, pageSize);
        return dto;
    }


}
