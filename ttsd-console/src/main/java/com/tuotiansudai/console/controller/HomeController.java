package com.tuotiansudai.console.controller;

import com.tuotiansudai.service.ConsoleHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    ConsoleHomeService consoleHomeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {

        ModelAndView mav = new ModelAndView("/index");

        mav.addObject("userToday", consoleHomeService.getRegisterUserToday());
        mav.addObject("user7Days", consoleHomeService.getRegisterUser7Days());
        mav.addObject("user30Days", consoleHomeService.getRegisterUser30Days());

        mav.addObject("rechargeToday", consoleHomeService.getSumRechargeAmountToday());
        mav.addObject("recharge7Days", consoleHomeService.getSumRechargeAmount7Days());
        mav.addObject("recharge30Days", consoleHomeService.getSumRechargeAmount30Days());

        mav.addObject("withdrawToday", consoleHomeService.getSumWithdrawAmountToday());
        mav.addObject("withdraw7Days", consoleHomeService.getSumWithdrawAmount7Days());
        mav.addObject("withdraw30Days", consoleHomeService.getSumWithdrawAmount30Days());

        mav.addObject("investToday", consoleHomeService.getSumInvestAmountToday());
        mav.addObject("invest7Days", consoleHomeService.getSumInvestAmount7Days());
        mav.addObject("invest30Days", consoleHomeService.getSumInvestAmount30Days());

        mav.addObject("totalInvest", consoleHomeService.getSumInvestAmount());

        return mav;
    }
}
