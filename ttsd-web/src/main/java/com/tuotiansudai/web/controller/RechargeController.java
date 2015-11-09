package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.utils.AmountUtil;
import com.tuotiansudai.utils.BankCardUtil;
import com.tuotiansudai.utils.LoginUserInfo;
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
    private AccountService accountService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView recharge() {
        AccountModel accountModel = accountService.findByLoginName(LoginUserInfo.getLoginName());
        long balance = accountModel.getBalance();
        ModelAndView modelAndView = new ModelAndView("/recharge");
        BankCardModel bankCard = bindBankCardService.getPassedBankCard();
        boolean isBindCard = bankCard != null;
        boolean isFastPayOn = bankCard != null && bankCard.isFastPayOn();

        modelAndView.addObject("balance", AmountUtil.convertCentToString(balance));
        modelAndView.addObject("banks", BankCardUtil.getRechargeBanks());
        modelAndView.addObject("isBindCard", isBindCard);
        modelAndView.addObject("isFastPayOn", isFastPayOn);
        if (isBindCard) {
            modelAndView.addObject("userName", accountModel.getUserName());
            modelAndView.addObject("identityNumber", accountModel.getIdentityNumber());
            modelAndView.addObject("bankCode", bankCard.getBankCode());
            modelAndView.addObject("bank", BankCardUtil.getBankName(bankCard.getBankCode()));
            modelAndView.addObject("bankCard", bankCard.getCardNumber());
        }

        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView recharge(@Valid @ModelAttribute RechargeDto rechargeDto) {
        BaseDto<PayFormDataDto> baseDto = rechargeService.recharge(rechargeDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
