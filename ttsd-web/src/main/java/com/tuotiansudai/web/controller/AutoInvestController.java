package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.AutoInvestPlanDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import com.tuotiansudai.util.RequestIPParser;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//@Controller
//@RequestMapping(path = "/auto-invest")
public class AutoInvestController {

    @Autowired
    private InvestService investService;

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String autoInvest() {
        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        if (accountModel != null && accountModel.isAutoInvest()) {
            AutoInvestPlanModel autoInvestPlan = investService.findAutoInvestPlan(loginName);
            if (autoInvestPlan == null || (!autoInvestPlan.isEnabled())) {
                return "redirect:/auto-invest/plan";
            } else {
                return "redirect:/auto-invest/plan-detail";
            }
        } else {
            return "redirect:/auto-invest/agreement";
        }
    }

    @RequestMapping(value = "/agreement", method = RequestMethod.GET)
    private ModelAndView autoInvestAgreement() {
        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        if (accountModel == null || !accountModel.isAutoInvest()) {
            return new ModelAndView("/auto-invest-agreement");
        } else {
            return new ModelAndView("redirect:/auto-invest");
        }
    }

    @RequestMapping(value = "/plan", method = RequestMethod.GET)
    public ModelAndView autoInvestPlan() {
        AutoInvestPlanModel model = investService.findAutoInvestPlan(LoginUserInfo.getLoginName());
        ModelAndView mv = new ModelAndView("/auto-invest-plan");
        if (model != null) {
            AutoInvestPlanDto dto = new AutoInvestPlanDto(model);
            mv.addObject("model", dto);
        }
        mv.addObject("periods", AutoInvestMonthPeriod.AllPeriods);
        return mv;
    }

    @RequestMapping(value = "/plan-detail", method = RequestMethod.GET)
    public ModelAndView autoInvestPlanDetail() {
        AutoInvestPlanModel model = investService.findAutoInvestPlan(LoginUserInfo.getLoginName());
        if (model != null && model.isEnabled()) {
            ModelAndView mv = new ModelAndView("/auto-invest-plan-detail");
            AutoInvestPlanDto dto = new AutoInvestPlanDto(model);
            mv.addObject("model", dto);
            mv.addObject("periods", AutoInvestMonthPeriod.split(model.getAutoInvestPeriods()));
            return mv;
        } else {
            return new ModelAndView("redirect:/auto-invest");
        }
    }

    @RequestMapping(value = "/turn-on", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> turnOnAutoInvestPlan(@RequestBody AutoInvestPlanDto autoInvestPlanDto, HttpServletRequest request) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(investService.turnOnAutoInvest(LoginUserInfo.getLoginName(), autoInvestPlanDto, RequestIPParser.parse(request)));
        return baseDto;
    }

    @RequestMapping(value = "/turn-off", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto turnOffAutoInvestPlan(HttpServletRequest request) {
        String ip = RequestIPParser.parse(request);
        investService.turnOffAutoInvest(LoginUserInfo.getLoginName(), ip);
        return new BaseDto();
    }
}
