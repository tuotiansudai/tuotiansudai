package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.BlacklistService;
import com.tuotiansudai.service.UmpWithdrawService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.BankCardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/ump/withdraw")
public class UmpWithdraw {

    private final BankCardMapper bankCardMapper;

    private final AccountMapper accountMapper;

    private final BlacklistService blacklistService;

    private final UmpWithdrawService umpWithdrawService;

    @Value("${pay.withdraw.fee}")
    private long withdrawFee;

    @Autowired
    public UmpWithdraw(BankCardMapper bankCardMapper, AccountMapper accountMapper, BlacklistService blacklistService, UmpWithdrawService umpWithdrawService) {
        this.bankCardMapper = bankCardMapper;
        this.accountMapper = accountMapper;
        this.blacklistService = blacklistService;
        this.umpWithdrawService = umpWithdrawService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView withdraw() {
        BankCardModel bankCard = bankCardMapper.findPassedBankCardByLoginName(LoginUserInfo.getLoginName());
        if (bankCard == null) {
            return new ModelAndView("redirect:/bind-card");
        }

        ModelAndView modelAndView = new ModelAndView("/ump-withdraw");
        long balance = accountMapper.findByLoginName(LoginUserInfo.getLoginName()).getBalance();
        boolean hasAccess = !blacklistService.userIsInBlacklist(LoginUserInfo.getLoginName());
        modelAndView.addObject("balance", AmountConverter.convertCentToString(balance));
        modelAndView.addObject("withdrawFee", AmountConverter.convertCentToString(withdrawFee));
        modelAndView.addObject("hasAccess", String.valueOf(hasAccess));
        modelAndView.addObject("bankCard", bankCard);
        modelAndView.addObject("bankName", BankCardUtil.getBankName(bankCard.getBankCode()));
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView withdraw(@Valid @ModelAttribute WithdrawDto withdrawDto) {
        String loginName = LoginUserInfo.getLoginName();
        if (blacklistService.userIsInBlacklist(loginName)) {
            return new ModelAndView("/");
        }
        UmpAsyncMessage message = umpWithdrawService.withdraw(loginName, AmountConverter.convertStringToCent(withdrawDto.getAmount()), withdrawFee);
        return new ModelAndView("/ump-pay", "pay", message);
    }
}
