package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import com.tuotiansudai.service.SystemBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


@Controller
@RequestMapping(value = "/finance-manage")
public class SystemBillController {

    @Autowired
    SystemBillService systemBillService;

    @RequestMapping(value = "/system-bill", method = RequestMethod.GET)
    public ModelAndView getSystemBillList(@RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                          @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                          @RequestParam(value = "operationType", required = false) SystemBillOperationType operationType,
                                          @RequestParam(value = "businessType", required = false) SystemBillBusinessType businessType,
                                          @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        ModelAndView modelAndView = new ModelAndView("/system-bill");
        BaseDto<BasePaginationDataDto> baseDto = systemBillService.findSystemBillPagination(
                startTime,
                endTime,
                operationType,
                businessType,
                index,
                pageSize);

        long sumIncome = systemBillService.findSumSystemIncome(
                startTime,
                endTime,
                operationType,
                businessType);

        long sumExpend = systemBillService.findSumSystemExpend(
                startTime,
                endTime,
                operationType,
                businessType);

        long sumWin = sumIncome - sumExpend;

        modelAndView.addObject("baseDto", baseDto);
        modelAndView.addObject("sumIncome", sumIncome);
        modelAndView.addObject("sumExpend", sumExpend);
        modelAndView.addObject("sumWin", sumWin);
        modelAndView.addObject("systemBillOperationTypeList", Lists.newArrayList(SystemBillOperationType.values()));
        modelAndView.addObject("systemBillBusinessTypeList", Lists.newArrayList(SystemBillBusinessType.values()));
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("businessType", businessType);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        return modelAndView;
    }
}
