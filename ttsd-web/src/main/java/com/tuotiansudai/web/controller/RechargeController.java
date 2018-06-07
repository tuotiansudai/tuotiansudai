package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.request.BankRechargeRequestDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.service.BankRechargeService;
import com.tuotiansudai.service.BankService;
import com.tuotiansudai.service.UserBindBankCardService;
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
    private BankRechargeService bankRechargeService;

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
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView recharge(@Valid @ModelAttribute BankRechargeRequestDto dto) {
        BankAsyncMessage baseDto = bankRechargeService.recharge(dto.getSource(),
                LoginUserInfo.getLoginName(),
                LoginUserInfo.getMobile(),
                AmountConverter.convertStringToCent(dto.getAmount()),
                dto.getPayType(),
                dto.getChannel());
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
