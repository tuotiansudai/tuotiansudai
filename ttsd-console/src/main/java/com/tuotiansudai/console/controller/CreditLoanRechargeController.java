package com.tuotiansudai.console.controller;


import com.tuotiansudai.console.service.CreditLoanRechargeService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
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
    public ModelAndView systemRecharge(@Valid @ModelAttribute CreditLoanRechargeDto creditLoanRechargeDto, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView("/error/404", "responsive", true);
        String operatorLoginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountMapper.findByLoginName(operatorLoginName);
        creditLoanRechargeDto.setOperatorLoginName(operatorLoginName);

        if (accountModel.isNoPasswordInvest()) {
            try {
                BaseDto<PayDataDto> baseDto = creditLoanRechargeService.NoPasswordCreditLoanRecharge(creditLoanRechargeDto);
                if (baseDto.getData().getStatus()) {
                    return new ModelAndView("/", "responsive", true);
                }
                redirectAttributes.addFlashAttribute("errorMessage", baseDto.getData().getMessage());
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "充值失败");
            }
        } else {
            try {
                BaseDto<PayFormDataDto> baseDto = creditLoanRechargeService.creditLoanRecharge(creditLoanRechargeDto);
                if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                    return new ModelAndView("/pay", "pay", baseDto);
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "充值失败");
            }
        }
        return modelAndView;
    }
}
