package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.dto.ExperienceBalancePaginationItemDto;
import com.tuotiansudai.console.dto.ExperienceBillPaginationItemDto;
import com.tuotiansudai.console.dto.InvestRepayExperiencePaginationItemDto;
import com.tuotiansudai.console.service.ConsoleExperienceService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.InterestCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


@Controller
@RequestMapping(value = "/experience-manage")
public class ExperienceController {
    @Autowired
    private ConsoleExperienceService consoleExperienceService;

    @Autowired
    private LoanService loanService;

    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public ModelAndView balance(@RequestParam(value = "mobile", required = false) String mobile,
                                @RequestParam(value = "balanceMin", required = false) String balanceMin,
                                @RequestParam(value = "balanceMax", required = false) String balanceMax,
                                @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/experience-balance");
        int pageSize = 10;
        BasePaginationDataDto<ExperienceBalancePaginationItemDto> baseDto = consoleExperienceService.balance(mobile, balanceMin, balanceMax, index, pageSize);
        long sumExperienceBalance = consoleExperienceService.sumExperienceBalance(mobile, balanceMin, balanceMax);
        modelAndView.addObject("data", baseDto);
        modelAndView.addObject("sumExperienceBalance", sumExperienceBalance);
        modelAndView.addObject("discountCost", InterestCalculator.estimateExperienceExpectedInterest(sumExperienceBalance, loanService.findLoanById(1)));

        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("balanceMin", balanceMin);
        modelAndView.addObject("balanceMax", balanceMax);
        return modelAndView;
    }

    @RequestMapping(value = "/repay-detail", method = RequestMethod.GET)
    public ModelAndView repayDetail(@RequestParam(value = "mobile", required = false) String mobile,
                                    @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                    @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                    @RequestParam(value = "repayStatus", required = false) RepayStatus repayStatus,
                                    @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/experience-repay-detail");
        int pageSize = 10;
        BasePaginationDataDto<InvestRepayExperiencePaginationItemDto> basePaginationDataDto
                = consoleExperienceService.investRepayExperience(mobile, startTime, endTime, repayStatus, index, pageSize);
        long sumActualInterestExperience = consoleExperienceService.findSumActualInterestExperience(mobile, startTime, endTime, repayStatus);
        long sumExpectedInterestExperience = consoleExperienceService.findSumExpectedInterestExperience(mobile, startTime, endTime, repayStatus);
        modelAndView.addObject("data", basePaginationDataDto);
        modelAndView.addObject("sumActualInterestExperience", sumActualInterestExperience);
        modelAndView.addObject("sumExpectedInterestExperience", sumExpectedInterestExperience);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("repayStatus",repayStatus);
        modelAndView.addObject("repayStatusList",Lists.newArrayList(RepayStatus.COMPLETE,RepayStatus.REPAYING));
        return modelAndView;
    }

    @RequestMapping(value = "/experience-bill", method = RequestMethod.GET)
    public ModelAndView experienceBill(@RequestParam(value = "mobile", required = false) String mobile,
                                    @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                    @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                    @RequestParam(value = "experienceBillOperationType", required = false) ExperienceBillOperationType operationType,
                                    @RequestParam(value = "experienceBusinessType", required = false) ExperienceBillBusinessType businessType,
                                    @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        ModelAndView modelAndView = new ModelAndView("/experience-bill");
        int pageSize = 10;
        BasePaginationDataDto<ExperienceBillPaginationItemDto> basePaginationDataDto = consoleExperienceService.experienceBill(mobile,
                startTime,
                endTime,
                operationType,
                businessType,
                index,
                pageSize);
        long sumInExperienceBillAmount = consoleExperienceService.findSumExperienceBillAmount(mobile,startTime,endTime,operationType == null?ExperienceBillOperationType.IN:operationType,businessType);
        long sumOutExperienceBillAmount = consoleExperienceService.findSumExperienceBillAmount(mobile,startTime,endTime,operationType == null?ExperienceBillOperationType.OUT:operationType,businessType);
        modelAndView.addObject("data", basePaginationDataDto);
        modelAndView.addObject("sumInExperienceBillAmount", sumInExperienceBillAmount);
        modelAndView.addObject("sumOutExperienceBillAmount", sumOutExperienceBillAmount);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("businessType", businessType);
        modelAndView.addObject("operationTypeList",Lists.newArrayList(ExperienceBillOperationType.values()));
        modelAndView.addObject("businessTypeList",Lists.newArrayList(ExperienceBillBusinessType.values()));
        return modelAndView;
    }


}
