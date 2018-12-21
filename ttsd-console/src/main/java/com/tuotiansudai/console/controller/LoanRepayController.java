package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.ConsoleLoanRepayService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanRepayDataItemDto;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.util.CalculateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/project-manage")
public class LoanRepayController {
    @Autowired
    private ConsoleLoanRepayService consoleLoanRepayService;

    @RequestMapping(value = "/loan-repay", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView findLoanRepayPagination(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                @RequestParam(value = "loanId", required = false) Long loanId,
                                                @RequestParam(value = "loginName", required = false) String loginName,
                                                @RequestParam(value = "repayStatus", required = false) RepayStatus repayStatus,
                                                @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/loan-repay");
        BaseDto<BasePaginationDataDto<LoanRepayDataItemDto>> baseDto = consoleLoanRepayService.findLoanRepayPagination(index, pageSize,
                loanId, loginName,
                startTime,
                endTime,
                repayStatus);
        List<RepayStatus> repayStatusList = Lists.newArrayList(RepayStatus.ALL, RepayStatus.COMPLETE, RepayStatus.REPAYING, RepayStatus.WAIT_PAY);
        modelAndView.addObject("baseDto", baseDto);
        modelAndView.addObject("repayStatusList", repayStatusList);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        if (repayStatus != null) {
            modelAndView.addObject("repayStatus", repayStatus);
        }
        return modelAndView;
    }


}
