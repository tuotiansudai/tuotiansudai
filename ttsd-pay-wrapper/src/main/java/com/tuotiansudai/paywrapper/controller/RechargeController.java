package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.paywrapper.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;


    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> recharge(@Valid @RequestBody RechargeDto dto) {
        return rechargeService.recharge(dto);
    }
}
