package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.paywrapper.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> withdraw(@Valid @RequestBody WithdrawDto dto) {
        return withdrawService.withdraw(dto);
    }
}
