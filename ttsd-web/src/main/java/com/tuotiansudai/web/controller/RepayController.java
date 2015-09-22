package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.service.RepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/repay")
public class RepayController {

    @Autowired
    private RepayService repayService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView repay() {
        return new ModelAndView("/repay");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView repay(@Valid @ModelAttribute RepayDto repayDto) {
        BaseDto<PayFormDataDto> baseDto = repayService.repay(repayDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
