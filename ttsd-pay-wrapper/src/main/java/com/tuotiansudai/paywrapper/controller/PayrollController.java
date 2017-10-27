package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.paywrapper.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @RequestMapping(path = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> pay(@Valid @RequestBody long payrollId) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        try {
            payrollService.pay(payrollId);
            dataDto.setStatus(true);
        } catch (Exception e) {
            dataDto.setStatus(false);
            dataDto.setMessage(e.getMessage());
        }
        return baseDto;
    }
}
