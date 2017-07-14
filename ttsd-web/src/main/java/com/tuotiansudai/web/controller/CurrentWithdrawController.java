package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.CurrentWithdrawDto;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/current-withdraw")
public class CurrentWithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView withdraw() {
        ModelAndView modelAndView = new ModelAndView("/current-withdraw");
        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView withdraw(@Valid @ModelAttribute CurrentWithdrawDto currentWithdrawDto) {
        String loginName = LoginUserInfo.getLoginName();
        //调用currentClient（需要封装）中的rest服务

        //BaseDto<PayFormDataDto> baseDto = withdrawService.withdraw(withdrawDto);
        return new ModelAndView("/success", "pay",currentWithdrawDto);
    }
}
