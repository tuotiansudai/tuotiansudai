package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class InvestController {

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanController loanController;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setSource(Source.WEB);
        ModelAndView mv = null;
        String errorMessage = null;
        try {
            investDto.setLoginName(LoginUserInfo.getLoginName());
            BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
            if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                mv = new ModelAndView("/pay", "pay", baseDto);
            }
        } catch (InvestException e) {
            errorMessage = e.getMessage();
        }

        if (mv == null) {
            mv = loanController.getLoanDetail(investDto.getLoanIdLong());
            if(errorMessage == null){
                errorMessage = "投资失败";
            }
            mv.addObject("errorMessage", errorMessage);
            mv.addObject("investAmount", investDto.getAmount());
        }
        return mv;
    }
    @RequestMapping(value = "/calculate-expected-interest/loan/{loanId}/amount/{amount:^\\d+\\.\\d{2}$}", method = RequestMethod.GET)
    @ResponseBody
    public String calculateExpectedInterest(@PathVariable long loanId, @PathVariable String amount) {
        long expectedInterest = investService.estimateInvestIncome(loanId, AmountConverter.convertStringToCent(amount));
        return AmountConverter.convertCentToString(expectedInterest);
    }

}
