package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanerLoanRepayDataDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.web.util.LoginUserInfo;
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

    @Autowired
    private LoanService loanService;

    @Autowired
    private RepayService repayService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(path = "loan-list", method = RequestMethod.GET)
    public ModelAndView loanList() {
        ModelAndView modelAndView = new ModelAndView("/loaner-loan-list");
        AccountModel accountModel = accountService.findByLoginName(LoginUserInfo.getLoginName());
        modelAndView.addObject("autoRepay",accountModel.isAutoRepay());
        return modelAndView;
    }

    @RequestMapping(path = "/loan-list-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> loanData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                   @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                   @RequestParam(name = "status", required = false) LoanStatus status,
                                                   @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                   @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {

        return loanService.getLoanerLoanData(LoginUserInfo.getLoginName(), index, pageSize, status, startTime, endTime);
    }

    @RequestMapping(path = "/loan/{loanId:^\\d+$}/repay-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<LoanerLoanRepayDataDto> loanRepayData(@PathVariable long loanId) {
        repayService.resetPayExpiredLoanRepay(loanId);
        return repayService.getLoanRepay(LoginUserInfo.getLoginName(), loanId);
    }
}
