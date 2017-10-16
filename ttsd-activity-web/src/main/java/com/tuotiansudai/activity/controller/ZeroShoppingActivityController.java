package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.service.ZeroShoppingActivityService;
import com.tuotiansudai.repository.model.LoanModel;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/activity/zero-shopping")
public class ZeroShoppingActivityController {

    @Autowired
    private ZeroShoppingActivityService zeroShoppingActivityService;

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView zeroShopping(){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/zero-shopping", "responsive", true);
        modelAndView.addObject("prize", zeroShoppingActivityService.getAllPrize());
        return modelAndView;
    }

    @RequestMapping(value = "/activity-loan-exists", method = {RequestMethod.GET})
    public ModelAndView activityLoanExists(ZeroShoppingPrize zeroShoppingPrize, RedirectAttributes redirectAttributes){
        List<LoanModel> loanModels = zeroShoppingActivityService.queryActivityLoan();

        ModelAndView modelAndView = new ModelAndView();
        if(CollectionUtils.isEmpty(loanModels)){
            modelAndView.setViewName("/activities/2017/zero-shopping-detail");
            modelAndView.addObject("message", false);
            return modelAndView;
        }

        modelAndView.setViewName("redirect:/loan/" + loanModels.get(0).getId());
        redirectAttributes.addFlashAttribute("zeroShoppingPrize", zeroShoppingPrize);
        return modelAndView;
    }

}
