package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.InvestTransferPurchaseService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/invest-transfer")
public class InvestTransferController {

    static Logger logger = Logger.getLogger(InvestTransferController.class);

    @Autowired
    private InvestTransferPurchaseService investTransferService;

    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> invest(@Valid @RequestBody InvestDto investDto) {
        return investTransferService.purchase(investDto);
    }
}
