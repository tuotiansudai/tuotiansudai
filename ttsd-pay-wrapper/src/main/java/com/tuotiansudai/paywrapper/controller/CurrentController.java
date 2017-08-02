package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.current.dto.InterestSettlementRequestDto;
import com.tuotiansudai.current.dto.DepositDto;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.paywrapper.current.CurrentDepositService;
import com.tuotiansudai.paywrapper.current.CurrentInterestSettlementService;
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
    private final CurrentInterestSettlementService currentInterestSettlementService;

    @Autowired
    public CurrentController(CurrentDepositService currentDepositService, CurrentInterestSettlementService currentInterestSettlementService) {
        this.currentDepositService = currentDepositService;
        this.currentInterestSettlementService = currentInterestSettlementService;
    }

    @RequestMapping(path = "/deposit-with-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> deposit(@Valid @RequestBody DepositDto depositRequestDto) {
        return currentDepositService.deposit(depositRequestDto);
    }

    @RequestMapping(value = "/deposit-with-no-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> noPasswordDeposit(@Valid @RequestBody DepositDto depositRequestDto) {
        return currentDepositService.noPasswordDeposit(depositRequestDto);
    }

    @RequestMapping(value = "/interest-settlement", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> interestSettlement(@Valid @RequestBody InterestSettlementRequestDto interestSettlementRequestDto) {
        return currentInterestSettlementService.InterestSettlement(interestSettlementRequestDto);
    }

    @RequestMapping(value = "/over-deposit", method = RequestMethod.POST)
    public void overDeposit(@Valid @RequestBody DepositDto depositRequestDto) {
        currentDepositService.overDeposit(depositRequestDto);
    }

}
