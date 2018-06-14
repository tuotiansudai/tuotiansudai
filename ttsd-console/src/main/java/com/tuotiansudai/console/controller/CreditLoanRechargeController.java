package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.CreditLoanRechargeService;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/finance-manage")
public class CreditLoanRechargeController {

    @Autowired
    private CreditLoanRechargeService creditLoanRechargeService;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @RequestMapping(value = "/credit-loan-recharge",method = RequestMethod.GET)
    public ModelAndView creditLoanRecharge() {
        return new ModelAndView("/credit-loan-recharge");
    }


    @RequestMapping(value = "/credit-loan-recharge",method = RequestMethod.POST)
    public ModelAndView creditLoanRecharge(@Valid @ModelAttribute CreditLoanRechargeDto creditLoanRechargeDto) {
        ModelAndView modelAndView = new ModelAndView("/credit-loan-recharge", "responsive", true);
        return modelAndView;
    }
}
