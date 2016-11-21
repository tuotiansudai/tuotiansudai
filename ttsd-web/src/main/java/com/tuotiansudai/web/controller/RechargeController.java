package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.*;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.BankCardUtil;
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
    private BindBankCardService bindBankCardService;

    @Autowired
    private BankService bankService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView recharge() {
        long balance = accountService.getBalance(LoginUserInfo.getLoginName());
        BankCardModel bankCard = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());
        boolean isBindCard = bankCard != null;
        boolean isFastPayOn = bankCard != null && bankCard.isFastPayOn();

        ModelAndView modelAndView = new ModelAndView("/recharge");
        modelAndView.addObject("balance", AmountConverter.convertCentToString(balance));
        modelAndView.addObject("banks", BankCardUtil.getRechargeBanks());
        modelAndView.addObject("isBindCard", isBindCard);
        modelAndView.addObject("isFastPayOn", isFastPayOn);
        modelAndView.addObject("bankList", bankService.findWebBankList());

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
