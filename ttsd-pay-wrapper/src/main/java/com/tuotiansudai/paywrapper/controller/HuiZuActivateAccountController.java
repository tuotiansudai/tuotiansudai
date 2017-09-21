package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuActivateAccountDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.service.HuiZuActivateAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/activate-account")
public class HuiZuActivateAccountController {

    @Autowired
    private HuiZuActivateAccountService huiZuActivateAccountService;

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayFormDataDto> invest(@Valid @RequestBody HuiZuActivateAccountDto activateAccountDto) {
        return huiZuActivateAccountService.password(activateAccountDto);
    }

    @RequestMapping(value = "/no-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> noPasswordPurchase(@Valid @RequestBody HuiZuActivateAccountDto activateAccountDto) {
        return huiZuActivateAccountService.noPassword(activateAccountDto);
    }

}
