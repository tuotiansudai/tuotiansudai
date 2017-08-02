package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.current.dto.RedeemRequestDto;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.paywrapper.current.CurrentDepositService;
import com.tuotiansudai.paywrapper.current.CurrentRedeemService;
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

    private final CurrentRedeemService currentRedeemService;

    @Autowired
    public CurrentController(CurrentDepositService currentDepositService, CurrentRedeemService currentRedeemService) {
        this.currentDepositService = currentDepositService;
        this.currentRedeemService = currentRedeemService;
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

    @RequestMapping(value = "/redeem-to-loan", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> redeemToLoan(@Valid @RequestBody RedeemRequestDto redeemRequestDto) {
        return currentRedeemService.redeemToLoan(redeemRequestDto);
    }

}
