package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.BasePaginationDto;
import com.tuotiansudai.dto.LoanRepayDto;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.LoanRepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/loan-repay")
public class LoanRepayController {
    @Autowired
    private LoanRepayService loanRepayService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView findLoanRepayPagination(@Valid @ModelAttribute LoanRepayDto loanRepayDto){
        ModelAndView modelAndView = new ModelAndView("/loan-repay");
        BasePaginationDto baseDto = loanRepayService.findLoanRepayPagination(loanRepayDto);
        List<RepayStatus> repayStatusList = loanRepayService.findAllRepayStatus();
        modelAndView.addObject("baseDto",baseDto);
        modelAndView.addObject("loanRepayDto",loanRepayDto);
        modelAndView.addObject("repayStatusList",repayStatusList);
        return modelAndView;
    }


}
