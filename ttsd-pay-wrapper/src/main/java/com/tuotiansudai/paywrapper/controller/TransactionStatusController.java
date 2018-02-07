package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.validation.validators.InvestStatusValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller(value = "/transaction-status")
public class TransactionStatusController {

    private final InvestStatusValidator investStatusValidator;

    @Autowired
    public TransactionStatusController(InvestStatusValidator investStatusValidator) {
        this.investStatusValidator = investStatusValidator;
    }

    @RequestMapping(value = "/invest/{investId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<PayDataDto> checkInvestStatus(@PathVariable(value = "investId") long orderId) {
        return investStatusValidator.validate(orderId);
    }
}
