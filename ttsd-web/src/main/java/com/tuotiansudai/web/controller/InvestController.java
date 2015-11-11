package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.utils.AmountConverter;
import com.tuotiansudai.utils.AutoInvestMonthPeriod;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class InvestController {

    @Autowired
    private InvestService investService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanController loanController;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setSource(Source.WEB);
        ModelAndView mv = null;
        String errorMessage = null;
        try {
            BaseDto<PayFormDataDto> baseDto = investService.invest(investDto);
            if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                mv = new ModelAndView("/pay", "pay", baseDto);
            }
        } catch (InvestException e) {
            errorMessage = e.getMessage();
        }

        if (mv == null) {
            mv = loanController.getLoanDetail(investDto.getLoanIdLong());
            if(errorMessage == null){
                errorMessage = "投资失败";
            }
            mv.addObject("errorMessage", errorMessage);
            mv.addObject("investAmount", investDto.getAmount());
        }
        return mv;
    }
    @RequestMapping(value = "/calculate-expected-interest/loan/{loanId}/amount/{amount:^\\d+\\.\\d{2}$}", method = RequestMethod.GET)
    @ResponseBody
    public String calculateExpectedInterest(@PathVariable long loanId, @PathVariable String amount) {
        long expectedInterest = investService.calculateExpectedInterest(loanId, AmountConverter.convertStringToCent(amount));
        return AmountConverter.convertCentToString(expectedInterest);
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
