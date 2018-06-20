package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.repository.model.UserBankCardModel;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.service.BankWithdrawService;
import com.tuotiansudai.service.BankBindCardService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.web.config.interceptors.MobileAccessDecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/withdraw")
public class WithdrawController {

    private final BankWithdrawService bankWithdrawService;

    private final BankAccountService bankAccountService;

    private final BankBindCardService bankBindCardService;

    private long withdrawFee = Long.parseLong(ETCDConfigReader.getReader().getValue("pay.withdraw.fee"));

    @Autowired
    public WithdrawController(BankWithdrawService bankWithdrawService, BankAccountService bankAccountService, BankBindCardService bankBindCardService) {
        this.bankWithdrawService = bankWithdrawService;
        this.bankAccountService = bankAccountService;
        this.bankBindCardService = bankBindCardService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView withdraw() {
        UserBankCardModel bankCard = bankBindCardService.findBankCard(LoginUserInfo.getLoginName());
        if (bankCard == null) {
            return MobileAccessDecision.isMobileAccess() ? new ModelAndView("redirect:/m/personal-info") : new ModelAndView("redirect:/personal-info");
        }

        long balance = bankAccountService.findBankAccount(LoginUserInfo.getLoginName()).getBalance();
        ModelAndView modelAndView = new ModelAndView("/withdraw");
        modelAndView.addObject("bankCard", bankCard);
        modelAndView.addObject("balance", AmountConverter.convertCentToString(balance));
        modelAndView.addObject("withdrawFee", AmountConverter.convertCentToString(withdrawFee));
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView withdraw(@Valid @ModelAttribute WithdrawDto withdrawDto) {
        BankAsyncMessage bankAsyncData = bankWithdrawService.withdraw(withdrawDto.getSource(),
                LoginUserInfo.getLoginName(), LoginUserInfo.getMobile(),
                AmountConverter.convertStringToCent(withdrawDto.getAmount()));
        return new ModelAndView("/pay", "pay", bankAsyncData);
    }
}
