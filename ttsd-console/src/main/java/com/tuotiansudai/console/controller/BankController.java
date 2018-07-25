package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.service.ConsoleBankService;
import com.tuotiansudai.dto.BankDto;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.service.BankService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/finance-manage")
public class BankController {

    @Autowired
    private BankService bankService;

    @Autowired
    private ConsoleBankService consoleBankService;

    @RequestMapping(value = "/bank-list")
    public ModelAndView bannerList(@RequestParam(value = "isBank", defaultValue = "true") Boolean isBank) {

        ModelAndView modelAndView = null;
        List<BankDto> bankModelList = null;
        bankModelList = isBank ? bankService.findBankList(null, null) : bankService.findUmpBankList(null, null);
        modelAndView = isBank ? new ModelAndView("/bank-list") : new ModelAndView("/ump-bank-list");
        modelAndView.addObject("bankList", bankModelList);
        return modelAndView;
    }

    @RequestMapping(value = "/bank/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editBank(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/bank-edit");
        BankModel bankModel = this.bankService.findById(id);
        modelAndView.addObject("bank", bankModel);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editBank(@ModelAttribute BankDto bankDto) {
        String loginName = LoginUserInfo.getLoginName();
        BankModel bankModel = new BankModel(bankDto);
        bankModel.setUpdatedBy(loginName);
        bankModel.setUpdatedTime(new Date());
        consoleBankService.updateBank(bankModel);
        String redirectUrl = "redirect:/finance-manage/bank-list?isBank=" + bankDto.getIsBank();
        return redirectUrl;
    }
}
