package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BankService;
import com.tuotiansudai.service.BindBankCardService;
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
@RequestMapping(value = "/bind-card")
public class BindBankCardController {

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private BankService bankService;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView bindBankCard() {
        ModelAndView view = new ModelAndView("/bind-card");

        UserModel userModel = userService.findByMobile(LoginUserInfo.getMobile());
        view.addObject("userName", userModel.getUserName());
        view.addObject("banks", BankCardUtil.getWithdrawBanks());
        view.addObject("bankList", bankService.findBankList(0L, 0L));

        return view;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView bindBankCard(@Valid @ModelAttribute BindBankCardDto bindBankCardDto, HttpServletRequest request) {
        bindBankCardDto.setLoginName(LoginUserInfo.getLoginName());
        bindBankCardDto.setIp(RequestIPParser.parse(request));
        BaseDto<PayFormDataDto> baseDto = bindBankCardService.bindBankCard(bindBankCardDto);
        ModelAndView view = new ModelAndView("/pay");
        view.addObject("pay", baseDto);
        return view;
    }

    @RequestMapping(value = "/replace", method = RequestMethod.GET)
    public ModelAndView replaceBankCard() {
        ModelAndView view = new ModelAndView("/replace-card");
        view.addObject("userName", userService.findByMobile(LoginUserInfo.getMobile()).getUserName());

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
    public ModelAndView replaceBankCard(@Valid @ModelAttribute BindBankCardDto bindBankCardDto, HttpServletRequest request) {
        bindBankCardDto.setLoginName(LoginUserInfo.getLoginName());
        bindBankCardDto.setIp(RequestIPParser.parse(request));
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
        BankCardModel bankCardModel = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());
        if (bankCardModel != null && !bankCardModel.isFastPayOn()) {
            return false;
        }
        return bindBankCardService.isManual(loginName);
    }

    @RequestMapping(value = "/limit-tips", method = RequestMethod.GET)
    @ResponseBody
    public String getLimitTips(String bankCode) {
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,###");
        if (bankCode == null) {
            BankCardModel bankCardModel = bindBankCardService.getPassedBankCard(LoginUserInfo.getLoginName());
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
