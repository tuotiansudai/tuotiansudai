package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.PayrollService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollStatusType;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.CalculateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping(value = "/payroll-manage")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView payroll(@RequestParam(name = "createStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date createStartTime,
                                @RequestParam(name = "createEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date createEndTime,
                                @RequestParam(name = "sendStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sendStartTime,
                                @RequestParam(name = "sendEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date sendEndTime,
                                @RequestParam(name = "amountMin", defaultValue = "0", required = false) String amountMin,
                                @RequestParam(name = "amountMax", defaultValue = "0", required = false) String amountMax,
                                @RequestParam(name = "payrollStatusType", required = false) PayrollStatusType payrollStatusType,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/payroll");
        BasePaginationDataDto basePaginationDataDto = payrollService.list(
                createStartTime == null ? new DateTime(0).toDate() : new DateTime(createStartTime).withTimeAtStartOfDay().toDate(),
                createEndTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(createEndTime).withTimeAtStartOfDay().plusDays(1).toDate(),
                sendStartTime == null ? new DateTime(0).toDate() : new DateTime(sendStartTime).withTimeAtStartOfDay().toDate(),
                sendEndTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(sendEndTime).withTimeAtStartOfDay().plusDays(1).toDate(),
                amountMin, amountMax, payrollStatusType, title, index, pageSize);
        modelAndView.addObject("data", basePaginationDataDto);
        modelAndView.addObject("payrollStatusTypes", PayrollStatusType.values());
        modelAndView.addObject("createStartTime", createStartTime);
        modelAndView.addObject("createEndTime", createEndTime);
        modelAndView.addObject("sendStartTime", sendStartTime);
        modelAndView.addObject("sendEndTime", sendEndTime);
        modelAndView.addObject("amountMin", amountMin.equals("0") ? null : amountMin);
        modelAndView.addObject("amountMax", amountMax.equals("0") ? null : amountMax);
        modelAndView.addObject("payrollStatusType", payrollStatusType);
        modelAndView.addObject("title", title);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/update/remark", method = RequestMethod.POST)
    public PayrollModel updateRemark(@RequestBody PayrollModel payrollModel){
        payrollService.updateRemark(payrollModel.getId(), payrollModel.getRemark(), LoginUserInfo.getLoginName());
        return payrollModel;
    }

    @RequestMapping(value = "/{id:^\\d+$}/detail", method = RequestMethod.GET)
    public ModelAndView payrollDetail(@PathVariable long id,
                                      @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                      @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize){
        ModelAndView modelAndView = new ModelAndView("/payroll-detail");
        modelAndView.addObject("data", payrollService.detail(id, index, pageSize));
        modelAndView.addObject("payrollStatusTypes", PayrollStatusType.values());
        return modelAndView;
    }
}
