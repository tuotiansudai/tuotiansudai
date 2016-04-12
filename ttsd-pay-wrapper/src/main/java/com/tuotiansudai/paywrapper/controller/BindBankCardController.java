package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.BindBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


@Controller
public class BindBankCardController {
    @Autowired
    private BindBankCardService bindBankCardService;

    @RequestMapping(value = "/bind-card", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> bindBankCard(@Valid @RequestBody BindBankCardDto dto) {

        return bindBankCardService.bindBankCard(dto);
    }

    @RequestMapping(value = "/bind-card/replace", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> replaceBankCard(@Valid @RequestBody BindBankCardDto dto) {
        return bindBankCardService.replaceBankCard(dto);
    }
}
