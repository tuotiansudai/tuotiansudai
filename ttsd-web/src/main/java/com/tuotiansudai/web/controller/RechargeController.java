package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.dto.UserRechargeDto;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.service.*;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/recharge")
public class RechargeController {

    @Autowired
    private UserRechargeService userRechargeService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserBindBankCardService userBindBankCardService;

    @Autowired
    private BankService bankService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView recharge() {
        ModelAndView modelAndView = new ModelAndView("/recharge");
        UserBankCardModel userBankCardModel = userBindBankCardService.findBankCard(LoginUserInfo.getLoginName());
        BankAccountModel bankAccountModel = bankAccountService.findBankAccount(LoginUserInfo.getLoginName());
        boolean isBindCard = userBankCardModel != null;
        modelAndView.addObject("balance", AmountConverter.convertCentToString(bankAccountModel == null ? 0 : bankAccountModel.getBalance()));
        modelAndView.addObject("isBindCard", isBindCard);
        modelAndView.addObject("bankList", bankService.findBankList(0L, 0L));

        if (isBindCard) {
            BankModel bankModel = bankService.findByBankCode(userBankCardModel.getBankCode());
            modelAndView.addObject("bankModel", bankModel);
        }

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView recharge(@Valid @ModelAttribute UserRechargeDto userRechargeDto) {
        userRechargeDto.setLoginName(LoginUserInfo.getLoginName());
        BaseDto<PayFormDataDto> baseDto = userRechargeService.recharge(userRechargeDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
