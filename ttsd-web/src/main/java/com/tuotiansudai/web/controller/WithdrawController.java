package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView withdraw() {
        return new ModelAndView("/withdraw", "balance", accountService.getBalance(LoginUserInfo.getLoginName()) / 100D);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView withdraw(@Valid @ModelAttribute WithdrawDto withdrawDto) {
        BaseDto<PayFormDataDto> baseDto = withdrawService.withdraw(withdrawDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
