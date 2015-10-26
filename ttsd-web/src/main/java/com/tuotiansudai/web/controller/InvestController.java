package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.AutoInvestMonthPeriod;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class InvestController {

    @Autowired
    private InvestService investService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RepayService repayService;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setInvestSource(InvestSource.WEB);
        BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }

    @RequestMapping(value = "/calculate-expected-interest/loan/{loanId}/amount/{amount:^\\d+\\.\\d{2}$}", method = RequestMethod.GET)
    @ResponseBody
    public String calculateExpectedInterest(@PathVariable long loanId, @PathVariable String amount) {
        long expectedInterest = investService.calculateExpectedInterest(loanId, AmountUtil.convertStringToCent(amount));
        return AmountUtil.convertCentToString(expectedInterest);
    }

    @RequestMapping(value = "/investor/invests", method = RequestMethod.GET)
    public ModelAndView userInvest() {
        return new ModelAndView("/invest-record");
    }

    @RequestMapping(value = "/investor/query-invests", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> queryUserInvest(
            Long loanId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            LoanStatus loanStatus, InvestStatus investStatus,
            Integer index, Integer pageSize
    ) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<InvestDetailDto> paginationList = new BasePaginationDataDto<>(1, 1, 0, Lists.<InvestDetailDto>newArrayList());
        if (!Strings.isNullOrEmpty(loginName)) {
            InvestDetailQueryDto queryDto = buildInvestDetailQueryDto(loanId, startTime, endTime, loanStatus, investStatus, index, pageSize, loginName);
            paginationList = investService.queryInvests(queryDto, true);
            List<InvestDetailDto> dtoList = paginationList.getRecords();
            List<InvestDetailDto> jsonDtoList = new ArrayList<>(dtoList.size());
            for (InvestDetailDto dto : dtoList) {
                jsonDtoList.add(new InvestJsonDetailDto(dto));
            }
            paginationList = new BasePaginationDataDto<>(
                    paginationList.getIndex(),
                    paginationList.getPageSize(),
                    paginationList.getCount(),
                    jsonDtoList);
            paginationList.setStatus(true);
        }
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(paginationList);
        return dto;
    }

    @RequestMapping(value = "/investor/query-invest-repay", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<InvestRepayDataDto> queryUserInvestRepay(long investId) {
        BaseDto<InvestRepayDataDto> dto = repayService.findInvestorInvestRepay(investId);
        return dto;
    }

    private InvestDetailQueryDto buildInvestDetailQueryDto(Long loanId, @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime, LoanStatus loanStatus, InvestStatus investStatus, Integer index, Integer pageSize, String loginName) {
        InvestDetailQueryDto queryDto = new InvestDetailQueryDto();
        queryDto.setLoanId(loanId);
        queryDto.setLoginName(loginName);
        queryDto.setBeginTime(startTime);
        if (endTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            endTime = calendar.getTime();
        }
        queryDto.setEndTime(endTime);
        queryDto.setLoanStatus(loanStatus);
        queryDto.setInvestStatus(investStatus);
        if (index == null || index <= 0) {
            queryDto.setPageIndex(1);
        } else {
            queryDto.setPageIndex(index);
        }
        if (pageSize == null || pageSize <= 0) {
            queryDto.setPageSize(10);
        } else {
            queryDto.setPageSize(pageSize);
        }
        return queryDto;
    }

    @RequestMapping(value = "/investor/auto-invest", method = RequestMethod.GET)
    public String autoInvest() {
        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        if (accountModel.isAutoInvest()) {
            AutoInvestPlanModel autoInvestPlan = investService.findUserAutoInvestPlan(loginName);
            if (autoInvestPlan == null || (!autoInvestPlan.isEnabled())) {
                return "redirect:/investor/auto-invest/plan";
            } else {
                return "redirect:/investor/auto-invest/plan-detail";
            }
        } else {
            return "redirect:/investor/auto-invest/agreement";
        }
    }

    @RequestMapping(value = "/investor/auto-invest/agreement", method = RequestMethod.GET)
    private ModelAndView autoInvestAgreement() {
        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        if (!accountModel.isAutoInvest()) {
            return new ModelAndView("/auto-invest-agreement");
        } else {
            return new ModelAndView("redirect:/investor/auto-invest");
        }
    }

    @RequestMapping(value = "/investor/auto-invest/plan", method = RequestMethod.GET)
    public ModelAndView autoInvestPlan() {
        AutoInvestPlanModel model = investService.findUserAutoInvestPlan(LoginUserInfo.getLoginName());
        ModelAndView mv = new ModelAndView("/auto-invest-plan");
        if (model != null) {
            AutoInvestPlanDto dto = new AutoInvestPlanDto(model);
            mv.addObject("model", dto);
        }
        mv.addObject("periods", AutoInvestMonthPeriod.AllPeriods);
        return mv;
    }

    @RequestMapping(value = "/investor/auto-invest/plan-detail", method = RequestMethod.GET)
    public ModelAndView autoInvestPlanDetail() {
        AutoInvestPlanModel model = investService.findUserAutoInvestPlan(LoginUserInfo.getLoginName());
        if (model != null && model.isEnabled()) {
            ModelAndView mv = new ModelAndView("/auto-invest-plan-detail");
            AutoInvestPlanDto dto = new AutoInvestPlanDto(model);
            mv.addObject("model", dto);
            mv.addObject("periods", AutoInvestMonthPeriod.split(model.getAutoInvestPeriods()));
            return mv;
        } else {
            return new ModelAndView("redirect:/investor/auto-invest");
        }
    }

    @RequestMapping(value = "/investor/auto-invest/turn-on", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto turnOnAutoInvestPlan(@RequestBody AutoInvestPlanDto autoInvestPlanDto) {
        BaseDto baseDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(true);

        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setLoginName(LoginUserInfo.getLoginName());
        model.setMinInvestAmount(AmountUtil.convertStringToCent(autoInvestPlanDto.getMinInvestAmount()));
        model.setMaxInvestAmount(AmountUtil.convertStringToCent(autoInvestPlanDto.getMaxInvestAmount()));
        model.setRetentionAmount(AmountUtil.convertStringToCent(autoInvestPlanDto.getRetentionAmount()));
        model.setAutoInvestPeriods(autoInvestPlanDto.getAutoInvestPeriods());
        if (model.getMaxInvestAmount() < model.getMaxInvestAmount()) {
            dataDto.setStatus(false);
        }
        investService.turnOnAutoInvest(model);
        return baseDto;
    }

    @RequestMapping(value = "/investor/auto-invest/turn-off", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto turnOffAutoInvestPlan() {
        investService.turnOffAutoInvest(LoginUserInfo.getLoginName());
        return new BaseDto();
    }
}
