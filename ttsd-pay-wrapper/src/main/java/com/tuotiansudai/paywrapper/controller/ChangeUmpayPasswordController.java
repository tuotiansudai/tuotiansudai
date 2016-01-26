package com.tuotiansudai.paywrapper.controller;

import com.tuotiansudai.dto.AccountDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.paywrapper.service.ChangeUmpayPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class ChangeUmpayPasswordController extends BaseController{

    @Autowired
    private ChangeUmpayPasswordService changeUmpayPasswordService;

    @RequestMapping(value = "/change-umpay-password", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto changeUmpayPassword(@Valid @RequestBody AccountDto accountDto) {
        return changeUmpayPasswordService.changeUmpayPassword(accountDto);
    }
}
