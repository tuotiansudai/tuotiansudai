package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.PayBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class PayBackController {

    @Autowired
    private PayBackService payBackService;

    @RequestMapping(value = "/payBack", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> payBack(@Valid @RequestBody InvestDto dto) {
        return payBackService.payBack(dto);
    }

}
