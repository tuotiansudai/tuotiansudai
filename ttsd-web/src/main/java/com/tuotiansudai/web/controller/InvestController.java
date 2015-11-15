package com.tuotiansudai.web.controller;

import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.dto.AutoInvestPlanDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import com.tuotiansudai.web.util.LoginUserInfo;
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
    private LoanController loanController;

    @RequestMapping(value = "/invest", method = RequestMethod.POST)
    public ModelAndView invest(@Valid @ModelAttribute InvestDto investDto) {
        investDto.setSource(Source.WEB);
        ModelAndView mv = null;
        String errorMessage = null;
        try {
            investDto.setLoginName(LoginUserInfo.getLoginName());
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
        return "redirect:/investor/auto-invest/agreement";
    }

    @RequestMapping(value = "/investor/auto-invest/agreement", method = RequestMethod.GET)
    private ModelAndView autoInvestAgreement() {
        ModelAndView mv = new ModelAndView("/auto-invest-agreement");
        mv.addObject("content", "自动投标");
        return mv;
    }

    @RequestMapping(value = "/investor/auto-invest/plan", method = RequestMethod.GET)
    public ModelAndView autoInvestPlan() {
        AutoInvestPlanModel model = investService.findUserAutoInvestPlan(LoginUserInfo.getLoginName());
        ModelAndView mv = new ModelAndView("/auto-invest-plan");
        mv.addObject("model", model);
        mv.addObject("periods", AutoInvestMonthPeriod.AllPeriods);
        return mv;
    }

    @RequestMapping(value = "/investor/auto-invest/plan-detail", method = RequestMethod.GET)
    public ModelAndView autoInvestPlanDetail4Test() {
        ModelAndView mv = new ModelAndView("/auto-invest-plan-detail");
        return mv;
    }

    @RequestMapping(value = "/investor/auto-invest/turn-on", method = RequestMethod.POST)
    public String turnOnAutoInvestPlan(@RequestBody AutoInvestPlanDto autoInvestPlanDto) {
        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setLoginName(LoginUserInfo.getLoginName());
        model.setMinInvestAmount(autoInvestPlanDto.getMinInvestAmount());
        model.setMaxInvestAmount(autoInvestPlanDto.getMaxInvestAmount());
        model.setRetentionAmount(autoInvestPlanDto.getRetentionAmount());
        model.setAutoInvestPeriods(autoInvestPlanDto.getAutoInvestPeriods());
        investService.turnOnAutoInvest(model);
        return "redirect:/investor/auto-invest";
    }

    @RequestMapping(value = "/investor/auto-invest/turn-off", method = RequestMethod.POST)
    public String turnOffAutoInvestPlan() {
        investService.turnOffAutoInvest(LoginUserInfo.getLoginName());
        return "redirect:/investor/auto-invest";
    }
}
