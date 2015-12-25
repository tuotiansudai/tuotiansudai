package com.tuotiansudai.console.controller;


import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.service.SystemRechargeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/finance-manage")
public class SystemRechargeController {
    static Logger logger = Logger.getLogger(SystemRechargeController.class);
    @Autowired
    private SystemRechargeService systemRechargeService;

    @RequestMapping(value = "/system-recharge",method = RequestMethod.GET)
    public ModelAndView systemRecharge() {
        return new ModelAndView("/system-recharge");
    }


    @RequestMapping(value = "/system-recharge",method = RequestMethod.POST)
    public ModelAndView systemRecharge(@Valid @ModelAttribute SystemRechargeDto systemRechargeDto) {
        logger.debug("=======================BEGIN1=========" + systemRechargeDto.getLoginName());
        logger.debug("=======================BEGIN2=========" + systemRechargeDto.getAmount());
        logger.debug("=======================BEGIN3=========" + systemRechargeDto.getOperatorLoginName());
        String operatorLoginName = LoginUserInfo.getLoginName();
        systemRechargeDto.setOperatorLoginName(operatorLoginName);
        logger.debug("=======================BEGIN4=========");
        BaseDto<PayFormDataDto> baseDto = systemRechargeService.systemRecharge(systemRechargeDto);
        logger.debug("=======================BEGIN5========="+ baseDto.getData().getUrl());
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
