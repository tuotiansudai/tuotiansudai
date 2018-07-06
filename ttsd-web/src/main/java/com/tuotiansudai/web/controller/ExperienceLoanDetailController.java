package com.tuotiansudai.web.controller;


import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.service.ExperienceLoanDetailService;
import com.tuotiansudai.service.RiskEstimateService;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


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
    private BankAccountService bankAccountService;

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public ModelAndView getLoanDetail() {
        return new ModelAndView("/error/404");
    }

}
