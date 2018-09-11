package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.dto.ExperienceBalancePaginationItemDto;
import com.tuotiansudai.console.dto.ExperienceBillPaginationItemDto;
import com.tuotiansudai.console.dto.InvestRepayExperiencePaginationItemDto;
import com.tuotiansudai.console.service.ConsoleExperienceService;
import com.tuotiansudai.console.service.ConsoleInvestService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.InvestPaginationDataDto;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.InterestCalculator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/experience-manage")
public class ExperienceController {
    @Autowired
    private ConsoleExperienceService consoleExperienceService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ConsoleInvestService consoleInvestService;

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
        modelAndView.addObject("repayStatus", repayStatus);
        modelAndView.addObject("repayStatusList", Lists.newArrayList(RepayStatus.COMPLETE, RepayStatus.REPAYING));
        return modelAndView;
    }

    @RequestMapping(value = "/experience-bill", method = RequestMethod.GET)
    public ModelAndView experienceBill(@RequestParam(value = "mobile", required = false) String mobile,
                                       @RequestParam(value = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                       @RequestParam(value = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                       @RequestParam(value = "operationType", required = false) ExperienceBillOperationType operationType,
                                       @RequestParam(value = "businessType", required = false) ExperienceBillBusinessType businessType,
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
        long sumInExperienceBillAmount = operationType == ExperienceBillOperationType.OUT ? 0l : consoleExperienceService.findSumExperienceBillAmount(mobile, startTime, endTime, ExperienceBillOperationType.IN, businessType);
        long sumOutExperienceBillAmount = operationType == ExperienceBillOperationType.IN ? 0l : consoleExperienceService.findSumExperienceBillAmount(mobile, startTime, endTime, operationType == null ? ExperienceBillOperationType.OUT : operationType, businessType);
        modelAndView.addObject("data", basePaginationDataDto);
        modelAndView.addObject("sumInExperienceBillAmount", sumInExperienceBillAmount);
        modelAndView.addObject("sumOutExperienceBillAmount", sumOutExperienceBillAmount);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);
        modelAndView.addObject("operationType", operationType);
        modelAndView.addObject("businessType", businessType);
        modelAndView.addObject("operationTypeList", Lists.newArrayList(ExperienceBillOperationType.values()));
        modelAndView.addObject("businessTypeList", Lists.newArrayList(ExperienceBillBusinessType.values()));
        return modelAndView;
    }


    @RequestMapping(value = "experience-record", method = RequestMethod.GET)
    public ModelAndView experienceInvestCount(@RequestParam(name = "loanId", required = false) Long loanId,
                                              @RequestParam(name = "mobile", required = false) String investorMobile,
                                              @RequestParam(name = "source", required = false) Source source,
                                              @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                              @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                              @Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index) {
        int pageSize = 10;
        InvestPaginationDataDto dataDto = consoleInvestService.getInvestPagination(null,loanId, investorMobile, null, source, null,
                startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate(),
                endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate(),
                null, null, ProductType.EXPERIENCE, index, pageSize);
        List<String> channelList = consoleInvestService.findAllChannel();
        ModelAndView mv = new ModelAndView("/experience-record-list");
        mv.addObject("data", dataDto);
        mv.addObject("mobile", investorMobile);
        mv.addObject("loanId", loanId);
        mv.addObject("source", source);
        mv.addObject("preferenceTypes", PreferenceType.values());
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        mv.addObject("investStatusList", InvestStatus.values());
        mv.addObject("channelList", channelList);
        mv.addObject("sourceList", Source.values());
        mv.addObject("roleList", Role.values());
        return mv;
    }

}
