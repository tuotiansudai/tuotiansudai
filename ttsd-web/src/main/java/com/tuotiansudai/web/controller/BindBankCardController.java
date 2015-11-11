package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.utils.BankCardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/bind-card")
public class BindBankCardController {

    @Autowired
    private BindBankCardService bindBankCardService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView bindBankCard() {
        ModelAndView view = new ModelAndView("/bind-card");
        BankCardModel bankCardModel = bindBankCardService.getPassedBankCard();

        boolean isBindCard = bankCardModel != null;
        if (isBindCard) {
            view.addObject("openFastPayAvailable", !bankCardModel.isFastPayOn() && BankCardUtil.getFastPayBanks().contains(bankCardModel.getBankCode().toUpperCase()));
            view.addObject("bankCode", bankCardModel.getBankCode().toUpperCase());
            view.addObject("cardNumber", bankCardModel.getCardNumber());
        }

        view.addObject("userName", bindBankCardService.getUserName());
        view.addObject("isBindCard", isBindCard);
        view.addObject("banks", BankCardUtil.getFastPayBanks());

        return view;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView bindBankCard(@Valid @ModelAttribute BindBankCardDto bindBankCardDto) {
        BaseDto<PayFormDataDto> baseDto = bindBankCardService.bindBankCard(bindBankCardDto);
        ModelAndView view = new ModelAndView("/pay");
        view.addObject("pay", baseDto);
        return view;
    }

}
