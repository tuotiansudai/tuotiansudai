package com.tuotiansudai.console.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.service.SystemRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/system-recharge")
public class SystemRechargeController {
    @Autowired
    private SystemRechargeService systemRechargeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView systemRecharge() {
        return new ModelAndView("/system-recharge");
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView systemRecharge(@Valid @ModelAttribute SystemRechargeDto systemRechargeDto) {
        BaseDto<PayFormDataDto> baseDto = systemRechargeService.systemRecharge(systemRechargeDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
