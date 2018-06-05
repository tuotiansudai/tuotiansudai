package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanerLoanRepayDataDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;

@Controller
@RequestMapping(path = "/loaner")
public class LoanerController {

    private final LoanService loanService;

    private final RepayService repayService;

    @Autowired
    public LoanerController(LoanService loanService, RepayService repayService) {
        this.loanService = loanService;
        this.repayService = repayService;
    }

    @RequestMapping(path = "loan-list", method = RequestMethod.GET)
    public ModelAndView loanList() {
        return new ModelAndView("/loaner-loan-list");
    }

    @RequestMapping(path = "/loan-list-data", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> loanData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                   @RequestParam(name = "status", required = false) LoanStatus status,
                                                   @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                   @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {

        return loanService.getLoanerLoanData(LoginUserInfo.getLoginName(), index, 10, status, startTime, endTime);
    }

    @RequestMapping(path = "/loan/{loanId:^\\d+$}/repay-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<LoanerLoanRepayDataDto> getLoanRepayData(@PathVariable long loanId) {
        return repayService.getLoanRepay(LoginUserInfo.getLoginName(), loanId);
    }
}
