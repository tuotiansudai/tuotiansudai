package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.dto.PayrollDataDto;
import com.tuotiansudai.console.service.ConsolePayrollService;
import com.tuotiansudai.dto.BaseDataDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/payroll-manage")
public class PayrollController {

    static Logger logger = Logger.getLogger(PayrollController.class);

    @Autowired
    private ConsolePayrollService consolePayrollService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView payroll() {
        ModelAndView modelAndView = new ModelAndView("/payroll");
        return modelAndView;
    }

    @RequestMapping(value = "/import-csv", method = RequestMethod.POST)
    @ResponseBody
    public PayrollDataDto importPayrollUserList(HttpServletRequest httpServletRequest) throws Exception {
        return consolePayrollService.importPayrollUserList(httpServletRequest);
    }


}
