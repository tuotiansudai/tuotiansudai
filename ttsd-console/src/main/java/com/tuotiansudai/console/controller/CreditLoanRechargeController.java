package com.tuotiansudai.console.controller;


import com.google.common.base.Strings;
import com.tuotiansudai.console.service.CreditLoanRechargeService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
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
    public ModelAndView creditLoanRecharge() {
        return new ModelAndView("/credit-loan-recharge");
    }


    @RequestMapping(value = "/credit-loan-recharge",method = RequestMethod.POST)
    public ModelAndView creditLoanRecharge(@Valid @ModelAttribute CreditLoanRechargeDto creditLoanRechargeDto) {
        ModelAndView modelAndView = new ModelAndView("/credit-loan-recharge", "responsive", true);
        String operatorLoginName = LoginUserInfo.getLoginName();
        AccountModel accountModel = accountMapper.findByMobile(creditLoanRechargeDto.getMobile());

        creditLoanRechargeDto.setOperatorLoginName(operatorLoginName);

        String errorMessage;
        if (accountModel.isNoPasswordInvest()) {
            try {
                BaseDto<PayDataDto> baseDto = creditLoanRechargeService.noPasswordCreditLoanRecharge(creditLoanRechargeDto);
                if (baseDto.getData().getStatus()) {
                    return new ModelAndView("redirect:/");
                }else{
                    errorMessage = baseDto.getData().getMessage();
                }
            } catch (Exception e) {
                errorMessage = e.getLocalizedMessage();
            }
        } else {
            try {
                BaseDto<PayFormDataDto> baseDto = creditLoanRechargeService.creditLoanRecharge(creditLoanRechargeDto);
                if (baseDto.isSuccess() && baseDto.getData().getStatus()) {
                    return new ModelAndView("/pay", "pay", baseDto);
                }else{
                    errorMessage = baseDto.getData().getMessage();
                }
            } catch (Exception e) {
                errorMessage = e.getLocalizedMessage();
            }
        }
        modelAndView.addObject("errorMessage", Strings.isNullOrEmpty(errorMessage) ? null:"充值失败 "+errorMessage);
        return modelAndView;
    }
}
