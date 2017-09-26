package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.CreditLoanBillService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.CreditLoanBillPaginationItemDataDto;
import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.CreditLoanBillOperationType;
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
public class CreditLoanBillController {

    @Autowired
    private CreditLoanBillService creditLoanBillService;

    @RequestMapping(value = "/credit-loan-bill", method = RequestMethod.GET)
    public ModelAndView getSystemBillList(@RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime,
                                          @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
                                          @RequestParam(value = "operationType", required = false) CreditLoanBillOperationType operationType,
                                          @RequestParam(value = "businessType", required = false) CreditLoanBillBusinessType businessType,
                                          @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/credit-loan-bill");
        BaseDto<BasePaginationDataDto<CreditLoanBillPaginationItemDataDto>> baseDto = creditLoanBillService.findCreditLoanBillPagination(
                startTime,
                endTime,
                operationType,
                businessType,
                index,
                pageSize);

        long sumIncome = creditLoanBillService.findSumCreditLoanIncome(
                startTime,
                endTime,
                operationType,
                businessType);

        long sumExpend = creditLoanBillService.findSumCreditLoanExpend(
                startTime,
                endTime,
                operationType,
                businessType);

        long sumWin = sumIncome - sumExpend;

        modelAndView.addObject("baseDto", baseDto);
        modelAndView.addObject("sumIncome", sumIncome);
        modelAndView.addObject("sumExpend", sumExpend);
        modelAndView.addObject("sumWin", sumWin);
        modelAndView.addObject("creditLoanBillOperationTypeList", Lists.newArrayList(CreditLoanBillOperationType.values()));
        modelAndView.addObject("creditLoanBillBusinessTypeList", Lists.newArrayList(CreditLoanBillBusinessType.values()));
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("businessType", businessType);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        return modelAndView;
    }
}
