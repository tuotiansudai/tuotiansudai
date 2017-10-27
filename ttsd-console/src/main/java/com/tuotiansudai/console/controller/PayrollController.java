package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsolePayrollService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/finance-manage/payroll-manage")
public class PayrollController {
    private static Logger logger = Logger.getLogger(PayrollController.class);

    @Autowired
    private ConsolePayrollService consolePayrollService;

    @RequestMapping(value = "/primary-audit/{payRollId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView primaryAudit(@PathVariable long payRollId) {
        consolePayrollService.primaryAudit(payRollId);
        return null;
    }


}
