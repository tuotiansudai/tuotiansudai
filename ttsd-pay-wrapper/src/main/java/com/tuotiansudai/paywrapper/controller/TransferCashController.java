package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.paywrapper.service.TransferCashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class TransferCashController extends BaseController{

    @Autowired
    private TransferCashService transferCashService;

    @RequestMapping(value = "/transfer-cash", method = RequestMethod.POST)
    public BaseDto<PayDataDto> transferCash(@Valid @RequestBody TransferCashDto transferCashDto) {
        return transferCashService.transferCash(transferCashDto);
    }

}
