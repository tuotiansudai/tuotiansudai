package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BankService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    @RequestMapping(value = "/list")
    public ModelAndView bannerList() {
        ModelAndView modelAndView = new ModelAndView("/bank-list");
        List<BankModel> bankModelList = bankService.findBankList();
        modelAndView.addObject("bankList", bankModelList);
        return modelAndView;
    }
}
