package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
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

    private final RepayService repayService;

    @Autowired
    public RepayController(RepayService repayService) {
        this.repayService = repayService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView repay(@Valid @ModelAttribute RepayDto repayDto) {
        BankAsyncMessage bankAsyncMessage = repayDto.isAdvanced() ? repayService.advancedRepay(repayDto) : repayService.normalRepay(repayDto);
        return new ModelAndView("/pay", "pay", bankAsyncMessage);
    }
}
