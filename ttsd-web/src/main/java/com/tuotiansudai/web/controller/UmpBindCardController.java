package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.request.UmpBindCardRequestDto;
import com.tuotiansudai.fudian.message.UmpAsyncMessage;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.BankService;
import com.tuotiansudai.service.UmpBindCardService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.BankCardUtil;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DecimalFormat;

@Controller
@RequestMapping(value = "/ump/bind-card")
public class UmpBindCardController {

    private final UmpBindCardService umpBindCardService;

    private final BankService bankService;

    private final UserService userService;

    @Autowired
    public UmpBindCardController(UmpBindCardService umpBindCardService, BankService bankService, UserService userService){
        this.umpBindCardService = umpBindCardService;
        this.bankService = bankService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView bindBankCard() {
        ModelAndView view = new ModelAndView("/ump-bind-card");
        UserModel userModel = userService.findByMobile(LoginUserInfo.getMobile());
        view.addObject("userName", userModel.getUmpUserName());
        view.addObject("banks", BankCardUtil.getWithdrawBanks());
        view.addObject("bankList", bankService.findUmpBankList(0L, 0L));
        return view;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView bindBankCard(@Valid @ModelAttribute UmpBindCardRequestDto dto) {
        dto.setLoginName(LoginUserInfo.getLoginName());
        UmpAsyncMessage message = umpBindCardService.bindBankCard(dto);
        return new ModelAndView("/ump-pay", "pay", message);
    }

    @RequestMapping(value = "/replace", method = RequestMethod.GET)
    public ModelAndView replaceBankCard() {
        ModelAndView view = new ModelAndView("/ump-replace-card");
        view.addObject("userName", userService.findByMobile(LoginUserInfo.getMobile()).getUmpUserName());

        BankCardModel bankCardModel = umpBindCardService.getPassedBankCard(LoginUserInfo.getLoginName());
        if (bankCardModel != null && bankCardModel.isFastPayOn()) {
            view.addObject("banks", BankCardUtil.getFastPayBanks());
        } else {
            view.addObject("banks", BankCardUtil.getWithdrawBanks());
        }
        return view;
    }

    @RequestMapping(value = "/replace", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView replaceBankCard(@Valid @ModelAttribute UmpBindCardRequestDto dto) {
        dto.setLoginName(LoginUserInfo.getLoginName());
        UmpAsyncMessage message = umpBindCardService.replaceBankCard(dto);
        return new ModelAndView("/ump-pay", "pay", message);
    }

    @RequestMapping(value = "/is-replacing", method = RequestMethod.GET)
    @ResponseBody
    public boolean isReplacing() {
        String loginName = LoginUserInfo.getLoginName();
        return umpBindCardService.isReplacing(loginName);
    }

    @RequestMapping(value = "/is-manual", method = RequestMethod.GET)
    @ResponseBody
    public boolean isManual() {
        String loginName = LoginUserInfo.getLoginName();
        BankCardModel bankCardModel = umpBindCardService.getPassedBankCard(LoginUserInfo.getLoginName());
        if (bankCardModel != null && !bankCardModel.isFastPayOn()) {
            return false;
        }
        return umpBindCardService.isManual(loginName);
    }

    @RequestMapping(value = "/limit-tips", method = RequestMethod.GET)
    @ResponseBody
    public String getLimitTips(String bankCode) {
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,###");
        if (bankCode == null) {
            BankCardModel bankCardModel = umpBindCardService.getPassedBankCard(LoginUserInfo.getLoginName());
            if (bankCardModel != null && bankCardModel.isFastPayOn()) {
                bankCode = bankCardModel.getBankCode();
            }
        }
        BankModel bankModel = bankService.findByBankCode(bankCode);
        if (bankModel == null) {
            return "";
        }
        return bankModel.getName() + "快捷支付限额:" + "单笔" + myformat.format(bankModel.getSingleAmount() / 100) + "元/单日" + myformat.format(bankModel.getSingleDayAmount() / 100) + "元";
    }
}
