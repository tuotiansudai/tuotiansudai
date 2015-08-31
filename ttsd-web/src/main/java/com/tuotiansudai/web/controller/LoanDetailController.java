package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.InvestRecordRequestDto;
import com.tuotiansudai.dto.InvestRecordResponseDto;
import com.tuotiansudai.dto.LoanDetailDto;
import com.tuotiansudai.service.LoanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
public class LoanDetailController {

    @Autowired
    private LoanDetailService loanDetailService;

    @RequestMapping(value = "/loan/{loanId}",method = RequestMethod.GET)
    public ModelAndView getLoanDetail(@PathVariable String loanId) {
        LoanDetailDto dto = loanDetailService.getLoanDetail(loanId);
        return new ModelAndView("/loan-detail", "loan", dto);
    }
    @RequestMapping(value = "/loan/{loanId}/amount/{amount:^\\d+\\.\\d{2}$}",method = RequestMethod.GET)
    public String getExpectedTotalIncome(@PathVariable String loanId,@PathVariable String amount) {
        String expectedTotalIncome = loanDetailService.getExpectedTotalIncome(loanId, amount);
        return expectedTotalIncome;
    }
    @RequestMapping(value = "/get/invest",method = RequestMethod.POST)
    @ResponseBody
    public InvestRecordResponseDto getInvestList(@Valid @RequestBody InvestRecordRequestDto investRecordRequestDto){
        InvestRecordResponseDto dto = loanDetailService.getInvests(investRecordRequestDto);
        return dto;
    }




}
