package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.service.LoanService;
import com.tuotiansudai.repository.model.LoanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/loan")
public class CreateLoanController {
    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.POST)
    public BaseDto<PayDataDto> createLoan(@RequestParam long loanId) {
        return loanService.createLoan(loanId);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public BaseDto<PayDataDto> updateLoan(@RequestParam long loanId,@RequestParam LoanStatus loanStatus) {
        return loanService.updateLoan(loanId,loanStatus);
    }
}
