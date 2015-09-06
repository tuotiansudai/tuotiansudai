package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.LoanOutDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> createLoan(@RequestBody LoanDto loanDto) {
        return loanService.createLoan(loanDto.getId());
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public BaseDto<PayDataDto> updateLoan(@RequestBody LoanDto loanDto) {
        return loanService.updateLoanStatus(loanDto.getId(), loanDto.getLoanStatus());
    }

    // 放款
    @ResponseBody
    @RequestMapping(value = "/loanout", method = RequestMethod.POST)
    public BaseDto<PayDataDto> loanOut(@RequestBody LoanOutDto loanOutDto){
        return loanService.loanOut(loanOutDto.getLoanId(), loanOutDto.getLoginName(), loanOutDto.getAmount());
    }
}
