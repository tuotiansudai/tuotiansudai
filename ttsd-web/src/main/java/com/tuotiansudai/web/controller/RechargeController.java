package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.BankCardUtil;
import com.tuotiansudai.spring.LoginUserInfo;
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
    private RechargeService rechargeService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserBindBankCardService userBindBankCardService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private BankService bankService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView recharge() {
        BankCardModel bankCard = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());
//        boolean isBindCard = bankCard != null;
        boolean isFastPayOn = bankCard != null && bankCard.isFastPayOn();

        ModelAndView modelAndView = new ModelAndView("/recharge");
        UserBankCardModel userBankCardModel = userBindBankCardService.findBankCard(LoginUserInfo.getLoginName());
        BankAccountModel bankAccountModel = bankAccountService.findBankAccount(LoginUserInfo.getLoginName());

        boolean isBindCard = userBankCardModel != null;


        modelAndView.addObject("balance", AmountConverter.convertCentToString(bankAccountModel == null? 0 : bankAccountModel.getBalance()));
        modelAndView.addObject("isBindCard", isBindCard);
        modelAndView.addObject("bankList", bankService.findBankList(0L, 0L));

        UserModel userModel = userService.findByMobile(LoginUserInfo.getMobile());
        if (accountService.findByLoginName(LoginUserInfo.getLoginName()) != null && isBindCard) {
            modelAndView.addObject("userName", userModel.getUserName());
            modelAndView.addObject("identityNumber", userModel.getIdentityNumber());
            modelAndView.addObject("bankCode", bankCard.getBankCode());
            modelAndView.addObject("bank", BankCardUtil.getBankName(bankCard.getBankCode()));
            modelAndView.addObject("bankCard", bankCard.getCardNumber());

            BankModel bankModel = bankService.findByBankCode(bankCard.getBankCode());
            modelAndView.addObject("bankModel", bankModel);
        }

        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView recharge(@Valid @ModelAttribute RechargeDto rechargeDto) {
        rechargeDto.setLoginName(LoginUserInfo.getLoginName());
        BaseDto<PayFormDataDto> baseDto = rechargeService.recharge(rechargeDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
