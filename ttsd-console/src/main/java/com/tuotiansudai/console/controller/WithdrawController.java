package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ConsoleWithdrawService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.WithdrawPaginationItemDataDto;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


@Controller
@RequestMapping(value = "/finance-manage", method = RequestMethod.GET)
public class WithdrawController {

    private final ConsoleWithdrawService consoleWithdrawService;

    @Autowired
    public WithdrawController(ConsoleWithdrawService consoleWithdrawService) {
        this.consoleWithdrawService = consoleWithdrawService;
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public ModelAndView getWithdrawList(@RequestParam(value = "withdrawId", required = false) Long withdrawId,
                                        @RequestParam(value = "mobile", required = false) String mobile,
                                        @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                        @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                                        @RequestParam(value = "status", required = false) WithdrawStatus status,
                                        @RequestParam(value = "source", required = false) Source source,
                                        @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/withdraw");
        BaseDto<BasePaginationDataDto<WithdrawPaginationItemDataDto>> baseDto = consoleWithdrawService.findWithdrawPagination(withdrawId, mobile, status, source, index, startTime, endTime);

        long sumAmount = consoleWithdrawService.findSumWithdrawAmount(withdrawId, mobile, status, source, startTime, endTime);

        long sumFee = consoleWithdrawService.findSumWithdrawFee(withdrawId, mobile, status, source, startTime, endTime);

        modelAndView.addObject("baseDto", baseDto);
        modelAndView.addObject("sumAmount", sumAmount);
        modelAndView.addObject("sumFee", sumFee);
        modelAndView.addObject("withdrawStatusList", Lists.newArrayList(WithdrawStatus.values()));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", 10);
        modelAndView.addObject("withdrawId", withdrawId);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("status", status);
        modelAndView.addObject("source", source);
        modelAndView.addObject("withdrawSourceList", Source.values());
        if (status != null) {
            modelAndView.addObject("withdrawStatus", status);
        }
        return modelAndView;

    }
}
