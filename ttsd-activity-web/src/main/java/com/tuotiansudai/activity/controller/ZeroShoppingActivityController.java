package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.service.ZeroShoppingActivityService;
import com.tuotiansudai.service.LoanService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

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

    @ResponseBody
    @RequestMapping(value = "/activity-loan-exists", method = {RequestMethod.GET})
    public Boolean activityLoanExists(){
        return CollectionUtils.isNotEmpty(zeroShoppingActivityService.queryActivityLoan());
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET})





}
