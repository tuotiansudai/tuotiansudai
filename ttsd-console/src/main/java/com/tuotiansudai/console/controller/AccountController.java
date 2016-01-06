package com.tuotiansudai.console.controller;

import com.tuotiansudai.service.*;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @RequestMapping(value = "/{loginName}",method = RequestMethod.GET)
    @ResponseBody
    public String account(@PathVariable String loginName) {
        return AmountConverter.convertCentToString(accountService.getBalance(loginName));
    }
}
