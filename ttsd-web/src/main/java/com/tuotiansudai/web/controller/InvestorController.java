package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;

@Controller
@RequestMapping(path = "/investor")
public class InvestorController {

    @Autowired
    private InvestService investService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RepayService repayService;

    @RequestMapping(value = "/invest-list", method = RequestMethod.GET)
    public ModelAndView investList() {
        return new ModelAndView("/investor-invest-list");
    }

    @RequestMapping(value = "/invest-list-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> investListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                         @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                         @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                         @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                         @RequestParam(name = "status", required = false) LoanStatus status) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<InvestorInvestPaginationItemDataDto> dataDto = investService.getInvestPagination(loginName, index, pageSize, startTime, endTime, status);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

    @RequestMapping(path = "/invest/{investId:^\\d+$}/repay-data", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<InvestRepayDataDto> getInvestRepayData(@PathVariable long investId) {
        return repayService.findInvestorInvestRepay(LoginUserInfo.getLoginName(), investId);
    }

    @RequestMapping(value = "/auto-invest", method = RequestMethod.GET)
    public String autoInvest() {
        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        if (accountModel.isAutoInvest()) {
            AutoInvestPlanModel autoInvestPlan = investService.findAutoInvestPlan(loginName);
            if (autoInvestPlan == null || (!autoInvestPlan.isEnabled())) {
                return "redirect:/investor/auto-invest/plan";
            } else {
                return "redirect:/investor/auto-invest/plan-detail";
            }
        } else {
            return "redirect:/investor/auto-invest/agreement";
        }
    }

    @RequestMapping(value = "/auto-invest/agreement", method = RequestMethod.GET)
    private ModelAndView autoInvestAgreement() {
        String loginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountService.findByLoginName(loginName);
        if (!accountModel.isAutoInvest()) {
            return new ModelAndView("/auto-invest-agreement");
        } else {
            return new ModelAndView("redirect:/investor/auto-invest");
        }
    }

    @RequestMapping(value = "/auto-invest/plan", method = RequestMethod.GET)
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

    @RequestMapping(value = "/auto-invest/plan-detail", method = RequestMethod.GET)
    public ModelAndView autoInvestPlanDetail() {
        AutoInvestPlanModel model = investService.findAutoInvestPlan(LoginUserInfo.getLoginName());
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

    @RequestMapping(value = "/auto-invest/turn-on", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto turnOnAutoInvestPlan(@RequestBody AutoInvestPlanDto autoInvestPlanDto) {
        BaseDto baseDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        dataDto.setStatus(true);

        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setLoginName(LoginUserInfo.getLoginName());
        model.setMinInvestAmount(AmountConverter.convertStringToCent(autoInvestPlanDto.getMinInvestAmount()));
        model.setMaxInvestAmount(AmountConverter.convertStringToCent(autoInvestPlanDto.getMaxInvestAmount()));
        model.setRetentionAmount(AmountConverter.convertStringToCent(autoInvestPlanDto.getRetentionAmount()));
        model.setAutoInvestPeriods(autoInvestPlanDto.getAutoInvestPeriods());
        if (model.getMaxInvestAmount() < model.getMaxInvestAmount()) {
            dataDto.setStatus(false);
        }
        investService.turnOnAutoInvest(model);
        return baseDto;
    }

    @RequestMapping(value = "/auto-invest/turn-off", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto turnOffAutoInvestPlan() {
        investService.turnOffAutoInvest(LoginUserInfo.getLoginName());
        return new BaseDto();
    }
}
