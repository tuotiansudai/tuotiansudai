package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeConfigModel;
import com.tuotiansudai.activity.service.ZeroShoppingActivityService;
import com.tuotiansudai.repository.model.LoanModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/activity/zero-shopping")
public class ZeroShoppingActivityController {

    @Autowired
    private ZeroShoppingActivityService zeroShoppingActivityService;

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView zeroShopping(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/purchas-zero", "responsive", true);
        List<ZeroShoppingPrizeConfigModel> list = zeroShoppingActivityService.getAllPrize();
        for (ZeroShoppingPrizeConfigModel zeroShoppingPrizeConfigModel: list) {
            modelAndView.addObject(zeroShoppingPrizeConfigModel.getPrize().name(), zeroShoppingPrizeConfigModel.getPrizeSurplus());
        }
        modelAndView.addObject("appVersion", request.getHeader("appversion"));
        return modelAndView;
    }

    @RequestMapping(value = "/article", method = {RequestMethod.GET})
    public ModelAndView zeroShoppingDetail(ZeroShoppingPrize zeroShoppingPrize, String appVersion){
        ModelAndView modelAndView = new ModelAndView("/activities/2017/purchas-zero-article", "responsive", true);
        modelAndView.addObject("prizeName", zeroShoppingPrize);
        LoanModel loanModel = zeroShoppingActivityService.queryActivityLoan();
        modelAndView.addObject("exists", loanModel != null);
        modelAndView.addObject("loanId", loanModel == null ? null : String.valueOf(loanModel.getId()));
        modelAndView.addObject("appVersion", appVersion);
        return modelAndView;
    }

    @RequestMapping(value = "/activity-loan-detail", method = {RequestMethod.GET})
    public ModelAndView redirectLoanDetail(@RequestParam(value = "zeroShoppingPrize", required = false) ZeroShoppingPrize zeroShoppingPrize,
                                           RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("zeroShoppingPrize", zeroShoppingPrize);
        return new ModelAndView("redirect:/loan/" + zeroShoppingActivityService.queryActivityLoan().getId());
    }
}
