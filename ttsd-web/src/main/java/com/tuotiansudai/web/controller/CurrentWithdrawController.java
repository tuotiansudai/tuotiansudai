package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.CurrentWithdrawDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.CurrentWithdrawService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/current")
public class CurrentWithdrawController {

    @Autowired
    private AccountService accountService;
    @Autowired
    CurrentWithdrawService currentWithdrawService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView withdraw() {
        ModelAndView modelAndView = new ModelAndView("/withdraw");
        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.POST)
    public void withdraw(@ModelAttribute CurrentWithdrawDto currentWithdrawDto) {
        String loginName = LoginUserInfo.getLoginName();
        currentWithdrawService.currentWithdraw(currentWithdrawDto, loginName);
    }
}
