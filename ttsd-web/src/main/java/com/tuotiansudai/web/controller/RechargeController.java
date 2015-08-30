package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView recharge() {
        return new ModelAndView("/recharge");
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView recharge(@Valid @ModelAttribute RechargeDto rechargeDto) {
        BaseDto<PayFormDataDto> baseDto = rechargeService.recharge(rechargeDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
