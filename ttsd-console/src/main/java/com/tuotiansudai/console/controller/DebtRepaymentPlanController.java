package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.DebtRepaymentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/finance-manage")
public class DebtRepaymentPlanController {

    @Autowired
    private DebtRepaymentPlanService debtRepaymentPlanService;

    @RequestMapping(value = "/debt-repayment-plan", method = RequestMethod.GET)
    public ModelAndView debtRepaymentPlan(@RequestParam(value = "repayStatus",required = false) RepayStatus repayStatus) {
        ModelAndView modelAndView = new ModelAndView("/debt-repayment-plan");
        if(repayStatus == null){
            repayStatus = RepayStatus.REPAYING;
        }
        if(repayStatus == RepayStatus.ALL){
            repayStatus = null;
        }

        modelAndView.addObject("debtRepaymentPlans", this.debtRepaymentPlanService.findDebtRepaymentPlan(repayStatus));
        modelAndView.addObject("repayStatus",repayStatus);
        return modelAndView;
    }

    @RequestMapping(value = "/debt-repayment-detail", method = RequestMethod.GET)
    public ModelAndView debtRepaymentPlanDetail(@RequestParam(value = "repayStatus",required = false) RepayStatus repayStatus,@RequestParam("date") String date) {
        ModelAndView modelAndView = new ModelAndView("/debt-repayment-detail");
        if(repayStatus == null){
            repayStatus = RepayStatus.REPAYING;
        }
        if(repayStatus == RepayStatus.ALL){
            repayStatus = null;
        }
        modelAndView.addObject("debtRepaymentPlanDetails",this.debtRepaymentPlanService.findDebtRepaymentPlanDetail(repayStatus,date));
        modelAndView.addObject("repayStatus",repayStatus);
        modelAndView.addObject("date",date);
        return modelAndView;
    }

}
