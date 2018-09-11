package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.SystemBillService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.SystemBillPaginationItemDataDto;
import com.tuotiansudai.enums.BillOperationType;
import com.tuotiansudai.enums.SystemBillBusinessType;
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
    private SystemBillService systemBillService;

    @RequestMapping(value = "/system-bill", method = RequestMethod.GET)
    public ModelAndView getSystemBillList(@RequestParam(value = "isBankPlatform", defaultValue = "true") Boolean isBankPlatform,
                                          @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                          @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                          @RequestParam(value = "operationType", required = false) BillOperationType operationType,
                                          @RequestParam(value = "businessType", required = false) SystemBillBusinessType businessType,
                                          @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/system-bill");
        BaseDto<BasePaginationDataDto<SystemBillPaginationItemDataDto>> baseDto = systemBillService.findSystemBillPagination(isBankPlatform,
                startTime,
                endTime,
                operationType,
                businessType,
                index,
                pageSize);

        long sumIncome = systemBillService.findSumSystemBill(isBankPlatform,
                startTime,
                endTime,
                BillOperationType.IN,
                businessType);

        long sumExpend = systemBillService.findSumSystemBill(isBankPlatform,
                startTime,
                endTime,
                BillOperationType.OUT,
                businessType);

        long sumWin = sumIncome - sumExpend;

        modelAndView.addObject("baseDto", baseDto);
        modelAndView.addObject("sumIncome", sumIncome);
        modelAndView.addObject("sumExpend", sumExpend);
        modelAndView.addObject("sumWin", sumWin);
        modelAndView.addObject("systemBillOperationTypeList", BillOperationType.values());
        modelAndView.addObject("systemBillBusinessTypeList", SystemBillBusinessType.values());
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("businessType", businessType);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("isBankPlatform", isBankPlatform);
        return modelAndView;
    }
}
