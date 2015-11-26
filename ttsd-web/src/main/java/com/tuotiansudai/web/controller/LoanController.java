package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.web.freemarker.directive.AmountDirective;
import com.tuotiansudai.web.freemarker.directive.PercentIntegerDirective;

import com.tuotiansudai.web.util.LoginUserInfo;
import com.tuotiansudai.web.freemarker.directive.PercentFractionDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;


@Controller
@RequestMapping(value = "/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/{loanId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getLoanDetail(@PathVariable long loanId) {
        ModelAndView modelAndView = new ModelAndView("/loan");
        BaseDto<LoanDto> dto = loanService.getLoanDetail(LoginUserInfo.getLoginName(), loanId);
        modelAndView.addObject("percentFraction",new PercentFractionDirective());
        modelAndView.addObject("percentInteger",new PercentIntegerDirective());
        modelAndView.addObject("amount",new AmountDirective());
        modelAndView.addObject("loan",dto.getData());
        return modelAndView;
    }

    @RequestMapping(value = "/{loanId:^\\d+$}/invests", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> getInvestList(@PathVariable long loanId,
                                 @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                 @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return loanService.getInvests(loanId, index, pageSize);
    }

}
