package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/loan")
public class CreateLoanController {
    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.POST)
    public BaseDto<PayDataDto> createLoan(@RequestBody LoanDto loanDto) {
        return loanService.createLoan(loanDto);
    }
}
