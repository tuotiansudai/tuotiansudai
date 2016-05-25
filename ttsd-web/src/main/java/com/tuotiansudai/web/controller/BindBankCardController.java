package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.util.BankCardUtil;
import com.tuotiansudai.web.util.LoginUserInfo;
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

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView bindBankCard() {
        ModelAndView view = new ModelAndView("/bind-card");
        BankCardModel bankCardModel = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());

        boolean isBindCard = bankCardModel != null;
        if (isBindCard) {
            view.addObject("openFastPayAvailable", !bankCardModel.isFastPayOn() && BankCardUtil.getFastPayBanks().contains(bankCardModel.getBankCode().toUpperCase()));
            view.addObject("bankCode", bankCardModel.getBankCode().toUpperCase());
            view.addObject("cardNumber", bankCardModel.getCardNumber());
        }

        view.addObject("userName", accountService.findByLoginName(LoginUserInfo.getLoginName()).getUserName());
        view.addObject("isBindCard", isBindCard);
        view.addObject("banks", BankCardUtil.getWithdrawBanks());

        return view;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView bindBankCard(@Valid @ModelAttribute BindBankCardDto bindBankCardDto) {
        bindBankCardDto.setLoginName(LoginUserInfo.getLoginName());
        BaseDto<PayFormDataDto> baseDto = bindBankCardService.bindBankCard(bindBankCardDto);
        ModelAndView view = new ModelAndView("/pay");
        view.addObject("pay", baseDto);
        return view;
    }

    @RequestMapping(value = "/replace", method = RequestMethod.GET)
    public ModelAndView replaceBankCard() {
        ModelAndView view = new ModelAndView("/replace-card");
        view.addObject("userName", accountService.findByLoginName(LoginUserInfo.getLoginName()).getUserName());

        BankCardModel bankCardModel = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());
        if (bankCardModel != null && bankCardModel.isFastPayOn()) {
            view.addObject("banks", BankCardUtil.getFastPayBanks());
        } else {
            view.addObject("banks", BankCardUtil.getWithdrawBanks());
        }
        return view;
    }

    @RequestMapping(value = "/replace", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView replaceBankCard(@Valid @ModelAttribute BindBankCardDto bindBankCardDto) {
        bindBankCardDto.setLoginName(LoginUserInfo.getLoginName());
        BaseDto<PayFormDataDto> baseDto = bindBankCardService.replaceBankCard(bindBankCardDto);
        ModelAndView view = new ModelAndView("/pay");
        view.addObject("pay", baseDto);
        return view;
    }

    @RequestMapping(value = "/is-replacing", method = RequestMethod.GET)
    @ResponseBody
    public boolean isReplacing() {
        String loginName = LoginUserInfo.getLoginName();
        return bindBankCardService.isReplacing(loginName);
    }

    @RequestMapping(value = "/is-manual", method = RequestMethod.GET)
    @ResponseBody
    public boolean isManual() {
        String loginName = LoginUserInfo.getLoginName();
        return bindBankCardService.isManual(loginName);
    }

}
