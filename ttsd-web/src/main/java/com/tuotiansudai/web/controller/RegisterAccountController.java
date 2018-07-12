package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.service.BankAccountService;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.IdentityNumberValidator;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(path = "/register/account")
public class RegisterAccountController {

    private final UserService userService;

    private final BankAccountService bankAccountService;

    @Autowired
    public RegisterAccountController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView registerAccount(@RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect) {
        ModelAndView modelAndView = new ModelAndView("/register-account", "responsive", true);
        modelAndView.addObject("redirect", redirect);
        return modelAndView;
    }

    @RequestMapping(value = "/verify/identity-number/{identityNumber:^[1-9]\\d{13,16}[a-zA-Z0-9]{1}$}", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> isIdentityNumberExist(@PathVariable String identityNumber) {
        BaseDataDto dataDto = new BaseDataDto();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>(dataDto);
        if (!IdentityNumberValidator.validateIdentity(identityNumber)) {
            dataDto.setMessage("身份证不合法");
            return baseDto;
        }
        if (userService.isIdentityNumberExist(identityNumber)){
            dataDto.setMessage("身份证已存在");
            return baseDto;
        }
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/investor", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView registerInvestorAccount(@Valid @ModelAttribute RegisterAccountDto registerAccountDto, HttpServletRequest request) {
        registerAccountDto.setMobile(LoginUserInfo.getMobile());
        registerAccountDto.setLoginName(LoginUserInfo.getLoginName());
        BankAsyncMessage bankAsyncData = bankAccountService.registerInvestorAccount(registerAccountDto, registerAccountDto.getSource(), LoginUserInfo.getToken(), RequestIPParser.parse(request), null);
        return new ModelAndView("/pay", "pay", bankAsyncData);
    }

    @RequestMapping(value = "/loaner", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView registerLoanerAccount(HttpServletRequest request) {
        BankAsyncMessage bankAsyncData = bankAccountService.registerLoanerAccount(LoginUserInfo.getLoginName(), LoginUserInfo.getToken(), RequestIPParser.parse(request), null);
        return new ModelAndView("/pay", "pay", bankAsyncData);
    }
}
