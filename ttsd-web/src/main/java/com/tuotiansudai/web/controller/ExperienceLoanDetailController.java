package com.tuotiansudai.web.controller;


import com.tuotiansudai.repository.model.ExperienceLoanDto;
import com.tuotiansudai.service.*;
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
    private RiskEstimateService riskEstimateService;

    @Autowired
    private ExperienceLoanDetailService experienceLoanDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public ModelAndView getLoanDetail() {
        ExperienceLoanDto experienceLoanDto = experienceLoanDetailService.findExperienceLoanDtoDetail(1, LoginUserInfo.getLoginName());
        ModelAndView modelAndView = new ModelAndView("/experience-loan", "responsive", true);
        modelAndView.addObject("loan", experienceLoanDto);
        modelAndView.addObject("experienceBalance", userService.getExperienceBalanceByLoginName(LoginUserInfo.getLoginName()));
        modelAndView.addObject("isAccount", accountService.findByLoginName(LoginUserInfo.getLoginName()) == null ? "false" : "true");
        modelAndView.addObject("estimate", riskEstimateService.getEstimate(LoginUserInfo.getLoginName()) != null);
        return modelAndView;
    }

}
