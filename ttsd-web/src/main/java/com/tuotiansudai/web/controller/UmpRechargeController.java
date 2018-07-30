package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.request.UmpRechargeRequestDto;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.BankService;
import com.tuotiansudai.service.UmpRechargeService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.BankCardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/ump/recharge")
public class UmpRechargeController {

    private final UmpRechargeService umpRechargeService;

    private final AccountMapper accountMapper;

    private final BankCardMapper bankCardMapper;

    private final BankService bankService;

    private final UserService userService;

    @Autowired
    public UmpRechargeController(UmpRechargeService umpRechargeService, AccountMapper accountMapper, BankCardMapper bankCardMapper, BankService bankService, UserService userService){
        this.umpRechargeService = umpRechargeService;
        this.accountMapper = accountMapper;
        this.bankCardMapper = bankCardMapper;
        this.bankService = bankService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView recharge() {
        AccountModel accountModel = accountMapper.findByLoginName(LoginUserInfo.getLoginName());
        BankCardModel bankCard = bankCardMapper.findPassedBankCardByLoginName(LoginUserInfo.getLoginName());
        boolean isBindCard = bankCard != null;
        boolean isFastPayOn = bankCard != null && bankCard.isFastPayOn();

        ModelAndView modelAndView = new ModelAndView("/ump-recharge");
        modelAndView.addObject("balance", AmountConverter.convertCentToString(accountModel.getBalance()));
        modelAndView.addObject("banks", BankCardUtil.getRechargeBanks());
        modelAndView.addObject("isBindCard", isBindCard);
        modelAndView.addObject("isFastPayOn", isFastPayOn);
        modelAndView.addObject("bankList", bankService.findBankList(0L, 0L));

        UserModel userModel = userService.findByMobile(LoginUserInfo.getMobile());
        if (isBindCard) {
            modelAndView.addObject("userName", userModel.getUmpUserName());
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
    public ModelAndView recharge(UmpRechargeRequestDto dto){
        dto.setLoginName(LoginUserInfo.getLoginName());
        UmpAsyncMessage message = umpRechargeService.recharge(dto);
        return new ModelAndView("/ump-pay", "pay", message);
    }
}
