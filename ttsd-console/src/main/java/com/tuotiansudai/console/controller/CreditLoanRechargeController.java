package com.tuotiansudai.console.controller;


import com.tuotiansudai.console.service.SystemRechargeService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.spring.LoginUserInfo;
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
    private SystemRechargeService systemRechargeService;

    @RequestMapping(value = "/credit-loan-recharge",method = RequestMethod.GET)
    public ModelAndView systemRecharge() {
        return new ModelAndView("/credit-loan-recharge");
    }


    @RequestMapping(value = "/credit-loan-recharge",method = RequestMethod.POST)
    public ModelAndView systemRecharge(@Valid @ModelAttribute SystemRechargeDto systemRechargeDto) {
        String operatorLoginName = LoginUserInfo.getLoginName();
        systemRechargeDto.setOperatorLoginName(operatorLoginName);
        BaseDto<PayFormDataDto> baseDto = systemRechargeService.systemRecharge(systemRechargeDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
