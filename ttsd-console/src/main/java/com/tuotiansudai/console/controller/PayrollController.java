package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.dto.PayrollDataDto;
import com.tuotiansudai.console.service.ConsolePayrollService;
import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping({"/finance-manage/payroll-manage"})
public class PayrollController {
    static Logger logger = Logger.getLogger(PayrollController.class);
    @Autowired
    private ConsolePayrollService consolePayrollService;

    @RequestMapping(value = "/create", method = {RequestMethod.GET})
    public ModelAndView payroll() {
        ModelAndView modelAndView = new ModelAndView("/payroll");
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{id}", method = {RequestMethod.GET})
    public ModelAndView editPayrollView(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/payroll-edit");
        PayrollModel payrollModel = consolePayrollService.findById(id);
        List<PayrollDetailModel> payrollDetailModels = consolePayrollService.findByPayrollId(id);
        modelAndView.addObject("payrollModel",payrollModel);
        modelAndView.addObject("payrollDetailModels",payrollDetailModels);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView editPayroll(@ModelAttribute PayrollDataDto payrollDataDto) {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView();
        consolePayrollService.updatePayroll(loginName, payrollDataDto);
        modelAndView.setViewName("redirect:/finance-manage/payroll-manage/list");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createPayroll(@ModelAttribute PayrollDataDto payrollDataDto) {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView();
        consolePayrollService.createPayroll(loginName, payrollDataDto);
        modelAndView.setViewName("redirect:/finance-manage/payroll-manage/list");
        return modelAndView;
    }

    @RequestMapping(value = "/import-csv", method = {RequestMethod.POST})
    @ResponseBody
    public PayrollDataDto importPayrollUserList(HttpServletRequest httpServletRequest) throws Exception {
        return this.consolePayrollService.importPayrollUserList(httpServletRequest);
    }
}
