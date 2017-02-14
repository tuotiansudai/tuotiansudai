package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.loanout.LoanService;
import com.tuotiansudai.repository.model.LoanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @RequestMapping(path = "/{loanId}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> createLoan(@PathVariable long loanId) {
        return loanService.createLoan(loanId);
    }

    @RequestMapping(path = "/{loanId}/status/{status}", method = RequestMethod.PUT)
    @ResponseBody
    public BaseDto<PayDataDto> updateLoanStatus(@PathVariable long loanId, @PathVariable LoanStatus status) {
        return loanService.updateLoanStatus(loanId, status);
    }

    @ResponseBody
    @RequestMapping(value = "/{loanId}/loan-out", method = RequestMethod.POST)
    public BaseDto<PayDataDto> loanOut(@PathVariable long loanId){
        return loanService.loanOut(loanId);
    }

    @RequestMapping(value = "/{loanId}/cancel", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> cancelLoan(@PathVariable Long loanId) {
        return loanService.cancelLoan(loanId);
    }

}
