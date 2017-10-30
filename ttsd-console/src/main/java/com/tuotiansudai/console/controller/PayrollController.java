package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.dto.PayrollDataDto;
import com.tuotiansudai.console.service.ConsolePayrollService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping(value = "/finance-manage/payroll-manage")
public class PayrollController {
    private static Logger logger = Logger.getLogger(PayrollController.class);

    @Autowired
    private ConsolePayrollService consolePayrollService;

    @RequestMapping(value = "/primary-audit/{payRollId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView primaryAudit(@PathVariable long payRollId) {
        consolePayrollService.primaryAudit(payRollId, LoginUserInfo.getLoginName());
        return new ModelAndView("redirect:/finance-manage/payroll-manage/list");
    }

    @RequestMapping(value = "/advanced-audit/{payRollId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> advancedAudit(@PathVariable long payRollId) {
        return consolePayrollService.advancedAudit(payRollId, LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/reject/{payRollId:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView reject(@PathVariable long payRollId) {
        consolePayrollService.reject(payRollId, LoginUserInfo.getLoginName());
        return new ModelAndView("redirect:/finance-manage/payroll-manage/list");
    }


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
        modelAndView.addObject("payrollModel", payrollModel);
        modelAndView.addObject("payrollDetailModels", payrollDetailModels);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView editPayroll(@ModelAttribute PayrollDataDto payrollDataDto) {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView();
        consolePayrollService.updatePayroll(loginName, payrollDataDto);
        modelAndView.setViewName("redirect:/finance-manage/payroll-manage/payroll-list");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createPayroll(@ModelAttribute PayrollDataDto payrollDataDto) {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView();
        consolePayrollService.createPayroll(loginName, payrollDataDto);
        modelAndView.setViewName("redirect:/finance-manage/payroll-manage/payroll-list");
        return modelAndView;
    }

    @RequestMapping(value = "/import-csv", method = {RequestMethod.POST})
    @ResponseBody
    public PayrollDataDto importPayrollUserList(HttpServletRequest httpServletRequest) throws Exception {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        if (null == multipartFile) {
            return new PayrollDataDto(false, "请上传文件！");
        }
        if (!multipartFile.getOriginalFilename().endsWith(".csv")) {
            return new PayrollDataDto(false, "上传失败!文件必须是csv格式");
        }

        PayrollDataDto payrollDataDto = new PayrollDataDto();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            payrollDataDto = consolePayrollService.importPayrollUserList(inputStream);
        } catch (IOException e) {
            payrollDataDto.setStatus(false);
            payrollDataDto.setMessage("上传失败!文件内容错误");
        }
        return payrollDataDto;
    }
}
