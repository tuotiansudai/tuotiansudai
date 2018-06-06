package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.paywrapper.service.SystemRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class SystemRechargeController {

    @Autowired
    private SystemRechargeService systemRechargeService;


    @RequestMapping(value = "/system-recharge", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> systemRecharge(@Valid @RequestBody SystemRechargeDto dto) {
        return systemRechargeService.systemRecharge(dto);
    }
}
