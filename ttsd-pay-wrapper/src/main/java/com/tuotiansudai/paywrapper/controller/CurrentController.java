package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.paywrapper.current.CurrentDepositService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/current")
public class CurrentController {

    private final static Logger logger = Logger.getLogger(CurrentController.class);

    private final CurrentDepositService currentDepositService;

    @Autowired
    public CurrentController(CurrentDepositService currentDepositService) {
        this.currentDepositService = currentDepositService;
    }

    @RequestMapping(path = "/deposit-with-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> deposit(@Valid @RequestBody DepositRequestDto depositRequestDto) {
        return currentDepositService.deposit(depositRequestDto);
    }

    @RequestMapping(value = "/deposit-with-no-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> noPasswordDeposit(@Valid @RequestBody DepositRequestDto depositRequestDto) {
        return currentDepositService.noPasswordDeposit(depositRequestDto);
    }

    @RequestMapping(value = "/over-deposit", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> overDeposit(@Valid @RequestBody DepositRequestDto depositRequestDto) {
        return currentDepositService.overDeposit(depositRequestDto);
    }

}
