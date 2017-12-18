package com.tuotiansudai.web.controller;


import com.tuotiansudai.repository.model.ExperienceLoanDto;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.ExperienceLoanDetailService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


@Controller
@RequestMapping(value = "/loan")
public class ExperienceLoanDetailController {

    @Autowired
    private ExperienceLoanDetailService experienceLoanDetailService;

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private InvestService investService;

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public ModelAndView getLoanDetail() {
        ExperienceLoanDto experienceLoanDto = experienceLoanDetailService.findExperienceLoanDtoDetail(1, LoginUserInfo.getLoginName());
        ModelAndView modelAndView = new ModelAndView("/experience-loan", "responsive", true);
        modelAndView.addObject("loan", experienceLoanDto);
        modelAndView.addObject("experienceBalance", userService.getExperienceBalanceByLoginName(LoginUserInfo.getLoginName()));
        modelAndView.addObject("isAccount", accountService.findByLoginName(LoginUserInfo.getLoginName()) == null ? "false" : "true");
        return modelAndView;
    }

}
