package com.tuotiansudai.console.controller;


import com.tuotiansudai.console.service.CreditLoanRechargeService;
import com.tuotiansudai.console.service.SystemRechargeService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.text.MessageFormat;

@Controller
@RequestMapping(value = "/finance-manage")
public class CreditLoanRechargeController {

    @Autowired
    private CreditLoanRechargeService creditLoanRechargeService;

    @Autowired
    private AccountMapper accountMapper;

    @RequestMapping(value = "/credit-loan-recharge",method = RequestMethod.GET)
    public ModelAndView systemRecharge() {
        return new ModelAndView("/credit-loan-recharge");
    }


    @RequestMapping(value = "/credit-loan-recharge",method = RequestMethod.POST)
    public ModelAndView systemRecharge(@Valid @ModelAttribute InvestDto investDto, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("/error/404", "responsive", true);
        String operatorLoginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountMapper.findByLoginName(operatorLoginName);
        investDto.setLoginName(operatorLoginName);

        if (accountModel.isNoPasswordInvest()) {
            try {
                BaseDto<PayDataDto> baseDto = creditLoanRechargeService.NoPasswordCreditLoanRecharge(investDto);
                if (baseDto.getData().getStatus()) {
                    return new ModelAndView("/", "responsive", true);
                }
                redirectAttributes.addFlashAttribute("errorMessage", baseDto.getData().getMessage());
            } catch (InvestException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "充值失败");
            }
            redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
        } else {
            try {
                BaseDto<PayFormDataDto> baseDto = creditLoanRechargeService.creditLoanRecharge(investDto);
                if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                    return new ModelAndView("/pay", "pay", baseDto);
                }
            } catch (InvestException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "充值失败");
            }
            redirectAttributes.addFlashAttribute("investAmount", investDto.getAmount());
        }
        return modelAndView;
    }
}
