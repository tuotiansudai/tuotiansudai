package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.QuickPaymentBank;
import com.tuotiansudai.service.BindBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/bind-card")
public class BindBankCardController {

    @Autowired
    private BindBankCardService bindBankCardService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView bindBankCard() {
        String bindCardStatus = "unbindCard";
        ModelAndView view = new ModelAndView("/bind-card");

        BankCardModel bankCardModel = bindBankCardService.getPassedBankCard();
        if (bankCardModel != null) {
            bindCardStatus = "commonBindCard";
            if (QuickPaymentBank.isQuickPaymentBank(bankCardModel.getBankCode().toUpperCase())) {
                bindCardStatus = "specialBindCard";
            }
            view.addObject("bankNumber", bankCardModel.getBankCode().toUpperCase());
            view.addObject("cardNumber", bankCardModel.getCardNumber());
        }
        view.addObject("userName", bindBankCardService.getUserName());
        view.addObject("bindCardStatus", bindCardStatus);

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
