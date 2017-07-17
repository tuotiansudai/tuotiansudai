package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.CurrentRedeemDto;
import com.tuotiansudai.service.CurrentRedeemService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/current")
public class CurrentRedeemController {

    @Autowired
    private CurrentRedeemService currentRedeemService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView withdraw() {
        ModelAndView modelAndView = new ModelAndView("/withdraw");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void redeem(@ModelAttribute CurrentRedeemDto currentRedeemDto) {
        String loginName = LoginUserInfo.getLoginName();
        currentRedeemService.redeem(currentRedeemDto, loginName);
    }
}
