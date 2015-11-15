package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView withdraw() {
        BankCardModel bankCard = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());
        if (bankCard == null) {
            return new ModelAndView("redirect:/bind-card");
        }
        long balance = accountService.getBalance(LoginUserInfo.getLoginName());
        return new ModelAndView("/withdraw", "balance", AmountConverter.convertCentToString(balance));
    }


    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView withdraw(@Valid @ModelAttribute WithdrawDto withdrawDto) {
        String loginName = LoginUserInfo.getLoginName();
        withdrawDto.setLoginName(loginName);
        BaseDto<PayFormDataDto> baseDto = withdrawService.withdraw(withdrawDto);
        return new ModelAndView("/pay", "pay", baseDto);
    }
}
