package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.request.UmpRechargeRequestDto;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.service.UmpRechargeService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ump/recharge")
public class UmpRechargeController {

    private final UmpRechargeService umpRechargeService;

    @Autowired
    public UmpRechargeController(UmpRechargeService umpRechargeService){
        this.umpRechargeService = umpRechargeService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView recharge(UmpRechargeRequestDto dto){
        dto.setLoginName(LoginUserInfo.getLoginName());
        UmpAsyncMessage message = umpRechargeService.recharge(dto);
        return new ModelAndView("/pay", "pay", message);
    }
}
